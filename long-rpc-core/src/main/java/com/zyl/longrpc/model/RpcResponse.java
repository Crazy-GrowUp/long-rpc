package com.zyl.longrpc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @program: long-rpc
 * @description: PRC 响应类
 * @author: yl.zhan
 * @create: 2024-11-26 15:01
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RpcResponse implements Serializable {

    /**
     * 响应数据
     **/
    private Object data;


    /**
     * 响应数据类型
     **/
    private Class<?> dataType;

    /**
     * 响应信息
     **/
    private String message;


    /**
     * 异常信息
     **/
    private Exception exception;

}
