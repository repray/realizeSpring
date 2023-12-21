package com.liang.spring;

import java.io.File;
import java.lang.annotation.Annotation;
import java.net.URL;

public class XApplicationContext {

    private Class config;

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
            System.out.println(file);
            if (file.isDirectory()){
                File[] files = file.listFiles();
                for (File f : files) {
                    String fileName = f.getAbsolutePath();
                    System.out.println(fileName);
                    if (fileName.endsWith(".class")){

                    }
                }

            }
        }
    }


    public Object getBean(String beanName) {
        return null;
    }
}
