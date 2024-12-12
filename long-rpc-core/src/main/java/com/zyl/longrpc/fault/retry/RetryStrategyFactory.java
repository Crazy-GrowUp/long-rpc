package com.zyl.longrpc.fault.retry;

import com.zyl.longrpc.spi.SpiLoader;

/**
 * @program: long-rpc
 * @description: 获得重试对象工厂
 * @author: yl.zhan
 * @create: 2024-12-12 16:49
 **/
public class RetryStrategyFactory {

    static {
        SpiLoader.load(RetryStrategy.class);
    }

    public static RetryStrategy getRetryStrategy(String key) {
        return SpiLoader.getInstance(RetryStrategy.class, key);
    }

}
