package com.zyl.longrpc.serializer;

import java.io.IOException;

/*
 * 序列化器接口
 **/
public interface Serializer {

    /*
     * @Description 序列化
     * @Date 4:16 PM 11/25/2024
     * @Author yl.zhan
     * @param object
     * @return byte[]
     **/
    <T> byte[] serialize(T object) throws IOException;


    /*
     * @Description 反序列化
     * @Date 4:17 PM 11/25/2024
     * @Author yl.zhan
     * @param bytes
     * @param type
     * @return T
     **/
    <T> T deserialize(byte[] bytes, Class<T> type) throws IOException;


}
