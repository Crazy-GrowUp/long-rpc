package com.zyl.longrpc.config;

import com.zyl.longrpc.fault.tolerant.TolerantStrategyKeys;
import com.zyl.longrpc.loadbalancer.LoadBalancerKeys;
import com.zyl.longrpc.serializer.SerializerKeys;
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

    /**
     * 模拟调用（开启模拟数据模式）
     **/
    private boolean mock = false;

    /**
     * 序列化器
     **/
    private String serializer = SerializerKeys.JDK;

    /**
     * 注册中心配置
     **/
    private RegistryConfig registryConfig = new RegistryConfig();

    private String loadBalance = LoadBalancerKeys.RANDOM;


    private RetryStrategyConfig retryStrategyConfig = new RetryStrategyConfig();


    private String tolerantStrategy = TolerantStrategyKeys.FAIL_FAST;


}
