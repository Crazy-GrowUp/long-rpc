package com.zyl.longrpcspringbootstarter.com.zyl.longrpc.springboot.starter.annotation;

import com.zyl.longrpcspringbootstarter.com.zyl.longrpc.springboot.starter.bootstrap.RpcConsumerBootstrap;
import com.zyl.longrpcspringbootstarter.com.zyl.longrpc.springboot.starter.bootstrap.RpcInitBootstrap;
import com.zyl.longrpcspringbootstarter.com.zyl.longrpc.springboot.starter.bootstrap.RpcProviderBootstrap;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @program: long-rpc
 * @description: 启动RPC注解
 * @author: yl.zhan
 * @create: 2024-12-18 16:17
 **/
@Target({ElementType.TYPE}) //类上使用
@Retention(RetentionPolicy.RUNTIME) //运行时也保留
// 在使用此注解时，同时开启下面类中的监听
@Import({RpcInitBootstrap.class, RpcProviderBootstrap.class, RpcConsumerBootstrap.class})
public @interface EnableRpc {
    /**
     * @return boolean
     * @Description 是否启动服务（提供方开启，消费方关闭)
     * @Date 4:23 PM 12/18/2024
     * @Author yl.zhan
     **/
    boolean needServer() default true;
}
