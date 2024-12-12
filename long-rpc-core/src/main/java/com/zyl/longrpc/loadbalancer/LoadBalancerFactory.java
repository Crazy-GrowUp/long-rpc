package com.zyl.longrpc.loadbalancer;

import com.zyl.longrpc.spi.SpiLoader;

/**
 * @program: long-rpc
 * @description: 加载负载均衡器工厂
 * @author: yl.zhan
 * @create: 2024-12-12 14:34
 **/
public class LoadBalancerFactory {

    static {
        SpiLoader.load(LoadBalancer.class);
    }

    private static final LoadBalancer DEFAULT_LOAD_BALANCER = SpiLoader.getInstance(LoadBalancer.class, LoadBalancerKeys.RANDOM);

    public static LoadBalancer getLoadBalancer(String key) {
        return SpiLoader.getInstance(LoadBalancer.class, key);
    }

}
