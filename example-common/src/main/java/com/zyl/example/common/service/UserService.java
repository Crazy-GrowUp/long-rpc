package com.zyl.example.common.service;

import com.zyl.example.common.model.User;

/**
 * @program: long-rpc
 * @description: 用户服务类
 * @author: yl.zhan
 * @create: 2024-11-25 11:03
 **/
public interface UserService{

    /*
     * @Description 获取用户信息
     * @Date 11:06 AM 11/25/2024
     * @Author yl.zhan
     * @Param
     * @param user
     * @return
     * @return com.zyl.example.common.model.User
     **/
    User getUser(User user);

}
