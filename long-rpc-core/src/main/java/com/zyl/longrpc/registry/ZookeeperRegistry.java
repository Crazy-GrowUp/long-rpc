package com.zyl.longrpc.registry;

import cn.hutool.core.collection.ConcurrentHashSet;
import com.zyl.longrpc.config.RegistryConfig;
import com.zyl.longrpc.model.ServiceMetaInfo;
import io.vertx.core.json.Json;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.CuratorCache;
import org.apache.curator.framework.recipes.cache.CuratorCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.details.JsonInstanceSerializer;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 * @program: long-rpc
 * @description: zookeeper服务中心注册
 * @author: yl.zhan
 * @create: 2024-12-05 17:13
 **/
public class ZookeeperRegistry implements Registry {

    private CuratorFramework client;

    private ServiceDiscovery<ServiceMetaInfo> serviceDiscovery;


    /**
     * 注册服务缓存（服务端用，用于注册，注销，心跳）
     **/
    private final Set<String> localRegisterNodeKeySet = new ConcurrentHashSet<>();

    /**
     * 服务注册列表（客户端用，用于查找，监听）
     **/
    private final RegistryServiceMultiCache registryServiceMultiCache = new RegistryServiceMultiCache();

    /**
     * 正在监听的key  (客户端用，用于监听)
     **/
    private final Set<String> watchingKeySet = new ConcurrentHashSet<>();


    private static final String ZK_ROOT_PATH = "/rpc/zk";


    @Override
    public void init(RegistryConfig registryConfig) {
        //连接zookeeper
        client = CuratorFrameworkFactory
                .builder()
                //注册中地址
                .connectString(registryConfig.getAddress())
                .retryPolicy(new ExponentialBackoffRetry(Math.toIntExact(registryConfig.getTimeout()), 3))
                .build();

        serviceDiscovery = ServiceDiscoveryBuilder
                .builder(ServiceMetaInfo.class)
                .client(client)
                .basePath(ZK_ROOT_PATH)
                .serializer(new JsonInstanceSerializer<>(ServiceMetaInfo.class))
                .build();
        try {
            client.start();
            serviceDiscovery.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }

    @Override
    public void register(ServiceMetaInfo serviceMetaInfo) throws Exception {
        //注册
        serviceDiscovery.registerService(buildServiceInstance(serviceMetaInfo));
        String registerKey = ZK_ROOT_PATH + "/" + serviceMetaInfo.getServiceNodeKey();
        localRegisterNodeKeySet.add(registerKey);
    }

    @Override
    public void unRegister(ServiceMetaInfo serviceMetaInfo) {
        try {
            serviceDiscovery.unregisterService(buildServiceInstance(serviceMetaInfo));

            String registerKey = ZK_ROOT_PATH + "/" + serviceMetaInfo.getServiceNodeKey();
            localRegisterNodeKeySet.remove(registerKey);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public List<ServiceMetaInfo> serviceDiscovery(String serviceKey) {

        List<ServiceMetaInfo> cacheServiceMetaInfos = registryServiceMultiCache.readCache(serviceKey);
        if (cacheServiceMetaInfos != null) {
            return cacheServiceMetaInfos;
        }


        try {
            Collection<ServiceInstance<ServiceMetaInfo>> serviceInstances
                    = serviceDiscovery.queryForInstances(serviceKey);

            List<ServiceMetaInfo> serviceMetaInfoList = serviceInstances.stream()
                    .map(ServiceInstance::getPayload)
                    .collect(Collectors.toList());

            registryServiceMultiCache.writeCache(serviceKey, serviceMetaInfoList);
            return serviceMetaInfoList;
        } catch (Exception e) {
            throw new RuntimeException("获取服务列表失败", e);
        }
    }

    @Override
    public void destroy() throws ExecutionException, InterruptedException {
        System.out.println("当前节点下线");
        for (String key : localRegisterNodeKeySet) {
            try {
                client.delete().guaranteed().forPath(key);
            } catch (Exception e) {
                throw new RuntimeException(key + " 节点下线失败", e);
            }
        }
        localRegisterNodeKeySet.clear();
        if (client != null) {
            client.close();
        }
    }

    @Override
    public void heartBeat() {
        //不需要心跳机制，建立了临时节点，如果服务器故障，则临时节点直接丢失
    }

    @Override
    public void watch(String serviceKey) {
        String watchKey = ZK_ROOT_PATH + "/" + serviceKey;
        boolean add = watchingKeySet.add(watchKey);
        if (add) {
            CuratorCache curatorCache = CuratorCache.build(client, watchKey);
            curatorCache.start();

            curatorCache.listenable().addListener(
                    CuratorCacheListener.builder()
                            .forDeletes(childData -> registryServiceMultiCache.clearCache(serviceKey))
                            .forChanges((oldNode, node) -> registryServiceMultiCache.clearCache(serviceKey))
                            .build()
            );
        }
    }


    private ServiceInstance<ServiceMetaInfo> buildServiceInstance(ServiceMetaInfo serviceMetaInfo) {
//        String serviceAddress = serviceMetaInfo.getServiceAddress();
        String serviceAddress = serviceMetaInfo.getServiceHost() + ":" + serviceMetaInfo.getServicePort();

        try {
            return ServiceInstance.<ServiceMetaInfo>builder()
                    .id(serviceAddress)
                    .name(serviceMetaInfo.getServiceKey())
                    .address(serviceAddress)
                    .payload(serviceMetaInfo)
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
