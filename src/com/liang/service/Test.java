package com.liang.service;

import com.liang.spring.XApplicationContext;

public class Test {
    public static void main(String[] args) {
        XApplicationContext xApplicationContext = new XApplicationContext(AppConfig.class);
//        UserService userservice = (UserService) xApplicationContext.getBean("userService");
        System.out.println(xApplicationContext.getBean("userService"));
        System.out.println(xApplicationContext.getBean("userService"));
        System.out.println(xApplicationContext.getBean("userService"));
        System.out.println(xApplicationContext.getBean("userService"));
    }
}
