package com.zyl;

import cn.hutool.core.util.IdUtil;
import com.zyl.longrpc.model.RpcRequest;
import com.zyl.longrpc.protocol.*;
import com.zyl.longrpc.serializer.Serializer;
import com.zyl.longrpc.serializer.SerializerFactory;
import io.vertx.core.buffer.Buffer;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.checkerframework.checker.units.qual.C;

import java.io.IOException;

/**
 * Unit test for simple App.
 */
public class AppTest
        extends TestCase {
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(AppTest.class);
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp() {
        assertTrue(true);
    }


    public void testProtocolMessageEnDecode() throws IOException {
        //测试编解码器
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.setServiceName("myService");
        rpcRequest.setMethodName("getName");
        rpcRequest.setArgs(new Object[]{"zzz"});
        rpcRequest.setParameterTypes(new Class[]{String.class});
        rpcRequest.setServiceVersion("1.0");

//        Serializer serializer = SerializerFactory.getInstance(ProtocolMessageSerializerEnum.JDK.getValue());
//        byte[] bytes = serializer.serialize(rpcRequest);

        ProtocolMessage.Header header = new ProtocolMessage.Header();
        header.setMagic(ProtocolConstant.PROTOCOL_MAGIC);
        header.setType((byte) ProtocolMessageTypeEnum.REQUEST.getKey());
        header.setVersion(ProtocolConstant.PROTOCOL_VERSION);
        header.setStatus((byte) ProtocolMessageStatusEnum.OK.getValue());
        header.setSerializer((byte) ProtocolMessageSerializerEnum.JDK.getKey());
        header.setRequestId(IdUtil.getSnowflakeNextId());
        // 这个在编码器里面会自动设置
//        header.setBodyLength(0);

        ProtocolMessage<RpcRequest> rpcRequestProtocolMessage = new ProtocolMessage<>(header, rpcRequest);

        Buffer encode = ProtocolMessageEncoder.encode(rpcRequestProtocolMessage);

        ProtocolMessage<?> decode = ProtocolMessageDecoder.decode(encode);

        RpcRequest rpcRequest1 = (RpcRequest) decode.getBody();
        System.out.println("rpcRequest1:" + rpcRequest1);


    }


}
