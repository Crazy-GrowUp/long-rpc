package com.zyl.longrpc.server.tcp;

import com.zyl.longrpc.protocol.ProtocolConstant;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.parsetools.RecordParser;


/**
 * @program: long-rpc
 * @description: 装饰者模式 使用recordParser对原有buffer增强
 * @author: yl.zhan
 * @create: 2024-12-11 16:47
 **/
public class TcpBufferHandlerWrapper implements Handler<Buffer> {
    private final RecordParser recordParser;

    public TcpBufferHandlerWrapper(Handler<Buffer> bufferHandler) {
        recordParser = initRecordParser(bufferHandler);
    }

    public RecordParser initRecordParser(Handler<Buffer> bufferHandler) {
        RecordParser parser = RecordParser.newFixed(ProtocolConstant.MESSAGE_HEADER_LENGTH);

        parser.setOutput(new Handler<Buffer>() {
            int size = -1;
            Buffer resultBuffer = Buffer.buffer();

            @Override
            public void handle(Buffer buffer) {
                if (-1 == size) {
                    size = buffer.getInt(13);
                    parser.fixedSizeMode(size);

                    resultBuffer.appendBuffer(buffer);
                } else {
                    resultBuffer.appendBuffer(buffer);
                    bufferHandler.handle(resultBuffer);

                    parser.fixedSizeMode(ProtocolConstant.MESSAGE_HEADER_LENGTH);
                    size = -1;
                    resultBuffer = Buffer.buffer();
                }
            }
        });

        return parser;

    }

    @Override
    public void handle(Buffer buffer) {
        recordParser.handle(buffer);
    }
}
