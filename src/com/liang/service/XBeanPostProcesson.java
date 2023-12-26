package com.liang.service;

import com.liang.spring.BeanPostProcesson;
import com.liang.spring.Component;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

@Component
public class XBeanPostProcesson implements BeanPostProcesson {
    @Override
    public Object postProcessBeforeInitialization(String beanName, Object bean) {
        System.out.println(beanName+" :初始化前");
        if (beanName.equals("userService")) {
            System.out.println("1111");
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(String beanName, Object bean) {
        System.out.println(beanName+" :初始化后");
        if (beanName.equals("userService")) {
            Object proxyObject = Proxy.newProxyInstance(XBeanPostProcesson.class.getClassLoader(), bean.getClass().getInterfaces(), new InvocationHandler() {
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    System.out.println("代理逻辑");
                    return method.invoke(bean,args);
                }
            });
            return proxyObject;
        }
        return bean;

    }
}
