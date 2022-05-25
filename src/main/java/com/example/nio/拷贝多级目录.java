package com.example.nio;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class 拷贝多级目录 {
    public static void main(String[] args) throws Exception {
        String source = "";
        String target = "";
        Files.walk(Paths.get(source)).forEach(path -> {
            try {
            String targetName = path.toString().replace(source, target);
            if(Files.isDirectory(path)){
                Files.createDirectory(Paths.get(targetName));
            }else if(Files.isRegularFile(path)){
                Files.copy(path,Paths.get(targetName));
            }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
