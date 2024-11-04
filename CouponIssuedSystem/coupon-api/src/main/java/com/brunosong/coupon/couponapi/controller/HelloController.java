package com.brunosong.coupon.couponapi.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    // 초당 두건 * 200 ( 서버에서 동시에 처리할 수 있는 수 ) 톰캣 쓰레드 200 기본
    // 초당 두건 * 400 이면 RPS 800을 예상한다.
    @GetMapping("/hello")
    public String hello() throws InterruptedException {
        Thread.sleep(500);
        return "hello!";
    }
}
