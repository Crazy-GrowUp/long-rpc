package com.zyl.longrpc.loadbalancer;

import com.zyl.longrpc.model.ServiceMetaInfo;

import java.util.List;
import java.util.Map;

/**
 * 负载均衡器（客户端使用，消费端使用）
 *
 * @Date 2:01 PM 12/12/2024
 * @Author yl.zhan
 **/
public interface LoadBalancer {

    /**
     * @param requestParams       请求参数
     * @param serviceMetaInfoList 可用服务列表
     * @return com.zyl.longrpc.model.ServiceMetaInfo
     * @Description 选择服务
     * @Date 2:03 PM 12/12/2024
     * @Author yl.zhan
     **/
    ServiceMetaInfo select(Map<String, Object> requestParams, List<ServiceMetaInfo> serviceMetaInfoList);

}
