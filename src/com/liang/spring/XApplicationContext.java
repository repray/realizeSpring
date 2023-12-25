package com.liang.spring;

import java.io.File;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.concurrent.ConcurrentHashMap;

public class XApplicationContext {

    private Class config;

    private ConcurrentHashMap<String, BeanDefinition> beanDefinitionMap;

    public XApplicationContext(Class config) {
        this.config = config;

        //扫描
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
                        System.out.println(className);
                        try {
                            Class<?> clazz = classLoader.loadClass(className);
                            //如果存在Component注解,则说明是bean对象
                            if (clazz.isAnnotationPresent(Component.class)) {

                                Component component = clazz.getAnnotation(Component.class);
                                String beanName = component.value();
                                BeanDefinition beanDefinition = new BeanDefinition();
                                beanDefinition.setType(clazz);
                                //判断bean是单例还是多例
                                if (clazz.isAnnotationPresent(Scope.class)) {
                                    Scope scopeAnnotation = (clazz.getAnnotation(Scope.class));
                                    beanDefinition.setScope(scopeAnnotation.value());
                                }
                                //没有scope注解,则默认是单例
                                else {
                                    beanDefinition.setScope("singletion");
                                }
                                beanDefinitionMap.put(beanName,beanDefinition);

                            }
                        } catch (ClassNotFoundException e) {
                            throw new RuntimeException(e);
                        }

                    }
                }

            }
        }
    }


    public Object getBean(String beanName) {
        return null;
    }
}
