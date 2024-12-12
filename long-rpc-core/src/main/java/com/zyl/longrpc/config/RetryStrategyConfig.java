package com.zyl.longrpc.config;

import com.zyl.longrpc.fault.retry.RetryStrategyKeys;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: long-rpc
 * @description: 重试策略配置
 * @author: yl.zhan
 * @create: 2024-12-12 16:55
 **/
@NoArgsConstructor
@AllArgsConstructor
@Data
public class RetryStrategyConfig {
    private String strategy = RetryStrategyKeys.FIXED_INTERVAL;

    // 重试时间间隔
    private long retryTimeInterval = 1000L;

    // 重试次数
    private int retryCount = 3;


}
