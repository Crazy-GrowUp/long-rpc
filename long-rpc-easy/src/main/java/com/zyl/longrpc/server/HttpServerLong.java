package com.zyl.longrpc.server;

/**
 * @program: long-rpc
 * @description: HTTP服务器接口
 * @author: yl.zhan
 * @create: 2024-11-25 11:52
 **/
public interface HttpServerLong {

    /*
     * @Description 启动服务
     * @Date 11:53 AM 11/25/2024
     * @Author yl.zhan
     * @param port 端口号
     * @return
     **/
    void doStart(int port);

}
