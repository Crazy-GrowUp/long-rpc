package com.zyl.longrpc.protocol;

import com.zyl.longrpc.serializer.Serializer;
import com.zyl.longrpc.serializer.SerializerFactory;
import io.vertx.core.buffer.Buffer;

import java.io.IOException;

/**
 * @program: long-rpc
 * @description: 消息编码器
 * @author: yl.zhan
 * @create: 2024-12-10 15:34
 **/
public class ProtocolMessageEncoder {
    /**
     * @param protocolMessage 源数据对象
     * @return io.vertx.core.buffer.Buffer
     * @Description 编码
     * @Date 3:36 PM 12/10/2024
     * @Author yl.zhan
     **/
    public static Buffer encode(ProtocolMessage<?> protocolMessage) throws IOException {
        if (protocolMessage == null || protocolMessage.getHeader() == null) {
            return Buffer.buffer();
        }

        ProtocolMessage.Header header = protocolMessage.getHeader();

        Buffer buffer = Buffer.buffer();
        buffer.appendByte(header.getMagic());
        buffer.appendByte(header.getVersion());
        buffer.appendByte(header.getSerializer());
        buffer.appendByte(header.getType());
        buffer.appendByte(header.getStatus());
        buffer.appendLong(header.getRequestId());

        ProtocolMessageSerializerEnum serializerEnum = ProtocolMessageSerializerEnum.getByKey(header.getSerializer());

        if (serializerEnum==null) {
            throw new RuntimeException("未找到对应的序列化器，序列化ID："+header.getSerializer());
        }
        //通过名字获取序列化器
        Serializer serializer = SerializerFactory.getInstance(serializerEnum.getValue());
        // 将要传输的对象转换成byte[]进行传输
        byte[] serialize = serializer.serialize(protocolMessage.getBody());
        // 数据长度
        buffer.appendInt(serialize.length);
        buffer.appendBytes(serialize);
        return buffer;
    }
}
