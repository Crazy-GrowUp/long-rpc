package com.zyl.longrpc.proxy;


import com.zyl.longrpc.RpcApplication;
import com.zyl.longrpc.config.RpcConfig;

import java.lang.reflect.Proxy;

/**
 * @program: long-rpc
 * @description: 服务代理工厂（用于创建代理对象）
 * @author: yl.zhan
 * @create: 2024-11-26 17:40
 **/
public class ServiceProxyFactory {

    public static <T> T getProxy(Class<T> serviceClass) {
        //开启模拟数据接口
        if (RpcApplication.getRpcConfig().isMock()) {
            return getMockProxy(serviceClass);
        }
        return (T) Proxy.newProxyInstance(
                serviceClass.getClassLoader(),
                new Class[]{serviceClass},
                new ServiceProxy()
        );
    }

    /**
     * @Description 生成模拟数据接口
     * @Date 3:20 PM 12/2/2024
     * @Author yl.zhan
     * @Param
     * @param serviceClass
     * @return
     * @return T
     **/
    public static <T> T getMockProxy(Class<T> serviceClass){
        return (T) Proxy.newProxyInstance(
                serviceClass.getClassLoader(),
                new Class[]{serviceClass},
                new MockServerProxy()
        );
    }

}
