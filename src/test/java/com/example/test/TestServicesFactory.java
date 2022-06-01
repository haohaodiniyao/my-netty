package com.example.test;


import com.example.netty.chat.server.service.HelloService;
import com.example.netty.chat.server.service.ServicesFactory;

public class TestServicesFactory {
    public static void main(String[] args) {
        HelloService service = ServicesFactory.getService(HelloService.class);
        System.out.println(service.sayHello("hi"));
    }
}
