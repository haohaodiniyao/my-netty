package com.example.netty.hello;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;

public class HelloServer {
    public static void main(String[] args) {
        new ServerBootstrap()
                //EventLoopGroup (selector + thread)
                .group(new NioEventLoopGroup())
                .channel(NioServerSocketChannel.class)
                //boss 处理连接 worker（child）处理读写，worker（child）执行哪些操作（handler）
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    //channel 和客户端进行数据读写的通道
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        //添加具体handler
                        ch.pipeline().addLast(new StringDecoder());//ByteBuf转string
                        ch.pipeline().addLast(new ChannelInboundHandlerAdapter(){//自定义handler
                            //处理读事件
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                //打印上一步转换好的字符串
                                System.out.println("收到："+msg);
//                                super.channelRead(ctx, msg);
                            }
                        });
                    }
                }).bind(8080);
    }
}
