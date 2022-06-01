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
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.Scanner;

@Slf4j
public class EventLoopClient {
    public static void main(String[] args) throws InterruptedException {
        NioEventLoopGroup group = new NioEventLoopGroup();
        Channel channel = new Bootstrap()
                //添加EventLoop
                .group(group)
                //选择客户端channel实现
                .channel(NioSocketChannel.class)
                //添加处理器
                .handler(new ChannelInitializer<SocketChannel>() {
                    //连接建立后被调用
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        //详细日志
                        ch.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG));
                        ch.pipeline().addLast(new StringEncoder());
                    }
                })
                //连接到服务器
                .connect(new InetSocketAddress("localhost", 8080))
                .sync()
                .channel();
        //向服务器发送数据
//                .writeAndFlush("hello world");
//        log.info("{}",channel);
//        log.info("");

        //键盘输入数据，发送
        new Thread(() -> {
            Scanner scanner = new Scanner(System.in);
            while (true) {
                String s = scanner.nextLine();
                if ("q".equals(s)) {
                    //close是异步操作，其他线程执行close
                    channel.close();
                    break;
                }
                channel.writeAndFlush(s);
            }
        }, "input").start();

        //1、同步处理关闭后操作  2、异步处理关闭后操作
        ChannelFuture closeFuture = channel.closeFuture();
//        log.info("waiting close...");
//        closeFuture.sync();
//        log.info("处理关闭之后的操作");


        closeFuture.addListener((ChannelFutureListener) channelFuture -> {
            log.info("处理关闭之后的操作");
            //优雅关闭线程池
            group.shutdownGracefully();
        });
    }
}
