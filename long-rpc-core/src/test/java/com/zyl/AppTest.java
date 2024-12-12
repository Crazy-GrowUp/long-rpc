package com.zyl;

import cn.hutool.core.util.IdUtil;
import com.zyl.longrpc.model.RpcRequest;
import com.zyl.longrpc.protocol.*;
import com.zyl.longrpc.serializer.Serializer;
import com.zyl.longrpc.serializer.SerializerFactory;
import com.zyl.longrpc.server.tcp.VertxTcpServer;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetServer;
import io.vertx.core.net.NetSocket;
import io.vertx.core.parsetools.RecordParser;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.checkerframework.checker.units.qual.A;
import org.checkerframework.checker.units.qual.C;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

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


    public void testTcpServer() {
        int port = 8088;
        Vertx vertx = Vertx.vertx();
        // 创建TCP服务器
        NetServer netServer = vertx.createNetServer();
        netServer.connectHandler(new Handler<NetSocket>() {
            @Override
            public void handle(NetSocket netSocket) {
                netSocket.write(Buffer.buffer("Hello client!"));
                String sendMsg = "abcdefghijklmnopjrstuvwxyz";
                // 解决粘包半包
                RecordParser recordParser = RecordParser.newFixed(sendMsg.getBytes().length);

                recordParser.setOutput(new Handler<Buffer>() {
                    @Override
                    public void handle(Buffer buffer) {
                        //处理请求
                        if (buffer.getBytes().length > sendMsg.getBytes().length) {
                            System.out.println("粘包:" + buffer.getBytes().length);
                            return;
                        }
                        if (buffer.getBytes().length < sendMsg.getBytes().length) {
                            System.out.println("半包:" + buffer.getBytes().length);
                            return;
                        }
                        String s = new String(buffer.getBytes());
                        System.out.println(s);
                    }
                });
                netSocket.handler(recordParser);

//                netSocket.handler(new Handler<Buffer>() {
//                    @Override
//                    public void handle(Buffer buffer) {
//                        //处理请求
//                        if (buffer.getBytes().length > sendMsg.getBytes().length) {
//                            System.out.println("粘包:" + buffer.getBytes().length);
//                            return;
//                        }
//                        if (buffer.getBytes().length < sendMsg.getBytes().length) {
//                            System.out.println("半包:" + buffer.getBytes().length);
//                            return;
//                        }
//
//                        String s = new String(buffer.getBytes());
//                        System.out.println(s);
//                    }
//                });
            }
        });
        netServer.listen(port, new Handler<AsyncResult<NetServer>>() {
            @Override
            public void handle(AsyncResult<NetServer> netServerAsyncResult) {
                if (netServerAsyncResult.succeeded()) {
                    System.out.println("TCP服务器启动成功，端口号：" + port);
                }
            }
        });
    }

    public void testTcpClient() {
        int port = 8088;
        String host = "localhost";

        Vertx vertx = Vertx.vertx();
        NetClient netClient = vertx.createNetClient();

//        netClient.connect(port,host, new Handler<AsyncResult<NetClient>>() {
//            @Override
//            public void handle(AsyncResult<NetClient> netClientAsyncResult) {
//                if(netClientAsyncResult.succeeded()){
////                    System.out.println("123");
////                    NetClient result = netClientAsyncResult.result();
//
//                }
//            }
//        });

        netClient.connect(port, host, new Handler<AsyncResult<NetSocket>>() {
            @Override
            public void handle(AsyncResult<NetSocket> netSocketAsyncResult) {
                if (netSocketAsyncResult.succeeded()) {
                    //连接成功
                    System.out.println("连接TCP服务器成功," + host + ":" + port);

                    //连接成功后可以获取socket对象，使用map存储，这样就可以组发或者给某一个发送信息了

                    NetSocket socket = netSocketAsyncResult.result();
                    String sendMsg = "abcdefghijklmnopjrstuvwxyz";
                    int i = 1000;
                    // 发送信息
                    while (i > 0) {
                        socket.write(sendMsg);
                        i--;
                    }

                    // 接受消息并处理
                    socket.handler(new Handler<Buffer>() {
                        @Override
                        public void handle(Buffer buffer) {
                            byte[] bytes = buffer.getBytes();
                            System.out.println("收到服务端信息：" + new String(bytes));
                        }
                    });
                } else {
                    System.out.println("连接TCP失败," + netSocketAsyncResult.cause());
                }
            }
        });


    }

    public void testAtomicInteger(){
        AtomicInteger atomicInteger = new AtomicInteger(0);
        int max = Integer.MAX_VALUE;
        int andIncrement = 0;
        while (max>0){
            andIncrement = atomicInteger.getAndIncrement();
            max--;
        }
        max = 100;
        while (max>0){
            andIncrement = atomicInteger.getAndIncrement();
            max--;
        }
        System.out.println(andIncrement);


    }

    public static void main(String[] args) {
        AppTest appTest = new AppTest("");
        appTest.testTcpServer();
        appTest.testTcpClient();
    }

}
