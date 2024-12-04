package com.zyl.longrpc.config;

import lombok.Data;

/**
 * @program: long-rpc
 * @description: RPC 框架注册中心配置
 * @author: yl.zhan
 * @create: 2024-12-04 15:42
 **/
@Data
public class RegistryConfig {
    /**
     * 注册中心类别  etcd zookeeper redis
     **/
    private String registry = "etcd";

    /**
     * 注册中心地址
     **/
    private String address = "http://localhost:2380";

    /**
     * 用户名
     **/
    private String username;


    /**
     * 密码
     **/
    private String password;

    /**
     * 超时时间（单位毫秒） 默认10秒
     **/
    private Long timeout = 10000L;

}
