package com.example.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_LENGTH;

/**
 *
 */
@Slf4j
public class Http协议 {
    public static void main(String[] args) {
        NioEventLoopGroup boss = new NioEventLoopGroup();
        NioEventLoopGroup worker = new NioEventLoopGroup();
        try {

            ChannelFuture channelFuture = new ServerBootstrap()
                    .group(boss, worker)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel channel) throws Exception {
                            channel.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG));
                            //http 编解码器一体
                            channel.pipeline().addLast(new HttpServerCodec());

                            //只关注HttpRequest类型的消息
                            channel.pipeline().addLast(new SimpleChannelInboundHandler<HttpRequest>() {
                                @Override
                                protected void channelRead0(ChannelHandlerContext ctx, HttpRequest httpRequest) throws Exception {
                                    log.info("请求行：{}", httpRequest.uri());
                                    //返回响应
                                    DefaultFullHttpResponse res = new DefaultFullHttpResponse(httpRequest.protocolVersion(), HttpResponseStatus.OK);
                                    byte[] bytes = "<h1>Hello, world</h1>".getBytes();
                                    //内容长度，否则浏览器一直等着
                                    res.headers().setInt(CONTENT_LENGTH, bytes.length);
                                    res.content().writeBytes(bytes);
                                    ctx.writeAndFlush(res);
                                }
                            });

//                            channel.pipeline().addLast(new ChannelInboundHandlerAdapter() {
//                                @Override
//                                public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//                                    log.info("{}", msg.getClass());
//                                    //请求行 请求头
//                                    if (msg instanceof HttpRequest) {
//
//                                    } else if (msg instanceof HttpContent) {//请求体
//
//                                    }
//                                }
//                            });
                        }
                    })
                    .bind(8080).sync();
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            log.error("http com.example.server", e);
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }
}
