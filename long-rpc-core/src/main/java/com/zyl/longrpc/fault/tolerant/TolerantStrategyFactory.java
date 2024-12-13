package com.zyl.longrpc.fault.tolerant;

import com.zyl.longrpc.spi.SpiLoader;

/**
 * @program: long-rpc
 * @description: 创建容错服务工厂
 * @author: yl.zhan
 * @create: 2024-12-13 15:36
 **/
public class TolerantStrategyFactory {

    static {
        SpiLoader.load(TolerantStrategy.class);
    }


    private static final TolerantStrategy DEFAULT_TOLERANT_STRATEGY = new FailFastTolerantStrategy();

    public static TolerantStrategy getTolerantStrategy(String key) {
        TolerantStrategy instance = SpiLoader.getInstance(TolerantStrategy.class, key);
        if (instance == null) {
            return DEFAULT_TOLERANT_STRATEGY;
        }
        return instance;
    }

}
