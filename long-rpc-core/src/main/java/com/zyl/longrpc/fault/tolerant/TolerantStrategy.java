package com.zyl.longrpc.fault.tolerant;

import com.zyl.longrpc.model.RpcResponse;

import java.util.Map;

/**
 * @program: long-rpc
 * @description: 容错策略
 * @author: yl.zhan
 * @create: 2024-12-13 14:38
 **/
public interface TolerantStrategy {


    /**
     * @param context  上下文，用于传递数据
     * @param e  异常
     * @return com.zyl.longrpc.model.RpcResponse
     * @Description //TODO
     * @Date 2:39 PM 12/13/2024
     * @Author yl.zhan
     **/
    RpcResponse doTolerant(Map<String, Object> context, Exception e) throws Exception;

}
