package com.example.netty.future;

import io.netty.channel.EventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.DefaultPromise;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutionException;

/**
 * 开发网络编程框架
 * 使用promise模式
 */
@Slf4j
public class NettyPromiseTest {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        EventLoop eventLoop = new NioEventLoopGroup().next();
        //结果容器
        DefaultPromise<Integer> promise = new DefaultPromise<>(eventLoop);

        new Thread(() -> {
            log.info("{}", "开始计算");
            try {
                Thread.sleep(1000);
                int a = 1 / 0;
                //主动存储结果
                promise.setSuccess(200);
            } catch (InterruptedException e) {
                promise.setFailure(e);
                e.printStackTrace();
            }
        }).start();
        log.info("{}", "等待计算结果");
        log.info("结果：{}", promise.get());
    }
}
