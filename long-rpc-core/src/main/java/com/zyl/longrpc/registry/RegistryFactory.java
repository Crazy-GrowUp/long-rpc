package com.zyl.longrpc.registry;

import com.zyl.longrpc.spi.SpiLoader;

/**
 * @program: long-rpc
 * @description: 注册中心工厂（用于获取注册中心对象）
 * @author: yl.zhan
 * @create: 2024-12-04 16:25
 **/
public class RegistryFactory {

    static{
        SpiLoader.load(Registry.class);
    }

    /**
     * 默认注册中心
     **/
    private static final Registry DEFAULT_REGISTRY = new EtcdRegistry();

    public static Registry getInstance(String key){
        return SpiLoader.getInstance(Registry.class,key);
    }


}
