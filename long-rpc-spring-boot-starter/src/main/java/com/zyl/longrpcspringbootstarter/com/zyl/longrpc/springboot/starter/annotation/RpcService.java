package com.zyl.longrpcspringbootstarter.com.zyl.longrpc.springboot.starter.annotation;

import com.zyl.longrpc.constant.RpcConstant;
import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @program: long-rpc
 * @description: RPC服务注册注解（服务提供方使用）
 * 注解放在实现类上，如果没有写interfaceClass则需要将要注册的服务放在第一个实现
 * @author: yl.zhan
 * @create: 2024-12-18 16:19
 **/
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Component // 使用spring托管bean
public @interface RpcService {
    /**
     * 实现类
     **/
    Class<?> interfaceClass() default void.class;
    /**
     * 服务版本
     **/
    String serviceVersion() default RpcConstant.DEFAULT_SERVICE_VERSION;

}
