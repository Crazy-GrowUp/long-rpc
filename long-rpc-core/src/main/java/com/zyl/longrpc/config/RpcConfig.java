package com.zyl.longrpc.config;

import lombok.Data;

/**
 * @program: long-rpc
 * @description: RPC 框架配置
 * @author: yl.zhan
 * @create: 2024-11-28 14:44
 **/
@Data
public class RpcConfig {


    /**
     * 名称
     **/
    private String name = "long-rpc";

    /**
     * 版本号
     **/
    private String version = "1.0";

    /**
     * 服务器主机名
     **/
    private String serverHost = "localhost";

    /**
     * 服务器端口号
     **/
    private Integer serverPort = 8080;

}
