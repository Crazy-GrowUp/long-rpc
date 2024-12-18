package com.zyl.longrpcspringbootstarter.com.zyl.longrpc.springboot.starter.bootstrap;

import com.zyl.longrpc.RpcApplication;
import com.zyl.longrpc.config.RegistryConfig;
import com.zyl.longrpc.config.RpcConfig;
import com.zyl.longrpc.model.ServiceMetaInfo;
import com.zyl.longrpc.registry.LocalRegistry;
import com.zyl.longrpc.registry.Registry;
import com.zyl.longrpc.registry.RegistryFactory;
import com.zyl.longrpcspringbootstarter.com.zyl.longrpc.springboot.starter.annotation.RpcService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * @program: long-rpc
 * @description: RpcService注解解析类
 * @author: yl.zhan
 * @create: 2024-12-18 16:38
 **/
public class RpcProviderBootstrap implements BeanPostProcessor { //在SpringBean初始化后执行

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> beanClass = bean.getClass();
        // 反射
        RpcService rpcService = beanClass.getAnnotation(RpcService.class);
        if (rpcService != null) {
            Class<?> interfaceClass = rpcService.interfaceClass();
            if (interfaceClass == void.class) {
                interfaceClass = beanClass.getInterfaces()[0];
            }

            String serviceName = interfaceClass.getName();
            String serviceVersion = rpcService.serviceVersion();
            // 注册服务
            // 本地注册
            LocalRegistry.register(serviceName, beanClass);

            final RpcConfig rpcConfig = RpcApplication.getRpcConfig();
            // 获取注册中心信息
            RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
            // 获取注册中心对象
            Registry registry = RegistryFactory.getInstance(registryConfig.getRegistry());

            ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
            serviceMetaInfo.setServiceName(serviceName);
            serviceMetaInfo.setServiceVersion(serviceVersion);
            serviceMetaInfo.setServicePort(rpcConfig.getServerPort());
            serviceMetaInfo.setServiceHost(rpcConfig.getServerHost());

            try {
                // 注册到远程服务中心
                registry.register(serviceMetaInfo);
            } catch (Exception e) {
                throw new RuntimeException(serviceName + ",服务注册失败", e);
            }

        }
        return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
    }
}
