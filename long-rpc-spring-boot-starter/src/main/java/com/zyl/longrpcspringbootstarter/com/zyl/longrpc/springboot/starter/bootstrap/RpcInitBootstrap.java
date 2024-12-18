package com.zyl.longrpcspringbootstarter.com.zyl.longrpc.springboot.starter.bootstrap;

import com.zyl.longrpc.RpcApplication;
import com.zyl.longrpc.config.RpcConfig;
import com.zyl.longrpc.server.tcp.VertxTcpServer;
import com.zyl.longrpcspringbootstarter.com.zyl.longrpc.springboot.starter.annotation.EnableRpc;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Map;

/**
 * @program: long-rpc
 * @description: EnableRpc注解解析类
 * @author: yl.zhan
 * @create: 2024-12-18 16:37
 **/
@Slf4j
public class RpcInitBootstrap implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        Map<String, Object> annotationAttributes = importingClassMetadata.getAnnotationAttributes(EnableRpc.class.getName());
        boolean needServer = (boolean) annotationAttributes.get("needServer");
        // 初始化RPC（初始化注册中心）
        RpcApplication.init();

        final RpcConfig rpcConfig = RpcApplication.getRpcConfig();

        if (needServer) {
            //启动服务器
            VertxTcpServer vertxTcpServer = new VertxTcpServer();
            vertxTcpServer.doStart(rpcConfig.getServerPort());
        } else {
            log.info("不启动 Server");
        }

        ImportBeanDefinitionRegistrar.super.registerBeanDefinitions(importingClassMetadata, registry);
    }
}
