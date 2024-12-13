package com.zyl.longrpc.fault.tolerant;

import com.zyl.longrpc.model.RpcResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * @program: long-rpc
 * @description: 静默处理异常
 * @author: yl.zhan
 * @create: 2024-12-13 15:04
 **/
@Slf4j
public class FailSafeTolerantStrategy implements TolerantStrategy{
    @Override
    public RpcResponse doTolerant(Map<String, Object> context, Exception e) throws Exception {
        log.error("服务调用出错", e);
        return new RpcResponse();
    }
}
