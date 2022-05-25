package com.example.nio.网络编程.多线程;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * select（）阻塞
 * 可能导致register注册失败问题解决方法1和方法2
 * CPU密集
 * IO密集
 */
public class BossWorkerServer {
    public static void main(String[] args) throws IOException {
        Thread.currentThread().setName("boss");
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false);
        Selector boss = Selector.open();
        SelectionKey sscKey = ssc.register(boss, 0, null);
        sscKey.interestOps(SelectionKey.OP_ACCEPT);
        ssc.bind(new InetSocketAddress(8080));
        /**
         * Runtime.getRuntime().availableProcessors()
         * docker容器下拿到的是物理CPU个数
         * jdk10修复了，使用jvm参数UseContainerSupport配置，默认开启
         */
        Worker[] workers = new Worker[Runtime.getRuntime().availableProcessors()];
        for (int i = 0; i < workers.length; i++) {
            workers[i] = new Worker("worker-"+i);
        }
        AtomicInteger index = new AtomicInteger();
        while (true){
            boss.select();
            Iterator<SelectionKey> iterator = boss.selectedKeys().iterator();
            while(iterator.hasNext()){
                SelectionKey key = iterator.next();
                iterator.remove();
                if(key.isAcceptable()){
                    SocketChannel sc = ssc.accept();
                    sc.configureBlocking(false);
                    System.out.println("connected ..."+sc.getRemoteAddress());
                    //round robin 轮询
                    workers[index.getAndIncrement()%workers.length].register(sc);
                }
            }
        }
    }
    static class Worker implements Runnable{
        private Thread thread;
        private Selector selector;
        private String name;
        private boolean start = false;
        private ConcurrentLinkedQueue<Runnable> queue = new ConcurrentLinkedQueue<>();
        public Worker(String name){
            this.name = name;
        }
        //初始化线程和selector
        public void register(SocketChannel sc) throws IOException {
            if(!start){
                thread = new Thread(this,name);
                thread.start();
                selector = Selector.open();
                start = true;
            }
            //方法1：向队列中添加了任务，但是任务并没有立即执行
//            queue.add(()->{
//                try {
//                    sc.register(selector,SelectionKey.OP_READ,null);
//                } catch (ClosedChannelException e) {
//                    e.printStackTrace();
//                }
//            });
            selector.wakeup();//唤醒select方法 boss线程
            //方法2
            sc.register(selector,SelectionKey.OP_READ,null); //boss线程
        }
        @Override
        public void run() {
            try {
                selector.select();//阻塞的
                //方法1：
//                Runnable task = queue.poll();
                //如果不在同一个线程，执行先后顺序不可控，
                //执行sc.register(selector,SelectionKey.OP_READ,null)注册事件
                //方法1：
//                task.run();
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()){
                    SelectionKey key = iterator.next();
                    iterator.remove();
                    if (key.isReadable()) {
                        ByteBuffer buffer = ByteBuffer.allocate(16);
                        SocketChannel sc = (SocketChannel) key.channel();
                        sc.read(buffer);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
