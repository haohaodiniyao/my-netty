package com.example.netty.packag;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.AdaptiveRecvByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * 沾包
 * 半包
 */
@Slf4j
public class HelloWorldServer {
    public static void main(String[] args) {
        NioEventLoopGroup boss = new NioEventLoopGroup();
        NioEventLoopGroup worker = new NioEventLoopGroup();
        try {
            ChannelFuture channelFuture = new ServerBootstrap()
                    .group(boss, worker)
                    //调整系统接收缓冲区 10字节 复现半包
//                    .option(ChannelOption.SO_RCVBUF, 10)
                    //调整netty的接收缓冲区
                    .childOption(ChannelOption.RCVBUF_ALLOCATOR, new AdaptiveRecvByteBufAllocator(16, 16, 16))
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel channel) throws Exception {
                            //定长解码
//                            channel.pipeline().addLast(new FixedLengthFrameDecoder(10));
//                            channel.pipeline().addLast(new LineBasedFrameDecoder(10));
//                            channel.pipeline().addLast(new DelimiterBasedFrameDecoder(10, ));
//                            channel.pipeline().addLast(new LengthFieldBasedFrameDecoder())

                            channel.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG));
                        }
                    })
                    .bind(8080).sync();
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            log.error("com.example.server error", e);
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }
}
