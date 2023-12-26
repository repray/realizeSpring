package com.liang.spring;

public interface BeanPostProcesson {

    public Object postProcessBeforeInitialization(String beanName,Object bean);

    public Object postProcessAfterInitialization(String beanName,Object bean);
}
