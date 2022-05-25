package com.example.nio;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class ByteBufferTest {
    public static void main(String[] args) {
        //获取FileChannel 1.输入输出流  2。RandomAccessFile
        //twr 快捷生成try catch
        try (FileChannel fileChannel = new FileInputStream("data.txt").getChannel()) {
            //准备缓冲区
            ByteBuffer buffer = ByteBuffer.allocate(10);
            while (true){
                //从channel读，向buffer写
                int len = fileChannel.read(buffer);
                if(len == -1){
                    break;
                }
                //打印buffer内容
                buffer.flip();//切换到读模式
                //是否有数据
                while(buffer.hasRemaining()){
                    byte b = buffer.get();//默认1次读1字节
                    System.out.print((char)b);
                }
                buffer.clear();//切换到写模式
            }

        } catch (IOException e) {
        }
    }
}
