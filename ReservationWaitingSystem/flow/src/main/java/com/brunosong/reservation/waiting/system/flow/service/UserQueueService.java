package com.brunosong.reservation.waiting.system.flow.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class UserQueueService {

    private final ReactiveRedisTemplate<String,String> reactiveRedisTemplate;

    // 대기열 등록 API
    public Mono<Boolean> registerWaitQueue(final Long userId) {
        // 먼저 등록한 사람이 먼저 나올 수 있도록 redis sortedset 사용
        long unixTimestamp = Instant.now().getEpochSecond();
        return reactiveRedisTemplate.opsForZSet().add("user-queue", userId.toString(), unixTimestamp);
    }
}
