package com.zyl.longrpc.registry;

import com.zyl.longrpc.config.RegistryConfig;
import com.zyl.longrpc.model.ServiceMetaInfo;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * @program: long-rpc
 * @description: 注册中心
 * @author: yl.zhan
 * @create: 2024-12-04 15:48
 **/
public interface Registry {

    /**
     * @Description 初始化
     * @Date 3:50 PM 12/4/2024
     * @Author yl.zhan
     * @param registryConfig  注册中心配置
     **/
    void init(RegistryConfig registryConfig);

    /**
     * @Description 注册服务（服务端使用）
     * @Date 3:50 PM 12/4/2024
     * @Author yl.zhan
     * @param serviceMetaInfo 注册的服务器信息
     **/
    void register(ServiceMetaInfo serviceMetaInfo)throws Exception;


    /**
     * @Description 注销服务（服务端使用）
     * @Date 3:52 PM 12/4/2024
     * @Author yl.zhan
     * @param serviceMetaInfo 注销的服务器信息
     **/
    void unRegister(ServiceMetaInfo serviceMetaInfo);


    /**
     * @Description 服务发现（获取某个服务的所有节点，消费端使用）
     * @Date 3:54 PM 12/4/2024
     * @Author yl.zhan
     * @param serviceKey
     * @return java.util.List<com.zyl.longrpc.model.ServiceMetaInfo>
     **/
    List<ServiceMetaInfo> serviceDiscovery(String serviceKey);


    /**
     * @Description 服务销毁
     * @Date 3:55 PM 12/4/2024
     * @Author yl.zhan
     **/
    void destroy() throws ExecutionException, InterruptedException;


    /**
     * @Description 心跳检测（服务端使用）
     * @Date 2:32 PM 12/5/2024
     * @Author yl.zhan
     **/
    void heartBeat();

    /**
     * @Description 监听注册的服务 （消费者/客户端使用）
     * @Date 3:43 PM 12/5/2024
     * @Author yl.zhan
     * @Param
     * @return
     **/
    void watch(String serviceKey);

}
