package com.zyl.example.consumer;

import com.zyl.longrpc.RpcApplication;
import com.zyl.longrpc.config.RpcConfig;
import com.zyl.longrpc.constant.RpcConstant;
import com.zyl.longrpc.utlis.ConfigUtils;

/**
 * @program: long-rpc
 * @description: 测试类
 * @author: yl.zhan
 * @create: 2024-11-28 15:17
 **/
public class ConsumerExample {
    public static void main(String[] args) {

//        RpcConfig rpcConfig = RpcApplication.getRpcConfig();
        RpcConfig rpcConfig = ConfigUtils.loadConfig(RpcConfig.class, RpcConstant.DEFAULT_CONFIG_PREFIX);
        System.out.println(rpcConfig.getVersion());
        System.out.println(rpcConfig.getServerPort());
        System.out.println(rpcConfig.getName());

    }
}
