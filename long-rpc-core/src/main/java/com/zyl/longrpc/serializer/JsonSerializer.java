package com.zyl.longrpc.serializer;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.zyl.longrpc.model.RpcRequest;
import com.zyl.longrpc.model.RpcResponse;

import java.io.IOException;

/**
 * @program: long-rpc
 * @description: JSON序列化
 * @author: yl.zhan
 * @create: 2024-12-02 17:29
 **/
public class JsonSerializer implements Serializer {
    // 使用jackson 序列化
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();


    @Override
    public <T> byte[] serialize(T object) throws IOException {
        return OBJECT_MAPPER.writeValueAsBytes(object);
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> type) throws IOException {
        T obj = OBJECT_MAPPER.readValue(bytes, type);
        if (obj instanceof RpcRequest) {
            return handleRequest((RpcRequest) obj, type);
        }
        if (obj instanceof RpcResponse) {
            return handleResponse((RpcResponse) obj, type);
        }
        return obj;
    }


    /**
     * @param rpcRequest RPC请求
     * @param type       类型
     * @return T
     * @throws IOException IO异常
     * @Description 处理对象是 RpcRequest 的 json 数据
     * 由于 object 的原始对象会被擦除，导致反序列化时会被作为 LinkedHashMap 无法转换成原始对象，因此这里做了特殊处理
     * @Date 5:35 PM 12/2/2024
     * @Author yl.zhan
     **/
    private <T> T handleRequest(RpcRequest rpcRequest, Class<T> type) throws IOException {
        Class<?>[] parameterTypes = rpcRequest.getParameterTypes();
        Object[] args = rpcRequest.getArgs();

        //循环处理每个参数的类型
        for (int i = 0; i < parameterTypes.length; i++) {
            Class<?> clazz = parameterTypes[i];
            // 如果类型不同，则需要处理一下
            if (!clazz.isAssignableFrom(args[i].getClass())) {
                // 用户传递的参数和参数类型不同，需要把参数转换类型
                byte[] argBytes = OBJECT_MAPPER.writeValueAsBytes(args[i]);
                args[i] = OBJECT_MAPPER.readValue(argBytes, clazz);
            }
        }
        return type.cast(rpcRequest);
    }


    /**
     * @param rpcResponse
     * @param type
     * @return
     * @return T
     * @Description //TODO
     * @Date 5:48 PM 12/2/2024
     * @Author yl.zhan
     * @Param
     **/
    private <T> T handleResponse(RpcResponse rpcResponse, Class<T> type) throws IOException {
        byte[] dataBytes = OBJECT_MAPPER.writeValueAsBytes(rpcResponse.getData());
        rpcResponse.setData(OBJECT_MAPPER.readValue(dataBytes, rpcResponse.getDataType()));
        return type.cast(rpcResponse);
    }


}
