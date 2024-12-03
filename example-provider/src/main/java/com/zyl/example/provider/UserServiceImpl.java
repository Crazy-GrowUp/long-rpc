package com.zyl.example.provider;

import com.zyl.example.common.model.User;
import com.zyl.example.common.service.UserService;

/**
 * @program: long-rpc
 * @description: 用户服务实现类
 * @author: yl.zhan
 * @create: 2024-11-25 11:13
 **/
public class UserServiceImpl implements UserService {
    @Override
    public User getUser(User user) {
        System.out.println("用户名：" + user.getName());
        user.setName("你的名字是："+user.getName());
        return user;
    }

    public short getNumber(){
        return (short)3;
    }
}
