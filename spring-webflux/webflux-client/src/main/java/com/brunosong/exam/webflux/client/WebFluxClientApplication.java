package com.brunosong.exam.webflux.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@SpringBootApplication
public class WebFluxClientApplication {
    public static void main(String[] args) {
        SpringApplication.run(WebFluxClientApplication.class,args);
    }

    @GetMapping("/posts/{id}")
    public Map<String,String> getPost(@PathVariable Long id) {
        return Map.of("id", id.toString(), "content","Posts content is %d".formatted(id));
    }
}
