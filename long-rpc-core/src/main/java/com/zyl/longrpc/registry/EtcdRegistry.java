package com.zyl.longrpc.registry;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ConcurrentHashSet;
import cn.hutool.cron.CronUtil;
import cn.hutool.cron.task.Task;
import cn.hutool.json.JSONUtil;
import com.zyl.longrpc.config.RegistryConfig;
import com.zyl.longrpc.model.ServiceMetaInfo;
import io.etcd.jetcd.*;
import io.etcd.jetcd.options.GetOption;
import io.etcd.jetcd.options.PutOption;
import io.etcd.jetcd.options.WatchOption;
import io.etcd.jetcd.watch.WatchEvent;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 * @program: long-rpc
 * @description: etcd 测试
 * @author: yl.zhan
 * @create: 2024-12-04 15:56
 **/
public class EtcdRegistry implements Registry {

    private Client client;

    private KV kvClient;

    /**
     * 本机注册的节点 key 集合  (用于维护续期/服务端)
     **/
    private final Set<String> localRegisterNodeKeySet = new ConcurrentHashSet<>();

    /**
     * 服务缓存 （客户端使用）
     **/
    private final RegistryServiceMultiCache registryServiceMultiCache = new RegistryServiceMultiCache();

    /**
     * 正在监听的key的集合  （客户端）
     **/
    private final Set<String> watchingKeySet = new ConcurrentHashSet<>();


    /**
     * 根节点
     **/
    private static final String ETCD_ROOT_PATH = "/rpc/";

    @Override
    public void init(RegistryConfig registryConfig) {
        client = Client.builder().endpoints(registryConfig.getAddress()).connectTimeout(Duration.ofMillis(registryConfig.getTimeout())).build();

        kvClient = client.getKVClient();
        // TODO 最好是判断是否为服务提供者，再来开启心跳
        heartBeat();
    }

    @Override
    public void register(ServiceMetaInfo serviceMetaInfo) throws Exception {
        // 可设置过期时间的客户端  lease
        Lease leaseClient = client.getLeaseClient();

        // 创建一个30秒的租约
        long leaseId = leaseClient.grant(30).get().getID();

        String registerKey = ETCD_ROOT_PATH + serviceMetaInfo.getServiceNodeKey();
        ByteSequence key = ByteSequence.from(registerKey, StandardCharsets.UTF_8);
        ByteSequence value = ByteSequence.from(JSONUtil.toJsonStr(serviceMetaInfo), StandardCharsets.UTF_8);

        PutOption putOption = PutOption.builder().withLeaseId(leaseId).build();
        kvClient.put(key, value, putOption).get();

        // 注册成功后，加入到本地注册key的缓存中
        localRegisterNodeKeySet.add(registerKey);

    }

    @Override
    public void unRegister(ServiceMetaInfo serviceMetaInfo) {
        String registerKey = ETCD_ROOT_PATH + serviceMetaInfo.getServiceNodeKey();
        ByteSequence key = ByteSequence.from(registerKey, StandardCharsets.UTF_8);
        try {
            kvClient.delete(key).get();
            // 从本地缓存中清除
            localRegisterNodeKeySet.remove(registerKey);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<ServiceMetaInfo> serviceDiscovery(String serviceKey) {
        //先查询缓存
        List<ServiceMetaInfo> serviceMetaInfos = registryServiceMultiCache.readCache(serviceKey);
        if (serviceMetaInfos != null) {
            System.out.println("有缓存");
            return serviceMetaInfos;
        }else{
            System.out.println("么有缓存");
        }
        //前缀搜索 结尾一定要加 /
//        String searchPrefix = ETCD_ROOT_PATH + serviceKey + "/";
        String searchPrefix = ETCD_ROOT_PATH + serviceKey;

        //前缀查询
        GetOption getOption = GetOption.builder().isPrefix(true).build();
        try {
            List<KeyValue> keyValues = kvClient.get(ByteSequence.from(searchPrefix, StandardCharsets.UTF_8), getOption).get().getKvs();

            List<ServiceMetaInfo> serviceMetaInfoList = keyValues.stream().map(keyValue -> {
                //  TODO 这样是错误的，因为watch的key和本地缓存的key不一样
//                String key = keyValue.getKey().toString(StandardCharsets.UTF_8);
//                watch(key);
                String value = keyValue.getValue().toString(StandardCharsets.UTF_8);
                return JSONUtil.toBean(value, ServiceMetaInfo.class);
            }).collect(Collectors.toList());

            //存储到缓存中
            registryServiceMultiCache.writeCache(serviceKey, serviceMetaInfoList);
            //监听
            watch(serviceKey);

            return serviceMetaInfoList;
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("获取服务列表失败", e);
        }
    }

    @Override
    public void destroy() {
        System.out.println("当前节点下线");

        // 下线前把所有的key值删除
        for (String key : localRegisterNodeKeySet) {
            try {
                kvClient.delete(ByteSequence.from(key, StandardCharsets.UTF_8)).get();
            } catch (Exception e) {
                System.out.println(key + " 节点下线失败，" + e);
            }
        }


        if (kvClient != null) {
            kvClient.close();
        }

        if (client != null) {
            client.close();
        }

    }

    @Override
    public void heartBeat() {
        // 十秒钟执行一次
        CronUtil.schedule("*/10 * * * * * *", new Task() {
            @Override
            public void execute() {
                // 循环本地缓存中的key值
                for (String key : localRegisterNodeKeySet) {
                    try {
                        List<KeyValue> kvs = kvClient.get(ByteSequence.from(key, StandardCharsets.UTF_8))
                                .get()
                                .getKvs();
                        // 此节点已经过期 (需要重启节点才能注册)
                        if (CollUtil.isEmpty(kvs)) {
                            // 过期了就删除本地缓存，等它重新注册加入到缓存中
                            localRegisterNodeKeySet.remove(key);
                            continue;
                        }

                        // 没过期，就续签（续约）
                        KeyValue keyValue = kvs.get(0);
                        String value = keyValue.getValue().toString(StandardCharsets.UTF_8);
                        ServiceMetaInfo serviceMetaInfo = JSONUtil.toBean(value, ServiceMetaInfo.class);
                        register(serviceMetaInfo);

                    } catch (Exception e) {
                        throw new RuntimeException(key + " 续签失败 ", e);
                    }
                }
            }
        });
        // 支持秒级别定时任务
        CronUtil.setMatchSecond(true);
        CronUtil.start();
    }

    @Override
    public void watch(String serviceKey) {
        Watch watchClient = client.getWatchClient();
        boolean add = watchingKeySet.add(serviceKey);
        if (add) {
            // 添加成功说明之前没监听过，要监听
            String searchPrefix = ETCD_ROOT_PATH + serviceKey;
            //前缀查询
            WatchOption watchOption = WatchOption.builder().isPrefix(true).build();
            watchClient.watch(ByteSequence.from(searchPrefix, StandardCharsets.UTF_8), watchOption, watchResponse -> {
                for (WatchEvent event : watchResponse.getEvents()) {
                    switch (event.getEventType()) {
                        case DELETE:
                            registryServiceMultiCache.clearCache(serviceKey);
                            break;
                        case PUT:
                        default:
                            break;
                    }
                }
            });
        }
    }
}
