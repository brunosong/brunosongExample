package com.brunosong.exam;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public class Publisher {

    public Flux<Integer> startFlux() {
        // Flux.just(1,2,3,4,5)
        // Flux.fromIterable(List.of("a","b","c"))
        return Flux.range(1,10).log();
    }

    public Flux<String> startFlux2() {
        return Flux.fromIterable(List.of("a","b","c")).log();
    }

    public Mono<?> startMono() {
        // 비어있는 값을 보낼 수 있다
        return Mono.empty().log();
    }

    public Mono<Integer> startMono2() {
        // 단일값
        return Mono.just(1).log();
    }

    public Mono<?> startMono3() {
        // 에러
        return Mono.error(new Exception("hello reactor")).log();
    }
}
