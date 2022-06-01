package com.example.netty.event.loop;

import com.sun.corba.se.internal.CosNaming.BootstrapServer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.DefaultEventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;

@Slf4j
public class EventLoopServer {
    public static void main(String[] args) {
        //创建独立的EventLoopGroup处理耗时复杂的任务
        DefaultEventLoopGroup group = new DefaultEventLoopGroup();
        // 负责ServerSocketChannel的accept事件
        NioEventLoopGroup boss = new NioEventLoopGroup();
        // 负责SocketChannel的读写
        NioEventLoopGroup worker = new NioEventLoopGroup(2);
        new ServerBootstrap()
                .group(boss,worker)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel channel) throws Exception {
                        channel.pipeline().addLast("handler-1",new ChannelInboundHandlerAdapter(){
                            @Override                                          //ByteBuf
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//                                super.channelRead(ctx, msg);
                                ByteBuf byteBuf = (ByteBuf) msg;
                                log.info("handler-1 收到：{}",byteBuf.toString(Charset.forName("UTF-8")));
                                ctx.fireChannelRead(msg);//将消息传递给下一个handler
                            }
                        }).addLast(group,"handler-2",new ChannelInboundHandlerAdapter(){
                            @Override                                          //ByteBuf
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//                                super.channelRead(ctx, msg);
                                ByteBuf byteBuf = (ByteBuf) msg;
                                log.info("handler-2 收到：{}",byteBuf.toString(Charset.forName("UTF-8")));
                            }
                        });
                    }
                }).bind(8080);
    }
}
