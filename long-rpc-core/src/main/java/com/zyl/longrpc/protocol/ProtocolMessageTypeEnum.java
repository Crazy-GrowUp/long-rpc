package com.zyl.longrpc.protocol;

/**
 * @program: long-rpc
 * @description: 协议消息的类型枚举
 * @author: yl.zhan
 * @create: 2024-12-06 17:00
 **/
public enum ProtocolMessageTypeEnum {

    REQUEST(0),
    RESPONSE(1),
    HEART_BEAT(2),
    OTHER(3);


    private final int key;

    ProtocolMessageTypeEnum(int key) {
        this.key = key;
    }

    public int getKey() {
        return this.key;
    }


    /**
     * @param key
     * @return com.zyl.longrpc.protocol.ProtocolMessageTypeEnum
     * @Description 通过key获取枚举
     * @Date 5:05 PM 12/6/2024
     * @Author yl.zhan
     **/
    public static ProtocolMessageTypeEnum getEnumByKey(int key) {
        for (ProtocolMessageTypeEnum anEnum : ProtocolMessageTypeEnum.values()) {
            if (anEnum.key == key) {
                return anEnum;
            }
        }
        return null;
    }
}
