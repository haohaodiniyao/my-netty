package com.example.nio.网络编程.selector;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;

/**
 * 可写事件
 */
public class WriteServer {
    public static void main(String[] args) throws IOException {
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false);
        Selector selector = Selector.open();
        ssc.register(selector, SelectionKey.OP_ACCEPT);
        ssc.bind(new InetSocketAddress(8080));
        while (true){
            selector.select();
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while(iterator.hasNext()){
                SelectionKey key = iterator.next();
                iterator.remove();
                if(key.isAcceptable()){
                    SocketChannel sc = ssc.accept();
                    sc.configureBlocking(false);
                    SelectionKey scKey = sc.register(selector, SelectionKey.OP_READ);

                    //向客户端发送大量数据
                    StringBuffer sb = new StringBuffer();
                    for (int i = 0; i < 30000000; i++) {
                        sb.append("a");
                    }
                    ByteBuffer buffer = Charset.defaultCharset().encode(sb.toString());

                    //实际写多少字节
                    int write = sc.write(buffer);
                    //写缓冲区有限，可能分多次写，缓冲区满了，写不进去了，write=0
                    /**
                     * 3000
                     * 0
                     * 0
                     * 3000
                     * 0
                     * 0
                     * 0
                     */
                    System.out.println("write = "+write);
                    //判断是否有剩余内存
                    if (buffer.hasRemaining()){
                        scKey.interestOps(scKey.interestOps()+SelectionKey.OP_WRITE);
//                        scKey.interestOps(scKey.interestOps() | SelectionKey.OP_WRITE);
                        scKey.attach(buffer);
                    }

                }else if(key.isReadable()){

                }else if(key.isWritable()){//可写事件处理1次写不完
                    //写缓冲区可写了
                    ByteBuffer buffer = (ByteBuffer) key.attachment();
                    SocketChannel sc = (SocketChannel) key.channel();
                    //有可能这次也没写完，但是关注了可写事件，还会进入
                    int write = sc.write(buffer);
                    System.out.println("write = "+write);
                    //清理操作(全部写完了)
                    if(!buffer.hasRemaining()){
                        key.attach(null);//附件
                        key.interestOps(key.interestOps()-SelectionKey.OP_WRITE);//不关注写事件
                    }
                }
            }
        }


    }
}
