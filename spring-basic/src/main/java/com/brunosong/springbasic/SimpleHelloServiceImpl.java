package com.brunosong.springbasic;

import org.springframework.stereotype.Service;

@Service
public class SimpleHelloServiceImpl implements HelloService {
    @Override
    public String sayHello(String name) {
        return "Hello " + name;
    }
}
