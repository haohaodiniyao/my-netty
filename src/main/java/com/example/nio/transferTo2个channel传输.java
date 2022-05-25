package com.example.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class transferTo2个channel传输 {
    public static void main(String[] args) {
        try (FileChannel fromChannel = new FileInputStream("from.txt").getChannel();
             FileChannel toChannel = new FileOutputStream("to.txt").getChannel()) {
            //效率高，操作系统零拷贝，最多传输2g
            long size = fromChannel.size();
            //left代表还剩余多少字节
            //多次传输
            for(long left = size;left>0;){
                //l 实际传输字节
                //size - left 下一次传输开始position
                long l = fromChannel.transferTo(size - left, left, toChannel);
                left = left - l;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
