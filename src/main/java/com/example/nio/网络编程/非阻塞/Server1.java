package com.example.nio.网络编程.非阻塞;

import com.google.common.collect.Lists;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.List;

public class Server1 {
    public static void main(String[] args) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(16);
        //创建服务器
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false);//非阻塞模式
        //绑定监听端口
        ssc.bind(new InetSocketAddress(8080));
        List<SocketChannel> channelList = Lists.newArrayList();
        //一直在循环执行
        while (true){
            //accept建立与客户端连接，SocketChannel用来与客户端通信
            SocketChannel sc = ssc.accept();//非阻塞 没有连接返回null
            if(sc != null){
                channelList.add(sc);
                sc.configureBlocking(false);
            }
            for (SocketChannel channel:channelList){
                //接收客户端发送的请求
                int read = channel.read(buffer);//非阻塞 没有读到数据返回0
                if(read>0){
                    buffer.flip();
                    buffer.clear();
                }
            }
        }
    }
}
