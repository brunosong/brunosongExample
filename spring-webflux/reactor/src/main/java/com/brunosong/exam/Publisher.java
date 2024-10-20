package com.brunosong.exam;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class Publisher {

    public Flux<Integer> startFlux() {
        // Flux.just(1,2,3,4,5)
        // Flux.fromIterable(List.of("a","b","c"))
        return Flux.range(1,10).log();
    }

    public Mono<?> startMono() {
        // 단일값
        //return Mono.just(1).log();

        // 비어있는 값을 보낼 수 있다
        return Mono.empty().log();
    }
}
