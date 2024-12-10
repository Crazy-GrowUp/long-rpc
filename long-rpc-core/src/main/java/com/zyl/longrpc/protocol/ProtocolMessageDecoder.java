package com.zyl.longrpc.protocol;

import com.zyl.longrpc.model.RpcRequest;
import com.zyl.longrpc.model.RpcResponse;
import com.zyl.longrpc.serializer.Serializer;
import com.zyl.longrpc.serializer.SerializerFactory;
import io.vertx.core.buffer.Buffer;

import java.io.IOException;

/**
 * @program: long-rpc
 * @description: 消息解码器
 * @author: yl.zhan
 * @create: 2024-12-10 16:04
 **/
public class ProtocolMessageDecoder {

    /**
     * @param buffer 接收到的数据
     * @return com.zyl.longrpc.protocol.ProtocolMessage<?>
     * @Description 解码
     * @Date 4:05 PM 12/10/2024
     * @Author yl.zhan
     **/
    public static ProtocolMessage<?> decode(Buffer buffer) throws IOException {
        ProtocolMessage.Header header = new ProtocolMessage.Header();

        byte magicNum = buffer.getByte(0);
        // 判断魔数是否正确 （校验位）
        if (ProtocolConstant.PROTOCOL_MAGIC != magicNum) {
            throw new RuntimeException("消息魔数不对，" + magicNum);
        }
        header.setMagic(magicNum);
        header.setVersion(buffer.getByte(1));
        header.setSerializer(buffer.getByte(2));
        header.setType(buffer.getByte(3));
        header.setStatus(buffer.getByte(4));
        header.setRequestId(buffer.getLong(5));
        header.setBodyLength(buffer.getInt(13));

        byte[] bytes = buffer.getBytes(ProtocolConstant.MESSAGE_HEADER_LENGTH,
                ProtocolConstant.MESSAGE_HEADER_LENGTH + header.getBodyLength());

        // 获取序列化器
        ProtocolMessageSerializerEnum serializerEnum = ProtocolMessageSerializerEnum.getByKey(header.getSerializer());
        if (serializerEnum == null) {
            throw new RuntimeException("序列化器不存在，" + header.getSerializer());
        }

        Serializer serializer = SerializerFactory.getInstance(serializerEnum.getValue());
        // 判断类型
        ProtocolMessageTypeEnum messageTypeEnum = ProtocolMessageTypeEnum.getEnumByKey(header.getType());
        if (messageTypeEnum == null) {
            throw new RuntimeException("无法解析此消息类型" + header.getType());
        }


        switch (messageTypeEnum) {
            case REQUEST:
                RpcRequest request = serializer.deserialize(bytes, RpcRequest.class);
                return new ProtocolMessage<>(header, request);
            case RESPONSE:
                RpcResponse response = serializer.deserialize(bytes, RpcResponse.class);
                return new ProtocolMessage<>(header, response);
            case HEART_BEAT:
            case OTHER:
            default:
                throw new RuntimeException("消息暂不支持");

        }
    }

}
