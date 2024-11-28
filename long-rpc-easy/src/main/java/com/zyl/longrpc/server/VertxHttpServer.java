package com.zyl.longrpc.server;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;

/**
 * @program: long-rpc
 * @description: Vertx服务器实现
 * @author: yl.zhan
 * @create: 2024-11-25 11:56
 **/
public class VertxHttpServer implements HttpServerLong{
    public void doStart(int port) {
        Vertx vertx = Vertx.vertx();

        HttpServer httpServer = vertx.createHttpServer();

        // 使用RPC来处理
        httpServer.requestHandler(new HttpServerHandler());

//        httpServer.requestHandler(request->{
//            // 处理http请求
//            System.out.println("uri:"+request.uri()+", method"+ request.method());
//
//            // 响应 发送HTTP请求
//            request.response()
//                    .putHeader("content-type","text/plain")
//                    .end("Hello VertxServer");
//        });

        httpServer.listen(port, new Handler<AsyncResult<HttpServer>>() {
            @Override
            public void handle(AsyncResult<HttpServer> httpServerAsyncResult) {
                // 查看是否启动成功
                if (httpServerAsyncResult.succeeded()) {
                    System.out.println("Server start success "+ port);
                }else{
                    System.out.println("Server start fail "+ port+", result:"+httpServerAsyncResult.cause());
                }
            }
        });


    }
}
