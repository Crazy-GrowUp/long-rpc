package com.zyl.longrpc.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: long-rpc
 * @description: 服务信息类
 * @author: yl.zhan
 * @create: 2024-12-13 17:30
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceRegisterInfo<T> {
    /**
     * 服务名称
     **/
    private String serviceName;

    /**
     * 实现类
     **/
    private Class<? extends T> implClass;

}
