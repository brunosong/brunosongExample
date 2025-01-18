package com.brunosong.springbasic;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
public class HelloController {

    private HelloService helloService;

    public HelloController(HelloService helloService) {
        this.helloService = helloService;
    }

    @GetMapping("/hello")
    public String hello(String name) {
        // 컨트롤러에 중요한 역할은 값을 검증하는 것이다.
        return helloService.sayHello(Objects.requireNonNull(name));
    }
}
