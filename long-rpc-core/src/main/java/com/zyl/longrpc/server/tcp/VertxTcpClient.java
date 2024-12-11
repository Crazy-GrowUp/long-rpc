package com.zyl.longrpc.server.tcp;

import cn.hutool.core.util.IdUtil;
import com.google.protobuf.Message;
import com.zyl.longrpc.RpcApplication;
import com.zyl.longrpc.model.RpcRequest;
import com.zyl.longrpc.model.RpcResponse;
import com.zyl.longrpc.model.ServiceMetaInfo;
import com.zyl.longrpc.protocol.*;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetSocket;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @program: long-rpc
 * @description: 使用Vertx创建TCP客户端
 * @author: yl.zhan
 * @create: 2024-12-06 17:32
 **/
public class VertxTcpClient {

    public void start(String ip, int port) {
        Vertx vertx = Vertx.vertx();
        //TCP 客户端
        NetClient netClient = vertx.createNetClient();

        netClient.connect(port, ip, new Handler<AsyncResult<NetSocket>>() {
            @Override
            public void handle(AsyncResult<NetSocket> netSocketAsyncResult) {
                if (netSocketAsyncResult.succeeded()) {
                    //连接成功
                    System.out.println("连接TCP服务器成功," + ip + ":" + port);

                    //连接成功后可以获取socket对象，使用map存储，这样就可以组发或者给某一个发送信息了

                    NetSocket socket = netSocketAsyncResult.result();

                    // 发送信息
                    socket.write("Hello, Server!");

                    // 接受消息并处理
                    socket.handler(new Handler<Buffer>() {
                        @Override
                        public void handle(Buffer buffer) {
                            byte[] bytes = buffer.getBytes();
                            System.out.println("收到服务端信息："+new String(bytes));
                        }
                    });
                }else{
                    System.out.println("连接TCP失败,"+netSocketAsyncResult.cause());
                }
            }
        });
    }

    public static RpcResponse doRpcResponse(RpcRequest rpcRequest, ServiceMetaInfo serviceMetaInfo) throws ExecutionException, InterruptedException {
        Vertx vertx = Vertx.vertx();
        NetClient netClient = vertx.createNetClient();
        Integer servicePort = serviceMetaInfo.getServicePort();
        String serviceHost = serviceMetaInfo.getServiceHost();

        CompletableFuture<RpcResponse> completableFuture = new CompletableFuture<>();
        netClient.connect(servicePort,serviceHost,new Handler<AsyncResult<NetSocket>>() {
            @Override
            public void handle(AsyncResult<NetSocket> netSocketAsyncResult) {
                if (netSocketAsyncResult.succeeded()) {
                    NetSocket socket = netSocketAsyncResult.result();
                    // 发送
                    ProtocolMessage.Header header = new ProtocolMessage.Header();
                    header.setMagic(ProtocolConstant.PROTOCOL_MAGIC);
                    header.setSerializer((byte) ProtocolMessageSerializerEnum.getByValue(RpcApplication.getRpcConfig().getSerializer()).getKey());
                    header.setType((byte) ProtocolMessageTypeEnum.REQUEST.getKey());
                    header.setStatus((byte) ProtocolMessageStatusEnum.OK.getValue());
                    header.setRequestId(IdUtil.getSnowflakeNextId());
                    header.setVersion(ProtocolConstant.PROTOCOL_VERSION);
                    try {
                        Buffer snedBuffer = ProtocolMessageEncoder.encode(new ProtocolMessage<>(header, rpcRequest));
                        socket.write(snedBuffer);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    // 解析
                    TcpBufferHandlerWrapper tcpBufferHandlerWrapper = new TcpBufferHandlerWrapper(buffer -> {
                        //反射调用
                        try {
                            ProtocolMessage<RpcResponse> decode = (ProtocolMessage<RpcResponse>) ProtocolMessageDecoder.decode(buffer);
                            RpcResponse body = decode.getBody();
                            completableFuture.complete(body);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }

                    });
                    socket.handler(tcpBufferHandlerWrapper);
                }
            }
        });
        RpcResponse rpcResponse = completableFuture.get();
        netClient.close();
        return rpcResponse;

    }

    public static void main(String[] args){
        VertxTcpClient client = new VertxTcpClient();
        client.start("127.0.0.1", 8888);
    }
}
