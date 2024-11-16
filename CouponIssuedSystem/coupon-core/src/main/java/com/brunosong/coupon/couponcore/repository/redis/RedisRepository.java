package com.brunosong.coupon.couponcore.repository.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class RedisRepository {

    private final RedisTemplate<String,String> redisTemplate;

    public Boolean zAdd(String key, String value, double score) {
        // addIfAbsent : 데이터가 중복되면 무시한다 옵션은 NX를 사용한다. ZADD NX
        // sorted set을 사용한다고 모든 문제가 해결되지 않는다. 그 이유는
        // 타임스템프 값이 중복이 되면 조회할때마다 값이 변경될 수도 있기 때문이다.
        // 그 예로 같은 타임스템프를 가진 4명이 존재하는데 쿠폰이 3장이라면
        // 조회에 따라 사용자가 변경될 수 있는 문제가 생기고
        // sorted set은 set보다 속도가 느리기때문에 아무래도 set으로 풀수 있는 방법을 찾아야 한다.
        return redisTemplate.opsForZSet().addIfAbsent(key, value, score);
    }
}
