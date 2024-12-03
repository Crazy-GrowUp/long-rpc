package com.zyl.longrpc.serializer;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @program: long-rpc
 * @description: hessian2
 * 序列化器 除了支持 Hessian 支持的所有数据类型外，
 * 还增加了对更多复杂数据类型的支持，例如 BigInteger, BigDecimal, Calendar, TimeZone 等
 *  解决了一些hessian序列化java类型的错误
 * @author: yl.zhan
 * @create: 2024-12-03 17:24
 **/
public class Hessian2Serializer implements Serializer{
    @Override
    public <T> byte[] serialize(T object) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Hessian2Output hessian2Output = new Hessian2Output(byteArrayOutputStream);
        hessian2Output.writeObject(object);
        hessian2Output.flush();
        return byteArrayOutputStream.toByteArray();
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> type) throws IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        Hessian2Input hessian2Input = new Hessian2Input(byteArrayInputStream);
        return (T)hessian2Input.readObject(type);
    }
}
