package com.zyl.longrpc.registry;

import com.zyl.longrpc.model.ServiceMetaInfo;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @program: long-rpc
 * @description: 注册中心服务本地缓存（支持多个服务，主要是为客户端使用）
 * @author: yl.zhan
 * @create: 2024-12-05 15:26
 **/
public class RegistryServiceMultiCache {


    /**
     * 服务缓存
     **/
    Map<String, List<ServiceMetaInfo>> serviceCache = new ConcurrentHashMap<>();



    /**
     * @Description 写缓存
     * @Date 3:31 PM 12/5/2024
     * @Author yl.zhan
     * @param serviceKey 缓存键名
     * @param newServiceCache  缓存列表
     **/
    void writeCache(String serviceKey, List<ServiceMetaInfo> newServiceCache){
        this.serviceCache.put(serviceKey,newServiceCache);
    }

    /**
     * @Description 读取缓存
     * @Date 3:34 PM 12/5/2024
     * @Author yl.zhan
     * @param serviceKey 缓存键名
     * @return java.util.List<com.zyl.longrpc.model.ServiceMetaInfo>
     **/
    List<ServiceMetaInfo> readCache(String serviceKey){
        return this.serviceCache.get(serviceKey);
    }

    /**
     * @Description 根据键名删除缓存
     * @Date 3:35 PM 12/5/2024
     * @Author yl.zhan
     * @param serviceKey 缓存键名
     **/
    void clearCache(String serviceKey){
        this.serviceCache.remove(serviceKey);
    }
    /**
     * @Description 清除全部缓存
     * @Date 3:35 PM 12/5/2024
     * @Author yl.zhan
     **/
    void clearAllCache(){
        this.serviceCache.clear();
    }


}
