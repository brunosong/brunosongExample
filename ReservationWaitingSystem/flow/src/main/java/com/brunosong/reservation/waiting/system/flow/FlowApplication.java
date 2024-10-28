package com.brunosong.reservation.waiting.system.flow;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@RequiredArgsConstructor
public class FlowApplication {

    public static void main(String[] args) {
        SpringApplication.run(FlowApplication.class,args);
    }

}