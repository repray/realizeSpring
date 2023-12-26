package com.liang.service;

import com.liang.spring.Autowired;
import com.liang.spring.Component;
import com.liang.spring.Scope;

@Component
//@Scope("prototype")
public class UserService {

    @Autowired
    private OrderService orderService;
    public void test(){
        System.out.println(orderService);
    }
}
