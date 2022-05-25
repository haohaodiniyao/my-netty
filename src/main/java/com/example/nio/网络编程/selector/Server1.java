package com.example.nio.网络编程.selector;

import com.google.common.collect.Lists;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.List;

public class Server1 {
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
    public static void main(String[] args) throws IOException {
        //创建selector
        Selector selector = Selector.open();
        //创建服务器
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false);//非阻塞模式
        //建立selector和channel的联系
        //SelectionKey就是将来时间发生后，通过它可以知道事件和哪个channel的事件
        //accept connect read write 事件
        SelectionKey sscKey = ssc.register(selector, 0, null);
        //感兴趣事件
        sscKey.interestOps(SelectionKey.OP_ACCEPT);
        //绑定监听端口
        ssc.bind(new InetSocketAddress(8080));
        List<SocketChannel> channelList = Lists.newArrayList();
        //一直在循环执行
        while (true){
            //没有事件，线程阻塞;注意：有事件未处理，不阻塞
            selector.select();
            //处理事件
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();//accept read
            while(iterator.hasNext()){
                SelectionKey key = iterator.next();
                //处理完这个key就删除
                iterator.remove();
//                key.cancel();
                if(key.isAcceptable()){//accept
                    ServerSocketChannel channel = (ServerSocketChannel)key.channel();
                    SocketChannel sc = channel.accept();
                    sc.configureBlocking(false);
                    ByteBuffer buffer = ByteBuffer.allocate(16);
                    SelectionKey scKey = sc.register(selector, 0, buffer);
                    scKey.interestOps(SelectionKey.OP_READ);
                }else if(key.isReadable()){//read
                    try{
                        SocketChannel channel = (SocketChannel)key.channel();
                        //消息边界问题处理（定长、分隔符、LTV）
                        //buffer不能局部变量
                        //内容大于16情况
//                        ByteBuffer buffer = ByteBuffer.allocate(16);
                         ByteBuffer buffer = (ByteBuffer)key.attachment();
                        int read = channel.read(buffer);
                        if(read == -1){//客户端正常断开返回-1
                            key.cancel();
                        }else{
//                            buffer.flip();
//                            System.out.println(Charset.defaultCharset().decode(buffer));
//                            buffer.clear();
                            split(buffer);//分隔符
                            //内容大于16，扩容
                            if(buffer.position() == buffer.limit()){
                                ByteBuffer newBuffer = ByteBuffer.allocate(buffer.capacity() * 2);
                                buffer.flip();
                                newBuffer.put(buffer);
                                key.attach(newBuffer);
                            }
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                        //客户端强制断开连接情况(需要将key取消，即从selector的keys中删除)
                        //拿到了key必须要处理
                        key.cancel();
                    }
                }

            }
        }
    }
}
