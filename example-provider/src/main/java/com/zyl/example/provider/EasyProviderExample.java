package com.zyl.example.provider;

import com.zyl.example.common.service.UserService;
import com.zyl.longrpc.RpcApplication;
import com.zyl.longrpc.registry.LocalRegistry;
import com.zyl.longrpc.server.HttpServerLong;
import com.zyl.longrpc.server.VertxHttpServer;

/**
 * @program: long-rpc
 * @description: 简易服务提供者示例
 * @author: yl.zhan
 * @create: 2024-11-25 11:14
 **/
public class EasyProviderExample {

    public static void main(String[] args) {
        // 初始化RPC
        RpcApplication.init();

        // 注册服务
        LocalRegistry.register(UserService.class.getName(), UserServiceImpl.class);
        // 启动服务提供者
        HttpServerLong httpServer = new VertxHttpServer();
//        httpServer.doStart(8080);
        httpServer.doStart(RpcApplication.getRpcConfig().getServerPort());
//        httpServer.doStart(ConfigUtils.loadConfig(RpcConfig.class, RpcConstant.DEFAULT_CONFIG_PREFIX).getServerPort());
    }
}
