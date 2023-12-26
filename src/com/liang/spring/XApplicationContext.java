package com.liang.spring;

import java.beans.Introspector;
import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class XApplicationContext {

    private Class config;

    private ConcurrentHashMap<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();

    private ConcurrentHashMap<String, Object> singletonObjects = new ConcurrentHashMap<>();

    private ArrayList<BeanPostProcesson> beanPostProcessonList = new ArrayList<>();

    public XApplicationContext(Class config) {
        this.config = config;

        //扫描,将扫描到的bean放入beanDefinitionMap
        if (config.isAnnotationPresent(ComponentScan.class)) {
            ComponentScan componentScanAnnotation = (ComponentScan) config.getAnnotation(ComponentScan.class);
            //扫描路径
            String path = componentScanAnnotation.value(); //com.liang.service
            path = path.replace(".", "/");
            ClassLoader classLoader = XApplicationContext.class.getClassLoader();
            URL resource = classLoader.getResource(path);
            File file = new File(resource.getFile());

            if (file.isDirectory()) {
                File[] files = file.listFiles();
                for (File f : files) {
                    String fileName = f.getAbsolutePath();

                    if (fileName.endsWith(".class")) {

                        String className = fileName.substring(fileName.indexOf("com"), fileName.indexOf(".class"));
                        className = className.replace("\\", ".");
                        try {
                            Class<?> clazz = classLoader.loadClass(className);
                            //如果存在Component注解,则说明是bean对象
                            if (clazz.isAnnotationPresent(Component.class)) {

                                if (BeanPostProcesson.class.isAssignableFrom(clazz)) {
                                    BeanPostProcesson object = (BeanPostProcesson) clazz.newInstance();
                                    beanPostProcessonList.add(object);

                                }
                                Component component = clazz.getAnnotation(Component.class);
                                String beanName = component.value();

                                if (beanName.equals("")) {
                                    //getSimpleName 获得类的短名称
                                    beanName = Introspector.decapitalize(clazz.getSimpleName());
                                }


                                BeanDefinition beanDefinition = new BeanDefinition();
                                beanDefinition.setType(clazz);
                                //判断bean是单例还是多例
                                if (clazz.isAnnotationPresent(Scope.class)) {
                                    Scope scopeAnnotation = (clazz.getAnnotation(Scope.class));
                                    beanDefinition.setScope(scopeAnnotation.value());
                                }
                                //没有scope注解,则默认是单例
                                else {
                                    beanDefinition.setScope("singleton");
                                }
                                beanDefinitionMap.put(beanName, beanDefinition);

                            }
                        } catch (ClassNotFoundException e) {
                            throw new RuntimeException(e);
                        } catch (InstantiationException e) {
                            throw new RuntimeException(e);
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        }

        //实例化单例bean
        for (String beanName : beanDefinitionMap.keySet()) {
            BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
            Object bean = createBean(beanName, beanDefinition);
            singletonObjects.put(beanName, bean);
        }
    }

    private Object createBean(String beanName, BeanDefinition beanDefinition) {
        Class clazz = beanDefinition.getType();
        try {
            Object object = clazz.getConstructor().newInstance();
            //依赖注入
            for (Field field : clazz.getDeclaredFields()) {
                if (field.isAnnotationPresent(Autowired.class)) {
                    field.setAccessible(true);
                    field.set(object, getBean(field.getName()));
                }
            }
            //Aware beanName回调
            if (object instanceof BeanNameAware) {
                ((BeanNameAware) object).setBeanName(beanName);
            }

            for (BeanPostProcesson beanPostProcesson : beanPostProcessonList) {
                beanPostProcesson.postProcessBeforeInitialization(beanName,object);
            }
            //初始化
            if (object instanceof InitializingBean) {
                ((InitializingBean) object).afterPropertiesSet();
            }

            for (BeanPostProcesson beanPostProcesson : beanPostProcessonList) {
                object = beanPostProcesson.postProcessAfterInitialization(beanName, object);
            }

            //BeanPostProcesson 初始化后 AOP
            return object;
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public Object getBean(String beanName) {
        BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
        if (beanDefinition == null) {
            throw new NullPointerException();
        } else {
            String scope = beanDefinition.getScope();
            //获取单例bean
            //先判断单例池中是否存在
            if (scope.equals("singleton")) {
                Object bean = singletonObjects.get(beanName);
                if (bean == null) {
                    bean = createBean(beanName, beanDefinition);
                    singletonObjects.put(beanName, bean);
                }
                return bean;
            }
            //多例
            else {
                return createBean(beanName, beanDefinition);
            }
        }
    }
}
