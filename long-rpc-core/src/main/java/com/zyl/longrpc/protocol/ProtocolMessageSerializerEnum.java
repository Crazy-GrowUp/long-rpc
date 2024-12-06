package com.zyl.longrpc.protocol;

/**
 * @program: long-rpc
 * @description: 协议消息的序列化器枚举
 * @author: yl.zhan
 * @create: 2024-12-06 17:07
 **/
public enum ProtocolMessageSerializerEnum {

    JDK(0, "jdk"),
    JSON(1, "json"),
    KRYO(2, "kryo"),
    HESSIAN(3, "hessian"),
    HESSIAN2(4, "hessian2");


    private final int key;

    private final String value;

    ProtocolMessageSerializerEnum(int key, String value) {
        this.key = key;
        this.value = value;
    }

    public int getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }


    /**
     * @param key
     * @return com.zyl.longrpc.protocol.ProtocolMessageSerializerEnum
     * @Description 根据key获取枚举
     * @Date 5:11 PM 12/6/2024
     * @Author yl.zhan
     **/
    public static ProtocolMessageSerializerEnum getByKey(int key) {
        for (ProtocolMessageSerializerEnum protocolMessageSerializerEnum : ProtocolMessageSerializerEnum.values()) {
            if (protocolMessageSerializerEnum.getKey() == key) {
                return protocolMessageSerializerEnum;
            }
        }
        return null;
    }

    /**
     * @param value
     * @return com.zyl.longrpc.protocol.ProtocolMessageSerializerEnum
     * @Description 根据value获取枚举
     * @Date 5:12 PM 12/6/2024
     * @Author yl.zhan
     **/
    public static ProtocolMessageSerializerEnum getByValue(String value) {
        for (ProtocolMessageSerializerEnum protocolMessageSerializerEnum : ProtocolMessageSerializerEnum.values()) {
            if (protocolMessageSerializerEnum.getValue().equals(value)) {
                return protocolMessageSerializerEnum;
            }
        }
        return null;
    }

}
