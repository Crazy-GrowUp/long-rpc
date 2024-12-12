package com.zyl.longrpc.loadbalancer;

/**
 * @program: long-rpc
 * @description: 支持的负载均衡器的值
 * @author: yl.zhan
 * @create: 2024-12-12 14:29
 **/
public interface LoadBalancerKeys {

    // 一致性哈希
    String CONSISTENT_HASH = "consistentHash";
    // 轮询
    String ROUND_ROBIN = "roundRobin";
    // 随机
    String RANDOM = "random";

}
