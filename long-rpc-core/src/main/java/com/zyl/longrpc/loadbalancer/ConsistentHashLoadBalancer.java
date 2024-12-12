package com.zyl.longrpc.loadbalancer;

import com.zyl.longrpc.model.ServiceMetaInfo;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @program: long-rpc
 * @description: 一致性哈希负载均衡器
 * @author: yl.zhan
 * @create: 2024-12-12 14:16
 **/
public class ConsistentHashLoadBalancer implements LoadBalancer {

    /**
     * 存储哈希值和对应的服务信息
     **/
    private final TreeMap<Integer, ServiceMetaInfo> virtualNodes = new TreeMap<>();

    /**
     * 虚拟节点个数
     **/
    private static final int VIRTUAL_NODE_SIZE = 100;


    @Override
    public ServiceMetaInfo select(Map<String, Object> requestParams, List<ServiceMetaInfo> serviceMetaInfoList) {
        if (serviceMetaInfoList.isEmpty()) {
            return null;
        }
        if (serviceMetaInfoList.size() == 1) {
            return serviceMetaInfoList.get(0);
        }

        // 建立虚拟节点
        for (ServiceMetaInfo serviceMetaInfo : serviceMetaInfoList) {
            for (int i = 0; i < VIRTUAL_NODE_SIZE; i++) {
                String key = serviceMetaInfo.getServiceAddress() + "#" + i;
                int hash = hash(key);
                virtualNodes.put(hash, serviceMetaInfo);
            }
        }


        int requestHash = hash(requestParams);

        // 找对最接近的hash值的serviceMetaInfo
        Map.Entry<Integer, ServiceMetaInfo> integerServiceMetaInfoEntry = virtualNodes.ceilingEntry(requestHash);
        // 没找到，那就返回第一个
        if (integerServiceMetaInfoEntry == null) {
            return virtualNodes.firstEntry().getValue();
        }

        return integerServiceMetaInfoEntry.getValue();

    }

    public int hash(Object key) {
        return key.hashCode();
    }


}
