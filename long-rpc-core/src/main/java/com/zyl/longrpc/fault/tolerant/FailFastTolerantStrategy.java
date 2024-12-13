package com.zyl.longrpc.fault.tolerant;

import com.zyl.longrpc.model.RpcResponse;

import java.util.Map;

/**
 * @program: long-rpc
 * @description: 快速失败 - 容错策略（立即通知外层调用方）
 * @author: yl.zhan
 * @create: 2024-12-13 14:49
 **/
public class FailFastTolerantStrategy implements TolerantStrategy{
    @Override
    public RpcResponse doTolerant(Map<String, Object> context, Exception e) throws Exception {
        throw new Exception("服务调用出错",e);
    }
}
