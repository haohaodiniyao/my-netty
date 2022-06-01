package com.example.netty.future;

import java.util.concurrent.*;

public class JDKFuture {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(2);

        Future<Integer> future = executorService.submit(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                Thread.sleep(1000);
                return 101;
            }
        });
        //主线程通过get方法等待
        future.get();
    }
}
