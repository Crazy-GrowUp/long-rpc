package com.zyl.longrpc.server;

import com.zyl.longrpc.model.RpcRequest;
import com.zyl.longrpc.model.RpcResponse;
import com.zyl.longrpc.registry.LocalRegistry;
import com.zyl.longrpc.serializer.JdkSerializer;
import com.zyl.longrpc.serializer.Serializer;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;

import java.io.IOException;
import java.lang.reflect.Method;

/**
 * @program: long-rpc
 * @description: Http 服务请求处理器
 * @author: yl.zhan
 * @create: 2024-11-26 15:10
 **/
public class HttpServerHandler implements Handler<HttpServerRequest> {

    @Override
    public void handle(HttpServerRequest request) {
        // 指定序列化器
        final Serializer serializer = new JdkSerializer();

        // 记录日志
        System.out.println("Received request:" + request.method() + "  " + request.uri());

        // 异步处理HTTP请求
        request.bodyHandler(new Handler<Buffer>() {
            @Override
            public void handle(Buffer body) {
                byte[] bytes = body.getBytes();
                RpcRequest rpcRequest = null;
                try {
                    // 反序列化
                    rpcRequest = serializer.deserialize(bytes, RpcRequest.class);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // 构造响应结果对象
                RpcResponse rpcResponse = new RpcResponse();

                if (rpcRequest == null) {
                    rpcResponse.setMessage("rpcRequest is null");
                    doResponse(request, rpcResponse, serializer);
                    return;
                }

                try {
                    // 获取要调用的服务实现类，通过反射调用
                    Class<?> implClass = LocalRegistry.get(rpcRequest.getServiceName());
                    Method method = implClass.getMethod(rpcRequest.getMethodName(), rpcRequest.getParameterTypes());
                    Object result = method.invoke(implClass.newInstance(), rpcRequest.getArgs());

                    // 封装返回结果
                    rpcResponse.setData(result);
                    rpcResponse.setDataType(method.getReturnType());
                    rpcResponse.setMessage("OK");
                } catch (Exception e) {
                    e.printStackTrace();
                    rpcResponse.setMessage(e.getMessage());
                    rpcResponse.setException(e);
                }
                doResponse(request, rpcResponse, serializer);
            }
        });


    }

    /**
     * @Description 响应
     * @Date 3:31 PM 11/26/2024
     * @Author yl.zhan
     * @param requset
     * @param rpcResponse
     * @param serializer
     **/
    void doResponse(HttpServerRequest requset, RpcResponse rpcResponse, Serializer serializer) {
        HttpServerResponse httpServerResponse = requset.response()
                .putHeader("content-type", "application/json; charset=utf-8");

        try{
            // 序列化
            byte[] serialized = serializer.serialize(rpcResponse);
            // 发送
            httpServerResponse.end(Buffer.buffer(serialized));
        }catch (IOException e){
            e.printStackTrace();
            // 错误就发送一个空的
            httpServerResponse.end(Buffer.buffer());
        }
    }
}
