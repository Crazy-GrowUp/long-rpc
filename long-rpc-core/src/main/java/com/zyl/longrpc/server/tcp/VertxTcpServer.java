package com.zyl.longrpc.server.tcp;

import com.zyl.longrpc.server.HttpServerLong;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetServer;
import io.vertx.core.net.NetSocket;

/**
 * @program: long-rpc
 * @description: 使用 Vertx 创建 TCP 服务器
 * @author: yl.zhan
 * @create: 2024-12-06 17:13
 **/
public class VertxTcpServer implements HttpServerLong {

    private byte[] handleRequest(byte[] requestData) {
        //处理请求的方法
        return "Hello ,client!".getBytes();
    }

    @Override
    public void doStart(int port) {
        Vertx vertx = Vertx.vertx();
        //TCP服务端
        NetServer netServer = vertx.createNetServer();

        netServer.connectHandler(new TcpServerHandler());

//        netServer.connectHandler(new Handler<NetSocket>() {
//            @Override
//            public void handle(NetSocket netSocket) {
//                netSocket.handler(new Handler<Buffer>() {
//                    @Override
//                    public void handle(Buffer buffer) {
//                        // 获取数据字节
//                        byte[] bytes = buffer.getBytes();
//                        System.out.println("服务器收到：" + String.valueOf(bytes));
//                        //处理消息
//                        byte[] bytes1 = handleRequest(bytes);
//                        netSocket.write(Buffer.buffer(bytes1));
//                    }
//                });
//            }
//        });


        netServer.listen(port,new Handler<AsyncResult<NetServer>>() {
            @Override
            public void handle(AsyncResult<NetServer> netServerAsyncResult) {
                if (netServerAsyncResult.succeeded()) {
                    System.out.println("端口："+port+" TCP服务器启动成功！");
                    //连接成功后可以获取socket对象，使用map存储，这样就可以组发或者给某一个发送信息了
                }else{
                    System.out.println("端口："+port+" TCP服务器启动失败！");
                }
            }
        });
    }

    public static void main(String[] args) {
        VertxTcpServer server = new VertxTcpServer();
        server.doStart(8888);
    }
}
