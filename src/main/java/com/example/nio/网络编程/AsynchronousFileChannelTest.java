package com.example.nio.网络编程;


import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class AsynchronousFileChannelTest {
    public static void main(String[] args) throws Exception {
        try (AsynchronousFileChannel channel = AsynchronousFileChannel.open(Paths.get("data.txt"), StandardOpenOption.READ)) {
            ByteBuffer buffer = ByteBuffer.allocate(16);
            System.out.println("read begin...");
            /**
             * 参数1 ByteBuffer
             * 参数2 读取的起始位置
             * 参数3 附件
             * 参数4 回调
             */
            channel.read(buffer, 0, buffer, new CompletionHandler<Integer, ByteBuffer>() {
                @Override
                public void completed(Integer result, ByteBuffer attachment) {
                    //成功
                    System.out.println("read completed, result = " + result + ", attachment = " + attachment);
                    attachment.flip();
//                    ByteBufferUtil.debugAll(attachment);
                }

                @Override
                public void failed(Throwable exc, ByteBuffer attachment) {
                    //失败
                    exc.printStackTrace();
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
        Thread.sleep(1000);
        //等待控制台输入
//        System.in.read();

    }
}
