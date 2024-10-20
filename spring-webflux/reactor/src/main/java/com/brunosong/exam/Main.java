package com.brunosong.exam;

public class Main {
    public static void main(String[] args) {
        var publisher = new Publisher();

        // 반드시 subscribe를 가져와서 처리 해줘야 한다.
        // 구조는 Reactor1.png 확인
//        publisher.startFlux()
//                .subscribe(System.out::println);

        publisher.startMono()
                        .subscribe(System.out::println);

        System.out.println("Hello world!");
    }
}