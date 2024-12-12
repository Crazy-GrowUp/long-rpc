package com.zyl.longrpc.fault.retry;

import com.zyl.longrpc.model.RpcResponse;

import java.util.concurrent.Callable;

/**
 * @program: long-rpc
 * @description: 重试策略
 * @author: yl.zhan
 * @create: 2024-12-12 16:37
 **/
public interface RetryStrategy {


    /**
     * @param callable 执行的方法
     * @return com.zyl.longrpc.model.RpcResponse
     * @Description 重试接口
     * @Date 4:38 PM 12/12/2024
     * @Author yl.zhan
     **/
    RpcResponse doRetry(Callable<RpcResponse> callable) throws Exception;

}
