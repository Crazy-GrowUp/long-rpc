package com.zyl.longrpc.proxy;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.zyl.longrpc.model.RpcRequest;
import com.zyl.longrpc.model.RpcResponse;
import com.zyl.longrpc.serializer.JdkSerializer;
import com.zyl.longrpc.serializer.Serializer;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

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
        Serializer serializer = new JdkSerializer();

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
            // 发送请求
            // TODO 地址被写死了（需要使用注册中心和服务发现机制解决）
            httpResponse = HttpRequest.post("http://localhost:8083")
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
