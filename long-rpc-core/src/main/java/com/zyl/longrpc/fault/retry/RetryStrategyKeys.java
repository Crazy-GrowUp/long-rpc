package com.zyl.longrpc.fault.retry;

/**
 * @program: long-rpc
 * @description: 重试策略选项
 * @author: yl.zhan
 * @create: 2024-12-12 16:50
 **/
public interface RetryStrategyKeys {

    String NO_RETRY = "no";

    String FIXED_INTERVAL = "fixedInterval";

}
