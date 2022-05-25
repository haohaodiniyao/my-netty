package com.example.nio.网络编程.非阻塞;

import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

public class Client1 {
    public static void main(String[] args) throws Exception {
        SocketChannel sc = SocketChannel.open();
        sc.connect(new InetSocketAddress("localhost",8080));
        System.out.println("waiting...");
    }
}
