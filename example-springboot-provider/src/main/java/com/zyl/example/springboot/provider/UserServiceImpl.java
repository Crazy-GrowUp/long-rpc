package com.zyl.example.springboot.provider;

import com.zyl.example.common.model.User;
import com.zyl.example.common.service.UserService;
import com.zyl.longrpcspringbootstarter.com.zyl.longrpc.springboot.starter.annotation.RpcService;
import org.springframework.stereotype.Service;

import java.util.Random;

/**
 * @program: long-rpc
 * @description: 用户实现类
 * @author: yl.zhan
 * @create: 2024-12-18 17:32
 **/
@Service
@RpcService
public class UserServiceImpl implements UserService {
    @Override
    public User getUser(User user) {
        System.out.println(user.getName());
        user.setName("springboot实现 " + user.getName());
        return user;
    }

    @Override
    public short getNumber() {
        return (short) (new Random().nextInt(10));
    }
}
