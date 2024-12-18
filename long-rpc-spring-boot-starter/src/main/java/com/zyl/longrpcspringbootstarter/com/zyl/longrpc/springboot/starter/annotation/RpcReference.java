package com.zyl.longrpcspringbootstarter.com.zyl.longrpc.springboot.starter.annotation;

import com.zyl.longrpc.constant.RpcConstant;
import com.zyl.longrpc.fault.retry.RetryStrategyKeys;
import com.zyl.longrpc.fault.tolerant.TolerantStrategyKeys;
import com.zyl.longrpc.loadbalancer.LoadBalancerKeys;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Description 远程服务注解（消费端使用）
 * @Date 4:18 PM 12/18/2024
 * @Author yl.zhan
 **/

//@Target({ElementType.FIELD, ElementType.METHOD})
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RpcReference {

    /**
     * 服务接口类
     **/
    Class<?> interfaceClass() default void.class;

    /**
     * 版本
     **/
    String serviceVersion() default RpcConstant.DEFAULT_SERVICE_VERSION;

    /**
     * 负载均衡器
     **/
    String loadBalancer() default LoadBalancerKeys.ROUND_ROBIN;

    /**
     * 重试策略
     **/
    String retryStrategy() default RetryStrategyKeys.NO_RETRY;

    /**
     * 容错策略
     **/
    String tolerantStrategy() default TolerantStrategyKeys.FAIL_FAST;

}
