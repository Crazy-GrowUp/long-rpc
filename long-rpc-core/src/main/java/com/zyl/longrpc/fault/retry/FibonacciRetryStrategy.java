package com.zyl.longrpc.fault.retry;

import com.github.rholder.retry.*;
import com.zyl.longrpc.RpcApplication;
import com.zyl.longrpc.config.RetryStrategyConfig;
import com.zyl.longrpc.model.RpcResponse;

import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * @program: long-rpc
 * @description: 斐波那契间隔时间重试
 * @author: yl.zhan
 * @create: 2024-12-12 17:18
 **/
public class FibonacciRetryStrategy implements RetryStrategy {
    @Override
    public RpcResponse doRetry(Callable<RpcResponse> callable) throws Exception {
        RetryStrategyConfig retryStrategyConfig = RpcApplication.getRpcConfig().getRetryStrategyConfig();
        //使用google的
        Retryer<RpcResponse> builder = RetryerBuilder.<RpcResponse>newBuilder()
                .retryIfExceptionOfType(Exception.class) //重试条件，报错
                .withWaitStrategy(WaitStrategies.fibonacciWait(retryStrategyConfig.getRetryTimeInterval(), TimeUnit.MILLISECONDS)) //重试间隔时间
                .withStopStrategy(StopStrategies.stopAfterAttempt(retryStrategyConfig.getRetryCount())) // 重试次数
                .withRetryListener(new RetryListener() {
                    @Override
                    public <V> void onRetry(Attempt<V> attempt) {
                        if(attempt.hasException()){
                            Date date = new Date();
                            System.out.printf("%s:重试了 %s 次\n",date.toLocaleString(),attempt.getAttemptNumber());
                        }
                    }
                })
                .build();
        return builder.call(callable);
    }
}
