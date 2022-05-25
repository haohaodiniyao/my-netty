package com.example.nio;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class ByteBuffer2String {
    public static void main(String[] args) {
        //1、字符串转ByteBuffer(没有自动切换到读模式)
        ByteBuffer buffer = ByteBuffer.allocate(16);
        buffer.put("hello".getBytes());

        //2 自动切换到读模式
        ByteBuffer hello = StandardCharsets.UTF_8.encode("hello");

        //3 自动切换到读模式
        ByteBuffer wrap = ByteBuffer.wrap("hello".getBytes());
    }
}
