package com.liang.service;

import com.liang.spring.*;

@Component
//@Scope("prototype")
public class UserService implements BeanNameAware, InitializingBean {

    @Autowired
    private OrderService orderService;

    private String beanName;

    private String remark;

    public void test() {
        System.out.println(orderService);
    }

    @Override
    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    @Override
    public void afterPropertiesSet() {
        System.out.println("test: afterPropertiesSet");
    }
}
