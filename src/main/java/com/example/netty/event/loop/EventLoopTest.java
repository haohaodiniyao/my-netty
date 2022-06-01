package com.example.netty.event.loop;

import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.NettyRuntime;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@Slf4j
public class EventLoopTest {
    public static void main(String[] args) {
        //io事件、普通任务、定时任务
        NioEventLoopGroup group = new NioEventLoopGroup(2);
        //普通任务、定时任务
//        DefaultEventLoopGroup group1 = new DefaultEventLoopGroup();
//        System.out.println("cores = " + NettyRuntime.availableProcessors());
        //获取事件循环对象
        log.info("{}",group.next());
        log.info("{}",group.next());
        log.info("{}",group.next());
        log.info("{}",group.next());
        /**
         * 轮询的效果
         * 22:30:23.190 [main] INFO com.example.netty.event.loop.EventLoopTest - io.netty.channel.nio.NioEventLoop@42f30e0a
         * 22:30:23.190 [main] INFO com.example.netty.event.loop.EventLoopTest - io.netty.channel.nio.NioEventLoop@24273305
         * 22:30:23.190 [main] INFO com.example.netty.event.loop.EventLoopTest - io.netty.channel.nio.NioEventLoop@42f30e0a
         * 22:30:23.190 [main] INFO com.example.netty.event.loop.EventLoopTest - io.netty.channel.nio.NioEventLoop@24273305
         */
        //执行普通任务
        group.next().submit(()->{
            log.info("执行普通任务");
        });

        //执行定时任务
        group.next().scheduleAtFixedRate(()->{
            log.info("执行定时任务");
        },0,1, TimeUnit.SECONDS);


        log.info("main");
    }
}
