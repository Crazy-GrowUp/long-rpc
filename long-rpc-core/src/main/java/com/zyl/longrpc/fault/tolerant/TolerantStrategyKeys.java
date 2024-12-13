package com.zyl.longrpc.fault.tolerant;

/**
 * @program: long-rpc
 * @description: 容错服务类型
 * @author: yl.zhan
 * @create: 2024-12-13 15:42
 **/
public interface TolerantStrategyKeys {
    String FAIL_BACK = "failBack";

    String FAIL_FAST = "failFast";

    String FAIL_SAFE = "failSafe";

    String FAIL_OVER = "failOver";

}
