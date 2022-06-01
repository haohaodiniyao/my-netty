package com.example.netty.event.loop;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

/**
 * Future
 * Promise
 * 都是配合异步方法
 * 处理结果
 */
@Slf4j
public class EventLoopClientChannelFuture {
    public static void main(String[] args) throws InterruptedException {
        ChannelFuture channelFuture = new Bootstrap()
                .group(new NioEventLoopGroup())
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    //连接建立后被调用
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new StringEncoder());
                    }
                })
                //连接到服务器（异步非阻塞），主线程发起connect，真正connect是NIO线程
                .connect(new InetSocketAddress("localhost", 8080));
        //1、阻塞当前线程，直到nio线程建立connect连接，必须的
//        channelFuture.sync();
//        Channel channel = channelFuture.channel();
//        log.info("{}",channel);
        //向服务器发送数据
//        channel.writeAndFlush("hello world");

        //2、连接建立，回调对象，异步处理结果
        channelFuture.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                Channel channel = channelFuture.channel();
                //nio线程，不是主线程执行
                log.info("{}", channel);
                // 向服务器发送数据
                channel.writeAndFlush("hello world");
            }
        });
        log.info("");
    }
}
