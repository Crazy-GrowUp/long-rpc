package com.zyl.longrpc.registry;

import io.etcd.jetcd.ByteSequence;
import io.etcd.jetcd.Client;
import io.etcd.jetcd.KV;
import io.etcd.jetcd.kv.GetResponse;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @program: long-rpc
 * @description: etcd 测试
 * @author: yl.zhan
 * @create: 2024-12-04 15:17
 **/
public class EtcdUseTest {
    public static void main(String[] args){
        // 创建一个etcd 客户端， 使用端点连接
        Client client = Client.builder().endpoints("http://localhost:2379").build();

        // 普通KV存储客户端
        KV kvClient = client.getKVClient();
        ByteSequence key = ByteSequence.from("test_key".getBytes());
        ByteSequence value = ByteSequence.from("test_value".getBytes());
        try {
            // 使用get()，让异步方法变同步
            // put方法是异步执行的，get可以等等它执行完，再往下执行
            kvClient.put(key,value).get();

            CompletableFuture<GetResponse> getFuture = kvClient.get(key);

            GetResponse getResponse = getFuture.get();

            kvClient.delete(key).get();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
