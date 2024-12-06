package com.zyl.longrpc.server.tcp;

import com.google.protobuf.Message;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetSocket;

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

    public static void main(String[] args){
        VertxTcpClient client = new VertxTcpClient();
        client.start("127.0.0.1", 8888);
    }
}
