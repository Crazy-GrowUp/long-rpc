package com.zyl.example.consumer;

import com.zyl.example.common.model.User;
import com.zyl.example.common.service.UserService;
import com.zyl.longrpc.bootstrap.ConsumerBootstrap;
import com.zyl.longrpc.config.RpcConfig;
import com.zyl.longrpc.constant.RpcConstant;
import com.zyl.longrpc.proxy.ServiceProxyFactory;
import com.zyl.longrpc.utlis.ConfigUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * @program: long-rpc
 * @description: 测试类
 * @author: yl.zhan
 * @create: 2024-11-28 15:17
 **/
@Slf4j
public class ConsumerExample {
    public static void main(String[] args) {
        ConsumerBootstrap.init();

//        RpcConfig rpcConfig = RpcApplication.getRpcConfig();
//        RpcConfig rpcConfig = ConfigUtils.loadConfig(RpcConfig.class, RpcConstant.DEFAULT_CONFIG_PREFIX);

        UserService userService = ServiceProxyFactory.getProxy(UserService.class);

        short number = userService.getNumber();
        User user = new User();
        user.setName("1");
        User user1 = userService.getUser(user);
        System.out.println("user1:" + user1);

        user.setName("2");
        user1 = userService.getUser(user);
        System.out.println("user2:" + user1);

        user.setName("3");
        user1 = userService.getUser(user);
        System.out.println("user3:" + user1);

        user.setName("4");
        user1 = userService.getUser(user);
        System.out.println("user4:" + user1.getName());

        user.setName("5");
        user1 = userService.getUser(user);
        System.out.println("user5:" + user1.getName());

        user.setName("6");
        user1 = userService.getUser(user);
        System.out.println("user6:" + user1.getName());


//        System.out.println("number:"+number);

//        UserService userService2 = ServiceProxyFactory.getProxy(UserService.class);
//        User user2 = userService2.getUser(user);
//        short number1= userService2.getNumber();
//        System.out.println("user2:"+user2);
//        UserService userService3 = ServiceProxyFactory.getProxy(UserService.class);
//        User user3 = userService3.getUser(user);
//        short num3 = userService3.getNumber();


    }
}
