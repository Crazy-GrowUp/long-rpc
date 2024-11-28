package com.zyl.example.consumer;

import com.zyl.example.common.model.User;
import com.zyl.example.common.service.UserService;
import com.zyl.longrpc.proxy.ServiceProxyFactory;

/**
 * @program: long-rpc
 * @description: 简单服务消费者示例
 * @author: yl.zhan
 * @create: 2024-11-25 11:19
 **/
public class EasyConsumerExample {

    public static void main(String[] args){
//        UserService userService = null;
        // 静态代理
//        UserService userService = new UserServiceProxy();

        // 动态代理
        UserService userService = ServiceProxyFactory.getProxy(UserService.class);

        User user = new User();

        user.setName("zyl");

        User newUser = userService.getUser(user);

        if(newUser != null){
            System.out.println(newUser.getName());
        }else{
            System.out.println("没有找到用户");
        }

    }

}
