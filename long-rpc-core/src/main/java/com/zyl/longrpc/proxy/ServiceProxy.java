package com.zyl.longrpc.proxy;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.http.HttpResponse;
import com.zyl.longrpc.RpcApplication;
import com.zyl.longrpc.config.RpcConfig;
import com.zyl.longrpc.constant.RpcConstant;
import com.zyl.longrpc.loadbalancer.LoadBalancer;
import com.zyl.longrpc.loadbalancer.LoadBalancerFactory;
import com.zyl.longrpc.model.RpcRequest;
import com.zyl.longrpc.model.RpcResponse;
import com.zyl.longrpc.model.ServiceMetaInfo;

import com.zyl.longrpc.registry.Registry;
import com.zyl.longrpc.registry.RegistryFactory;
import com.zyl.longrpc.serializer.Serializer;
import com.zyl.longrpc.serializer.SerializerFactory;
import com.zyl.longrpc.server.tcp.VertxTcpClient;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
                throw new RuntimeException("暂无服务地址");
            }
            // 暂时取第一个
//            ServiceMetaInfo selectedServiceMetaInfo = serviceMetaInfoList.get(0);
            // 使用负载均衡获取服务接口信息
            Map<String,Object> requestParams = new HashMap<>();
            requestParams.put("methodName",method.getName());
            requestParams.put("args",args);
            LoadBalancer loadBalancer = LoadBalancerFactory.getLoadBalancer(RpcApplication.getRpcConfig().getLoadBalance());
            ServiceMetaInfo selectedServiceMetaInfo = loadBalancer.select(requestParams, serviceMetaInfoList);


            //=================从注册中心获取服务提供者的请求地址 end====================


            // 发送请求
            // TODO 地址被写死了（需要使用注册中心和服务发现机制解决）
//            httpResponse = HttpRequest.post("http://localhost:8083")
//                    .body(bodyBytes)
//                    .execute();

            //======================= HTTP请求======================
//            httpResponse = HttpRequest.post(selectedServiceMetaInfo.getServiceAddress())
//                    .body(bodyBytes)
//                    .execute();
//
//            byte[] result = httpResponse.bodyBytes();
//
//            // 反序列化
//            RpcResponse rpcResponse = serializer.deserialize(result, RpcResponse.class);
//
//            return rpcResponse.getData();
            //======================= HTTP请求end======================

            // ========================TCP请求=========================

//            Vertx vertx = Vertx.vertx();
//            NetClient netClient = vertx.createNetClient();
//            // 同步方法
//            CompletableFuture<RpcResponse> completableFuture = new CompletableFuture<>();
//            netClient.connect(selectedServiceMetaInfo.getServicePort(), selectedServiceMetaInfo.getServiceHost(), new Handler<AsyncResult<NetSocket>>() {
//                @Override
//                public void handle(AsyncResult<NetSocket> result) {
//                    //这是一个异步的，需要使用CompletableFuture 等等变成同步
//                    if (result.succeeded()) {
//                        NetSocket socket = result.result();
//                        // 发送请求
//                        ProtocolMessage<RpcRequest> rpcRequestProtocolMessage = new ProtocolMessage<>();
//                        ProtocolMessage.Header header = new ProtocolMessage.Header();
//                        header.setMagic(ProtocolConstant.PROTOCOL_MAGIC);
//                        header.setType((byte) ProtocolMessageTypeEnum.REQUEST.getKey());
//                        header.setStatus((byte)ProtocolMessageStatusEnum.OK.getValue());
//                        header.setSerializer((byte)ProtocolMessageSerializerEnum.getByValue(RpcApplication.getRpcConfig().getSerializer()).getKey());
//                        header.setRequestId(IdUtil.getSnowflakeNextId());
//                        header.setVersion(ProtocolConstant.PROTOCOL_VERSION);
//
//                        rpcRequestProtocolMessage.setHeader(header);
//                        rpcRequestProtocolMessage.setBody(rpcRequest);
//
//                        try {
//                            Buffer encode = ProtocolMessageEncoder.encode(rpcRequestProtocolMessage);
//                            // TCP调用
//                            socket.write(encode);
//                        } catch (IOException e) {
//                            throw new RuntimeException(e);
//                        }
//
//
//                        // 处理响应
//                        socket.handler(new Handler<Buffer>() {
//                            @Override
//                            public void handle(Buffer buffer) {
//                                ProtocolMessage<RpcResponse> protocolMessage;
//                                try {
//                                    protocolMessage = (ProtocolMessage<RpcResponse>) ProtocolMessageDecoder.decode(buffer);
//                                } catch (IOException e) {
//                                    throw new RuntimeException(e);
//                                }
//                                RpcResponse response = protocolMessage.getBody();
//                                completableFuture.complete(response);
//                            }
//                        });
//                    } else {
//                        System.out.println("TCP连接失败,"
//                                + selectedServiceMetaInfo.getServiceAddress()
//                                + ":" + selectedServiceMetaInfo.getServicePort());
//                    }
//                }
//            });
//
//            // 等等完成
//            RpcResponse rpcResponse = completableFuture.get();
//            return rpcResponse.getData();

            RpcResponse rpcResponse = VertxTcpClient.doRpcResponse(rpcRequest, selectedServiceMetaInfo);
            return rpcResponse.getData();
            // ========================TCP请求end======================




        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (httpResponse != null) {
                httpResponse.close();
            }
        }
        return null;
    }
}
