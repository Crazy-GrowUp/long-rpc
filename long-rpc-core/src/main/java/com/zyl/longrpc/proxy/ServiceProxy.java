package com.zyl.longrpc.proxy;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.zyl.longrpc.RpcApplication;
import com.zyl.longrpc.config.RpcConfig;
import com.zyl.longrpc.constant.RpcConstant;
import com.zyl.longrpc.model.RpcRequest;
import com.zyl.longrpc.model.RpcResponse;
import com.zyl.longrpc.model.ServiceMetaInfo;
import com.zyl.longrpc.registry.Registry;
import com.zyl.longrpc.registry.RegistryFactory;
import com.zyl.longrpc.serializer.JdkSerializer;
import com.zyl.longrpc.serializer.Serializer;
import com.zyl.longrpc.serializer.SerializerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @program: long-rpc
 * @description: 服务代理（JDK动态代理）
 * @author: yl.zhan
 * @create: 2024-11-26 17:30
 **/
public class ServiceProxy implements InvocationHandler {


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 指定序列化器
//        Serializer serializer = new JdkSerializer();
        // 通过用户配置来获取对应的序列化器
        final Serializer serializer = SerializerFactory.getInstance(RpcApplication.getRpcConfig().getSerializer());

        // 构造请求
        RpcRequest rpcRequest = RpcRequest.builder()
                .serviceName(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .parameterTypes(method.getParameterTypes())
                .args(args)
                .build();
        HttpResponse httpResponse = null;
        try {
            // 序列化
            byte[] bodyBytes = serializer.serialize(rpcRequest);

            //=================从注册中心获取服务提供者的请求地址====================
            RpcConfig rpcConfig = RpcApplication.getRpcConfig();
            Registry registry = RegistryFactory.getInstance(rpcConfig.getRegistryConfig().getRegistry());
            ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
            serviceMetaInfo.setServiceName(method.getDeclaringClass().getName());
            serviceMetaInfo.setServiceVersion(RpcConstant.DEFAULT_SERVICE_VERSION);
            // 查看对于的服务列表
            List<ServiceMetaInfo> serviceMetaInfoList = registry.serviceDiscovery(serviceMetaInfo.getServiceKey());
            if (CollUtil.isEmpty(serviceMetaInfoList)) {
                throw  new RuntimeException("暂无服务地址");
            }
            // 暂时取第一个
            ServiceMetaInfo selectedServiceMetaInfo = serviceMetaInfoList.get(0);

            //=================从注册中心获取服务提供者的请求地址 end====================


            // 发送请求
            // TODO 地址被写死了（需要使用注册中心和服务发现机制解决）
//            httpResponse = HttpRequest.post("http://localhost:8083")
//                    .body(bodyBytes)
//                    .execute();


            httpResponse = HttpRequest.post(selectedServiceMetaInfo.getServiceAddress())
                    .body(bodyBytes)
                    .execute();

            byte[] result = httpResponse.bodyBytes();

            // 反序列化
            RpcResponse rpcResponse = serializer.deserialize(result, RpcResponse.class);

            return rpcResponse.getData();

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (httpResponse != null) {
                httpResponse.close();
            }
        }
        return null;
    }
}
