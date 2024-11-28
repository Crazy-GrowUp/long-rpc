package com.zyl.longrpc.registry;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @program: long-rpc
 * @description: 本地方法注册
 * @author: yl.zhan
 * @create: 2024-11-25 16:04
 **/
public class LocalRegistry {

    /*
     * 注册信息存储
     **/
    private static final Map<String,Class<?>> map = new ConcurrentHashMap<>();


    /*
     * @Description  注册服务
     * @Date 4:08 PM 11/25/2024
     * @Author yl.zhan
     * @param serviceName 服务类名
     * @param implClass  服务类
     **/
    public static void register(String serviceName,Class<?> implClass){
        map.put(serviceName,implClass);
    }


    /*
     * @Description 获取服务
     * @Date 4:09 PM 11/25/2024
     * @Author yl.zhan
     * @param serviceName 服务名
     * @return
     * @return java.lang.Class<?>
     **/
    public static Class<?> get(String serviceName){
        return map.get(serviceName);
    }

    /*
     * @Description  删除服务
     * @Date 4:10 PM 11/25/2024
     * @Author yl.zhan
     * @param serviceName
     * @return
     **/
    public static void remove(String serviceName){
        map.remove(serviceName);
    }


}
