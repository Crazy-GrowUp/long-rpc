package com.zyl.longrpc.loadbalancer;

import com.zyl.longrpc.model.ServiceMetaInfo;

import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * @program: long-rpc
 * @description: 随机负载均衡器
 * @author: yl.zhan
 * @create: 2024-12-12 14:12
 **/
public class RandomLoadBalancer implements LoadBalancer {
    private final Random random = new Random();

    @Override
    public ServiceMetaInfo select(Map<String, Object> requestParams, List<ServiceMetaInfo> serviceMetaInfoList) {
        int size = serviceMetaInfoList.size();
        if (size == 0) {
            return null;
        }
        if (size == 1) {
            return serviceMetaInfoList.get(0);
        }

        return serviceMetaInfoList.get(random.nextInt(size));
    }
}
