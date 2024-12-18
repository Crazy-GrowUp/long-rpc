package com.zyl.example.springboot.consumer;

import com.zyl.example.common.model.User;
import com.zyl.example.common.service.UserService;
import com.zyl.longrpcspringbootstarter.com.zyl.longrpc.springboot.starter.annotation.RpcReference;
import org.springframework.stereotype.Service;

/**
 * @program: long-rpc
 * @description:
 * @author: yl.zhan
 * @create: 2024-12-18 17:35
 **/
@Service
public class ExampleServiceImpl {
    @RpcReference
    UserService userService;

    public void test(){
        User user = new User();
        user.setName("zyl");
        User user1 = userService.getUser(user);
        System.out.println("rpc-"+user.getName());
        System.out.println("rpc1-"+user1.getName());
    }

}
