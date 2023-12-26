package com.liang.service;

import com.liang.spring.*;

@Component("userService")
//@Scope("prototype")
public class UserServiceImpl implements UserService{

    @Autowired
    private OrderService orderService;

    private String beanName;

    private String remark;

    public void test() {
        System.out.println(orderService);
    }
}
