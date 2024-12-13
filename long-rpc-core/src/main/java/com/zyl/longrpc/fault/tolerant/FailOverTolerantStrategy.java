package com.zyl.longrpc.fault.tolerant;

import com.zyl.longrpc.model.RpcRequest;
import com.zyl.longrpc.model.RpcResponse;
import com.zyl.longrpc.model.ServiceMetaInfo;
import com.zyl.longrpc.server.tcp.VertxTcpClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @program: long-rpc
 * @description: 转移到其他的服务
 * @author: yl.zhan
 * @create: 2024-12-13 15:10
 **/
public class FailOverTolerantStrategy implements TolerantStrategy {
    /**
     * @param context
     * @param e
     * @return com.zyl.longrpc.model.RpcResponse
     * @Description 转移到其他的服务
     * @Date 3:19 PM 12/13/2024
     * @Author yl.zhan
     **/
    @Override
    public RpcResponse doTolerant(Map<String, Object> context, Exception e) throws Exception {
        // context里存储request,selectServiceMetaInfoList
        List<ServiceMetaInfo> serviceMetaInfoList = (List<ServiceMetaInfo>) context.getOrDefault("serviceMetaInfoList", new ArrayList<>());
        if (serviceMetaInfoList.isEmpty()) {
            throw new RuntimeException("暂无可用服务", e);
        }
        RpcRequest rpcRequest = (RpcRequest) context.getOrDefault("rpcRequest", null);
        try {
            // 每个服务都试一次
            return VertxTcpClient.doRpcResponse(rpcRequest, serviceMetaInfoList.get(0));
        } catch (Exception err) {
            serviceMetaInfoList.remove(0);
            context.put("serviceMetaInfoList", serviceMetaInfoList);
            return doTolerant(context, e);
        }
    }
}
