package com.zyl.longrpc.protocol;

import lombok.Getter;

/**
 * @program: long-rpc
 * @description: 协议消息的状态枚举
 * @author: yl.zhan
 * @create: 2024-12-06 16:49
 **/
public enum ProtocolMessageStatusEnum {

    OK("ok", 20),
    BAD_REQUEST("ok", 40),
    BAD_RESPONSE("ok", 50);

    private final String text;

    private final int value;

    ProtocolMessageStatusEnum(String text, int value) {
        this.text = text;
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

    public String getText() {
        return this.text;
    }

    /**
     * @param value
     * @return com.zyl.longrpc.protocol.ProtocolMessageStatusEnum
     * @Description 通过value获取枚举
     * @Date 4:55 PM 12/6/2024
     * @Author yl.zhan
     **/
    public ProtocolMessageStatusEnum getStatusEnumByValue(int value) {
        for (ProtocolMessageStatusEnum protocolMessageStatusEnum : ProtocolMessageStatusEnum.values()) {
            if (protocolMessageStatusEnum.value == value) {
                return protocolMessageStatusEnum;
            }
        }
        return null;
    }

}
