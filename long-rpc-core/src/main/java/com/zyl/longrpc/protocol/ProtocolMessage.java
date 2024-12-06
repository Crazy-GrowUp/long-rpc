package com.zyl.longrpc.protocol;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: long-rpc
 * @description: 协议消息结构
 * @author: yl.zhan
 * @create: 2024-12-06 16:33
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProtocolMessage<T>{

    private Header header;

    private T body;

    /**
     * 消息头
     **/
    @Data
    public static class Header{
        /**
         * 魔数
         **/
        private byte magic;
        /**
         * 版本号
         **/
        private byte version;
        /**
         * 序列化器
         **/
        private byte serializer;
        /**
         * 消息类型（请求/响应）
         **/
        private byte type;
        /**
         * 状态
         **/
        private byte status;
        /**
         * 请求 ID
         **/
        private long requestId;
        /**
         * 消息体长度
         **/
        private int bodyLength;

    }


}
