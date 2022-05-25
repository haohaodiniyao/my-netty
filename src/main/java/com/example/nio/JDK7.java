package com.example.nio;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.concurrent.atomic.AtomicInteger;

public class JDK7 {
    public static void main(String[] args) throws Exception {
        // . 当前路径
        // .. 上一级路径
//        Path path = Paths.get("");
//        path.normalize();

//        Files.exists(path);
        //创建1层目录
//        Files.createDirectory(path);
        //创建多层目录
//        Files.createDirectories(path);

//        Path from = Paths.get("from.txt");
//        Path to = Paths.get("to2.txt");
//        Files.copy(from,to);
        //文件存在，java.nio.file.FileAlreadyExistsException: to.txt
        //from覆盖to
//        Files.copy(from,to, StandardCopyOption.REPLACE_EXISTING);

        //原子性移动文件
//        Files.move(from,to,StandardCopyOption.ATOMIC_MOVE);

        //删除文件和目录（只能删除空目录）
//        Files.delete(from);

        AtomicInteger dirCount = new AtomicInteger();
        AtomicInteger fileCount = new AtomicInteger();
        //遍历文件
        Files.walkFileTree(Paths.get("/Users/yaokai/Downloads/obs"),new SimpleFileVisitor<Path>(){
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                dirCount.incrementAndGet();
                return super.preVisitDirectory(dir, attrs);
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                fileCount.incrementAndGet();
                return super.visitFile(file, attrs);
            }
        });

        System.out.println("dirCount = "+dirCount);
        System.out.println("fileCount = " + fileCount);

    }
}
