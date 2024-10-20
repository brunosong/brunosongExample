package com.brunosong.exam;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.brunosong.exam")
public class SpringWebFluxApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringWebFluxApplication.class,args);
    }
}