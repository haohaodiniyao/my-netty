package com.example.netty.hello;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;

import java.net.InetSocketAddress;

public class HelloClient {
    public static void main(String[] args) throws InterruptedException {
        new Bootstrap()
                //添加EventLoop
                .group(new NioEventLoopGroup())
                //选择客户端channel实现
                .channel(NioSocketChannel.class)
                //添加处理器
                .handler(new ChannelInitializer<SocketChannel>() {
                    //连接建立后被调用
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new StringEncoder());
                    }
                })
                //连接到服务器
                .connect(new InetSocketAddress("localhost",8080))
                .sync()
                .channel()
                //向服务器发送数据
                .writeAndFlush("hello world");
    }
}
