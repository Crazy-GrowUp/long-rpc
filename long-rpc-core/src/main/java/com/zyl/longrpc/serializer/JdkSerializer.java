package com.zyl.longrpc.serializer;

import java.io.*;

/**
 * @program: long-rpc
 * @description: JDK 序列化器
 * @author: yl.zhan
 * @create: 2024-11-25 16:18
 **/
public class JdkSerializer implements Serializer{
    /*
     * @Description 序列化
     * @Date 4:19 PM 11/25/2024
     * @Author yl.zhan
     * @param object
     * @return byte[]
     **/
    @Override
    public <T> byte[] serialize(T object) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        objectOutputStream.writeObject(object);
        objectOutputStream.flush();
        objectOutputStream.close();
        return outputStream.toByteArray();
    }

    /*
     * @Description 反序列化
     * @Date 4:19 PM 11/25/2024
     * @Author yl.zhan
     * @param bytes
     * @param type
     * @return T
     **/
    @Override
    public <T> T deserialize(byte[] bytes, Class<T> type) throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);

        try {
          return (T) objectInputStream.readObject();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }finally {
            objectInputStream.close();
        }
    }
}
