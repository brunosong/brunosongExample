package com.brunosong.exam;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;

public class Operator2 {

    // 순차적으로 처리한다.
    // FlatMap 은 순서가 지켜지지 않는다.
    // concatMap
    public Flux<Integer> fluxConcatMap() {
        return Flux.range(1,10)
                .concatMap(i -> Flux.range(i * 10,10)
                        .delayElements(Duration.ofMillis(100))
                ).log();
    }

    // flatmapmany mono to flux
    public Flux<Integer> monoFlatMapMany() {
        return Mono.just(10)
                .flatMapMany(i -> Flux.range(1,i)).log();
    }

    // defaultIfEmpty, switchIfEmpty
    public Mono<Integer> defaultIfEmpty1() {
        return Mono.just(100)
                .filter(i -> i >100)
                // 값이 없으면 이 값을 쓰겠다
                .defaultIfEmpty(30).log();

    }

    public Mono<Integer> switchIfEmpty1() {
        return Mono.just(100)
                .filter(i -> i >100)
                .switchIfEmpty(Mono.just(30)
                        .map(i -> i * 2)).log();
    }

    public Mono<Integer> switchIfEmpty2() {
        return Mono.just(100)
                .filter(i -> i >100)
                .switchIfEmpty(Mono.error(new Exception("Not exists value...")))
                .log();
    }

    // merge / zip
    public Flux<String> fluxMerge() {
        return Flux.merge(Flux.fromIterable(List.of("1","2","3")), Flux.just("4"))
                .log();
    }

    public Flux<String> monoMerge() {
        return Mono.just("1").mergeWith(Mono.just("2"))
                .mergeWith(Mono.just("3"));
    }

    public Flux<String> fluxZip() {
        return Flux.zip(Flux.just("a","b","c"), Flux.just("d","e","f"))
                .map(i -> i.getT1() + i.getT2())
                .log();
    }

    public Mono<String> monoZip() {
        return Mono.zip(Mono.just("1"), Mono.just("d"), Mono.just("c"))
                .map(i -> i.getT1() + i.getT2() + i.getT3())
                .log();
    }

}
