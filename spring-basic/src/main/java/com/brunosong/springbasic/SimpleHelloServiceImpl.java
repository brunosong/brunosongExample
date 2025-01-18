package com.brunosong.springbasic;

public class SimpleHelloServiceImpl implements HelloService {
    @Override
    public String sayHello(String name) {
        return "Hello " + name;
    }
}
