package com.zyl.longrpc.server.tcp;

import com.zyl.longrpc.model.RpcRequest;
import com.zyl.longrpc.model.RpcResponse;
import com.zyl.longrpc.protocol.*;
import com.zyl.longrpc.registry.LocalRegistry;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetSocket;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @program: long-rpc
 * @description: TCP服务处理器
 * @author: yl.zhan
 * @create: 2024-12-10 17:14
 **/
public class TcpServerHandler implements Handler<NetSocket> {


    @Override
    public void handle(NetSocket netSocket) {
        netSocket.handler(buffer -> {
            ProtocolMessage<RpcRequest> protocolMessage;
            try {
                protocolMessage = (ProtocolMessage<RpcRequest>) ProtocolMessageDecoder.decode(buffer);
            } catch (IOException e) {
                throw new RuntimeException("消息协议解码错误",e);
            }

            RpcResponse rpcResponse = new RpcResponse();
            RpcRequest rpcRequest = protocolMessage.getBody();
            //使用反射调用方法
            try {
                Class<?> aClass = LocalRegistry.get(rpcRequest.getServiceName());
                Method method;
                if(rpcRequest.getParameterTypes().length>0){
                    // 有参方法
                    method = aClass.getMethod(rpcRequest.getMethodName(), rpcRequest.getParameterTypes());
                }else{
                    //无参方法
                    method = aClass.getMethod(rpcRequest.getMethodName());
                }
                Object result;
                if(rpcRequest.getArgs()==null){
                     result = method.invoke(aClass.getDeclaredConstructor().newInstance());
                }else{
                    result = method.invoke(aClass.getDeclaredConstructor().newInstance(),rpcRequest.getArgs());
                }
                rpcResponse.setData(result);
                rpcResponse.setDataType(method.getReturnType());
                rpcResponse.setMessage("OK");

            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            }


            ProtocolMessage.Header header = protocolMessage.getHeader();
            System.out.println("magic:"+header.getMagic());
            header.setType((byte)ProtocolMessageTypeEnum.RESPONSE.getKey());

            ProtocolMessage<RpcResponse> sendProtocolMessage = new ProtocolMessage<>(header, rpcResponse);

            try {
                Buffer encode = ProtocolMessageEncoder.encode(sendProtocolMessage);
                netSocket.write(encode);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
