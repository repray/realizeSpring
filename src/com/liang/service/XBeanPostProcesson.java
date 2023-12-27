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
                    // 调用反射的invoke方法,所以在 userservice.test()方法注释掉时,则不会执行该段代码
                    return method.invoke(bean,args);
                }
            });
            return proxyObject;
        }
        return bean;
    }
}
