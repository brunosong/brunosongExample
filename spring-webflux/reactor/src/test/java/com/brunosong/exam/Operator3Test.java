package com.brunosong.exam;

import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

class Operator3Test {

    private Operator3 operator = new Operator3();

    @Test
    void fluxCount() {
        StepVerifier.create(operator.fluxCount())
                .expectNext(10L)
                .verifyComplete();
    }

    @Test
    void fluxDistinct() {
        StepVerifier.create(operator.fluxDistinct())
                .expectNext("A","B","C")
                .verifyComplete();
    }

    @Test
    void fluxReduce() {
        StepVerifier.create(operator.fluxReduce())
                .expectNext(55)
                .verifyComplete();
    }

    @Test
    void fluxGroupBy() {
        StepVerifier.create(operator.fluxGroupBy())
                .expectNext(30)
                .expectNext(25)
                .verifyComplete();
    }

}