package com.example.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * LengthFieldBasedFrameDecoder
 */
public class LengthFieldBasedFrameDecoderTest {
    public static void main(String[] args) {
        EmbeddedChannel channel = new EmbeddedChannel(
                new LengthFieldBasedFrameDecoder(
                        //最大长度
                        1024,
                        //长度偏移量
                        0,
                        //长度4字节
                        4,
                        //长度跳过多少字节是内容（版本1个字节）
                        1,
                        //解析的时候剥离多少字节（比如 5 剩下的是内容）
                        0),
                new LoggingHandler(LogLevel.DEBUG)
        );
        //4个字节内容长度 + 1个字节版本 + 内容
        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer();
        send(buffer, "hello, world");
        send(buffer, "hi");
        send(buffer, "ni hao");
        channel.writeInbound(buffer);
    }

    private static void send(ByteBuf buf, String content) {
        byte[] bytes = content.getBytes();
        int length = bytes.length;
        buf.writeInt(length);
        buf.writeByte(1);
        buf.writeBytes(bytes);
    }
}
