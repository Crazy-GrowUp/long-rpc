package com.zyl.longrpc.fault.tolerant;

import com.zyl.longrpc.model.RpcRequest;
import com.zyl.longrpc.model.RpcResponse;
import com.zyl.longrpc.proxy.ServiceProxyFactory;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * @program: long-rpc
 * @description: 降级到其他服务调用
 * @author: yl.zhan
 * @create: 2024-12-13 15:21
 **/
@Slf4j
public class FailBackTolerantStrategy implements TolerantStrategy {
    @Override
    public RpcResponse doTolerant(Map<String, Object> context, Exception e) throws Exception {
        // 通过context里的信息 获取降级接口
        RpcRequest rpcRequest = (RpcRequest) context.getOrDefault("rpcRequest", null);
        if (rpcRequest == null) {
            log.info("FailBackTolerantStrategy", e);
            throw new Exception(e.getMessage());
        }
        String serviceName = rpcRequest.getServiceName();
        Class<?> serviceClass = Class.forName(serviceName);
        //使用模拟接口
        Object mockProxy = ServiceProxyFactory.getMockProxy(serviceClass);
        Method method = mockProxy.getClass().getMethod(rpcRequest.getMethodName(), rpcRequest.getParameterTypes());
        Object result = method.invoke(mockProxy, rpcRequest.getArgs());
        // 返回结果
        RpcResponse rpcResponse = new RpcResponse();
        rpcResponse.setData(result);
        rpcResponse.setDataType(method.getReturnType());
        rpcResponse.setMessage("Fail Back Tolerant Strategy");
        return rpcResponse;
    }
}
