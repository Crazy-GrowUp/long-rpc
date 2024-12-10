package com.zyl.longrpc.protocol;

/**
 * @program: long-rpc
 * @description: 协议常量
 * @author: yl.zhan
 * @create: 2024-12-06 16:45
 **/
public interface ProtocolConstant {

    /**
     * 消息头长度
     **/
    int MESSAGE_HEADER_LENGTH = 17;

    /**
     * 协议版本
     **/
    byte PROTOCOL_VERSION = 0x01;

    /**
     * 协议魔数  （确实可以改成crc校验，但是crc一般是放在最后）
     **/
    byte PROTOCOL_MAGIC = 0x01;

}
