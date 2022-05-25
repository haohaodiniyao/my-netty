package com.example.nio;

import java.nio.ByteBuffer;

public class ByteBufferReadWrite {
    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocate(10);
        /**
         * class java.nio.HeapByteBuffer       java堆内存，读写效率较低，gc影响
         * class java.nio.DirectByteBuffer     直接内存，读写效率高（少拷贝），分配系统内存，必须释放
         */
        System.out.println(buffer.getClass());
        System.out.println(ByteBuffer.allocateDirect(10).getClass());
        //向buffer写入数据 或者 channel.read(buf)
        buffer.put((byte)0x61); //'a'
        buffer.flip();//切换到读模式
        //从buffer读数据  channel.write
        System.out.println(buffer.get());
        //重复读取数据 调用rewind position重置为0  或者  get(i)不移动读指针

        //mark（记录position）和reset（position重置到mark的位置）
    }
}
