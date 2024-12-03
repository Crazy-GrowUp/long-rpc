package com.zyl.longrpc.serializer;

import com.zyl.longrpc.spi.SpiLoader;

import java.util.HashMap;
import java.util.Map;

/**
 * @program: long-rpc
 * @description: 序列化工厂（获取序列化器对象）
 * @author: yl.zhan
 * @create: 2024-12-03 11:12
 **/
public class SerializerFactory {
    // =========================手动加载======================
//    /**
//     * 序列化器映射 （单例）
//     **/
//    private static final Map<String, Serializer> KEY_SERIALIZER_MAP = new HashMap<String, Serializer>() {{
//        put(SerializerKeys.JDK, new JdkSerializer());
//        put(SerializerKeys.JSON, new JsonSerializer());
//        put(SerializerKeys.KRYO, new KryoSerializer());
//        put(SerializerKeys.HESSIAN, new HessianSerializer());
//    }};
//
//    /**
//     * 默认序列化器 JDK
//     **/
//    private static final Serializer DEFAULT_SERIALIZER = KEY_SERIALIZER_MAP.get(SerializerKeys.JDK);
//
//
//    public static Serializer getInstance(String key){
//        // 如果KEY不存在，就返回 DEFAULT_SERIALIZER
//        return KEY_SERIALIZER_MAP.getOrDefault(key, DEFAULT_SERIALIZER);
//    }
    //=======================手动加载 END====================================

    //======================读取META-INF加载 SPI ==============================
    static {
        SpiLoader.load(Serializer.class);
    }

    private static final Serializer DEFAULT_SERIALIZER = new JdkSerializer();

    public static final Serializer getInstance(String key) {
        return SpiLoader.getInstance(Serializer.class, key);
    }

    //======================读取META-INF加载 SPI END==============================


}
