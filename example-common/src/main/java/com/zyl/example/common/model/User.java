package com.zyl.example.common.model;

import java.io.Serializable;

/**
 * @program: long-rpc
 * @description: 用户类
 * @author: yl.zhan
 * @create: 2024-11-25 11:02
 **/
public class User implements Serializable {

    private String name;

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

}
