package com.brunosong.exam;

import reactor.core.publisher.Flux;

public class Publisher {

    public Flux<Integer> startFlux() {
        // Flux.just(1,2,3,4,5)
        // Flux.fromIterable(List.of("a","b","c"))
        return Flux.range(1,10).log();
    }
}
