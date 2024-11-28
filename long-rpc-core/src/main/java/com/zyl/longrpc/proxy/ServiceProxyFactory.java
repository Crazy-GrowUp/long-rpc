package com.zyl.longrpc.proxy;

import com.zyl.longrpc.proxy.ServiceProxy;

import java.lang.reflect.Proxy;

/**
 * @program: long-rpc
 * @description: 服务代理工厂（用于创建代理对象）
 * @author: yl.zhan
 * @create: 2024-11-26 17:40
 **/
public class ServiceProxyFactory {

    public static <T> T getProxy(Class<T> serviceClass) {
        return (T) Proxy.newProxyInstance(
                serviceClass.getClassLoader(),
                new Class[]{serviceClass},
                new ServiceProxy()
        );
    }

}
