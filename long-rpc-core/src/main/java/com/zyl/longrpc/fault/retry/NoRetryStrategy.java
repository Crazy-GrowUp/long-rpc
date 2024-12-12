package com.zyl.longrpc.fault.retry;

import com.zyl.longrpc.model.RpcResponse;

import java.util.concurrent.Callable;

/**
 * @program: long-rpc
 * @description: 不重试-重试策略
 * @author: yl.zhan
 * @create: 2024-12-12 16:39
 **/
public class NoRetryStrategy implements RetryStrategy {

    @Override
    public RpcResponse doRetry(Callable<RpcResponse> callable) throws Exception {
        return callable.call();
    }
}
