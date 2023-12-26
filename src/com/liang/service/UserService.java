package com.liang.service;

import com.liang.spring.Autowired;
import com.liang.spring.BeanNameAware;
import com.liang.spring.Component;
import com.liang.spring.Scope;

@Component
//@Scope("prototype")
public class UserService implements BeanNameAware {

    @Autowired
    private OrderService orderService;

    private String beanName;

    public void test() {
        System.out.println(orderService);
    }

    @Override
    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

}
