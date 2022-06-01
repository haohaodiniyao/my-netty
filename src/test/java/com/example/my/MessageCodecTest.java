package com.example.my;

import com.example.netty.chat.message.LoginRequestMessage;
import com.example.netty.chat.protocol.MessageCodec;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class MessageCodecTest {
    public static void main(String[] args) {
        EmbeddedChannel embeddedChannel = new EmbeddedChannel(
                new LoggingHandler(LogLevel.DEBUG),
                new MessageCodec()
        );
        embeddedChannel.writeOutbound(new LoginRequestMessage("zhangsan", "123456"));
    }
}
