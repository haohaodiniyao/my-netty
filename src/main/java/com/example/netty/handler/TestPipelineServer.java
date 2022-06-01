package com.example.netty.handler;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;

@Slf4j
public class TestPipelineServer {
    public static void main(String[] args) {
        new ServerBootstrap()
                .group(new NioEventLoopGroup())
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {

                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
//                        pipeline.addLast(new StringDecoder());
                        //head h1 h2 h3 h4 h5 h6 tail 双向链表
                        //入站h1
                        pipeline.addLast("h1", new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                ByteBuf byteBuf = (ByteBuf) msg;
                                //转字符串
                                String s = byteBuf.toString(Charset.forName("utf-8"));
                                log.info("h1 收到：{}", s);
                                //数据传递给下一个handler
                                super.channelRead(ctx, s);
                            }
                        });
                        //入站h2
                        pipeline.addLast("h2", new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                log.info("h2 收到：{}", msg);
                                super.channelRead(ctx, msg);
                            }
                        });
                        //入站h3
                        pipeline.addLast("h3", new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                log.info("h3 收到：{}", msg);
//                                super.channelRead(ctx, msg);
                                //写数据了，触发出站handler h6 h5 h4 这个顺序
                                //从tail开始向前找出站处理器
                                ch.writeAndFlush(ctx.alloc().buffer().writeBytes("server".getBytes()));
                                //从当前handler向前找出站处理器
//                                ctx.writeAndFlush(ctx.alloc().buffer().writeBytes("com.example.server".getBytes()));
                            }
                        });

                        //出站
                        pipeline.addLast("h4", new ChannelOutboundHandlerAdapter() {
                            @Override
                            public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                                log.info("h4 出站");
                                super.write(ctx, msg, promise);
                            }
                        });

                        //出站
                        pipeline.addLast("h5", new ChannelOutboundHandlerAdapter() {
                            @Override
                            public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                                log.info("h5 出站");
                                super.write(ctx, msg, promise);
                            }
                        });

                        //出站
                        pipeline.addLast("h6", new ChannelOutboundHandlerAdapter() {
                            @Override
                            public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                                log.info("h6 出站");
                                super.write(ctx, msg, promise);
                            }
                        });
                    }
                })
                .bind(8080);
    }
}
