package com.zyl.longrpc.bootstrap;

import com.zyl.longrpc.RpcApplication;
import com.zyl.longrpc.config.RegistryConfig;
import com.zyl.longrpc.config.RpcConfig;
import com.zyl.longrpc.model.ServiceMetaInfo;
import com.zyl.longrpc.model.ServiceRegisterInfo;
import com.zyl.longrpc.registry.LocalRegistry;
import com.zyl.longrpc.registry.Registry;
import com.zyl.longrpc.registry.RegistryFactory;
import com.zyl.longrpc.server.tcp.VertxTcpServer;

import java.util.List;

/**
 * @program: long-rpc
 * @description: 服务提供者初始化
 * @author: yl.zhan
 * @create: 2024-12-13 17:33
 **/
public class ProviderBootstrap {

    public static void init(List<ServiceRegisterInfo<?>> serviceRegisterInfos) {
        // 初始化RPC
        RpcApplication.init();

        RpcConfig rpcConfig = RpcApplication.getRpcConfig();
        RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
        for (ServiceRegisterInfo<?> serviceRegisterInfo : serviceRegisterInfos) {
            // 注册到本地服务
            LocalRegistry.register(serviceRegisterInfo.getServiceName(),serviceRegisterInfo.getImplClass());

            Registry registry = RegistryFactory.getInstance(registryConfig.getRegistry());

            ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
            serviceMetaInfo.setServiceName(serviceRegisterInfo.getServiceName());
            serviceMetaInfo.setServiceHost(rpcConfig.getServerHost());
            serviceMetaInfo.setServicePort(rpcConfig.getServerPort());
            try {
                // 注册到注册中心
                registry.register(serviceMetaInfo);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        VertxTcpServer vertxTcpServer = new VertxTcpServer();
        vertxTcpServer.doStart(rpcConfig.getServerPort());
    }
}
