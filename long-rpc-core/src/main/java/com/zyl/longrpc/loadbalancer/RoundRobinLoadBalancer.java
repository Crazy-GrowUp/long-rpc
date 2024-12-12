package com.zyl.longrpc.loadbalancer;

import com.zyl.longrpc.model.ServiceMetaInfo;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @program: long-rpc
 * @description: 轮询负载均衡器
 * @author: yl.zhan
 * @create: 2024-12-12 14:05
 **/
public class RoundRobinLoadBalancer implements LoadBalancer {

    private static final AtomicInteger count = new AtomicInteger(0);

    @Override
    public ServiceMetaInfo select(Map<String, Object> requestParams, List<ServiceMetaInfo> serviceMetaInfoList) {
        if (serviceMetaInfoList.isEmpty()) {
            return null;
        }
        int size = serviceMetaInfoList.size();
        if (size == 1) {
            return serviceMetaInfoList.get(0);
        }

        int index = count.getAndIncrement() % size;
        return serviceMetaInfoList.get(index);
    }
}
