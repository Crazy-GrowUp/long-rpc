package com.zyl.example.consumer;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.zyl.example.common.model.User;
import com.zyl.example.common.service.UserService;
import com.zyl.longrpc.model.RpcRequest;
import com.zyl.longrpc.model.RpcResponse;
import com.zyl.longrpc.serializer.JdkSerializer;
import com.zyl.longrpc.serializer.Serializer;

import java.io.IOException;

/**
 * @program: long-rpc
 * @description: 用户服务 静态代理
 * @author: yl.zhan
 * @create: 2024-11-26 15:40
 **/
public class UserServiceProxy implements UserService {
    @Override
    public User getUser(User user) {

        // 指定序列化器
        Serializer serializer = new JdkSerializer();

        // 发送请求
        RpcRequest rpcRequest = RpcRequest.builder()
                .serviceName(UserService.class.getName())
                .methodName("getUser")
                .parameterTypes(new Class[]{User.class})
                .args(new Object[]{user})
                .build();


        try {
            // 序列化
            byte[] bodyBytes = serializer.serialize(rpcRequest);
            byte[] result = new byte[]{};
            HttpResponse httpResponse = null;
            try {
                httpResponse = HttpRequest.post("http://localhost:8083").body(bodyBytes).execute();

                result = httpResponse.bodyBytes();
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                if (httpResponse != null) {
                    httpResponse.close();
                }
            }

            RpcResponse rpcResponse = serializer.deserialize(result, RpcResponse.class);

            return (User) rpcResponse.getData();
        } catch (IOException e) {
//            throw new RuntimeException(e);
            e.printStackTrace();
        }

        return null;
    }
}
