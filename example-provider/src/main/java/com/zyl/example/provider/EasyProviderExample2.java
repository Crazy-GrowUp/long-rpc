package com.zyl.example.provider;

import com.zyl.example.common.service.UserService;
import com.zyl.longrpc.RpcApplication;
import com.zyl.longrpc.config.RegistryConfig;
import com.zyl.longrpc.config.RpcConfig;
import com.zyl.longrpc.model.ServiceMetaInfo;
import com.zyl.longrpc.registry.LocalRegistry;
import com.zyl.longrpc.registry.Registry;
import com.zyl.longrpc.registry.RegistryFactory;
import com.zyl.longrpc.server.tcp.VertxTcpServer;

/**
 * @program: long-rpc
 * @description: 简易服务提供者示例
 * @author: yl.zhan
 * @create: 2024-11-25 11:14
 **/
public class EasyProviderExample2 {

    public static void main(String[] args) {
        // 初始化RPC
        RpcApplication.init();

        // 注册服务
        LocalRegistry.register(UserService.class.getName(), UserServiceImpl.class);

        // ===================使用Etcd服务注册=================
        RpcConfig rpcConfig = RpcApplication.getRpcConfig();
        RegistryConfig registryConfig = rpcConfig.getRegistryConfig();

        Registry registry = RegistryFactory.getInstance(registryConfig.getRegistry());

        ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
        serviceMetaInfo.setServiceName(UserService.class.getName());
        serviceMetaInfo.setServiceHost(rpcConfig.getServerHost());
        serviceMetaInfo.setServicePort(rpcConfig.getServerPort());

        try {
            registry.register(serviceMetaInfo);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // ===================使用Etcd服务注册end=================

        VertxTcpServer vertxTcpServer = new VertxTcpServer();
        vertxTcpServer.doStart(rpcConfig.getServerPort());

        // 启动服务提供者
//        HttpServerLong httpServer = new VertxHttpServer();
//        httpServer.doStart(8080);
//        httpServer.doStart(RpcApplication.getRpcConfig().getServerPort());
//        httpServer.doStart(ConfigUtils.loadConfig(RpcConfig.class, RpcConstant.DEFAULT_CONFIG_PREFIX).getServerPort());
    }
}
