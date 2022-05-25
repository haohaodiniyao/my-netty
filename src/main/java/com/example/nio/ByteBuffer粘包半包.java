package com.example.nio;

import java.nio.ByteBuffer;

/**
 * i = 11
 * source.position = 0
 * length = 12
 * Hello,world
 * i = 24
 * source.position = 12
 * length = 13
 * I'm zhangsan
 * i = 12
 * source.position = 0
 * length = 13
 * How are you?
 */
public class ByteBuffer粘包半包 {
    public static void main(String[] args) {
        ByteBuffer source = ByteBuffer.allocate(32);
        source.put("Hello,world\nI'm zhangsan\nHo".getBytes());
        split(source);
        source.put("w are you?\n".getBytes());
        split(source);
    }
    private static void split(ByteBuffer source){
        source.flip();//切换到读模式
        for (int i = 0; i < source.limit(); i++) {
            if (source.get(i) == '\n') {
                System.out.println("i = "+i);
                int length = i + 1 - source.position();
                System.out.println("source.position = "+source.position());
                System.out.println("length = "+length);
                ByteBuffer target = ByteBuffer.allocate(length);
                for (int j = 0; j < length; j++) {
                    byte b = source.get();
                    System.out.print((char) b);
                    target.put(b);
                }
            }
        }
        //注意Ho没有读完
        source.compact();//切换到写模式
    }
}
