package com.brunosong.coupon.couponcore.repository.redis;

import com.brunosong.coupon.couponcore.exception.CouponIssueException;
import com.brunosong.coupon.couponcore.repository.redis.dto.CouponIssueRequest;
import com.brunosong.coupon.couponcore.util.CouponRedisUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.brunosong.coupon.couponcore.exception.ErrorCode.FAIL_COUPON_ISSUE_REQUEST;

@RequiredArgsConstructor
@Repository
public class RedisRepository {

    private final RedisTemplate<String,String> redisTemplate;
    private final RedisScript<String> issueScript = issueRequestScript();
    private final String issueRequestQueueKey = CouponRedisUtils.getIssueRequestQueueKey();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public Boolean zAdd(String key, String value, double score) {
        // addIfAbsent : 데이터가 중복되면 무시한다 옵션은 NX를 사용한다. ZADD NX
        // sorted set을 사용한다고 모든 문제가 해결되지 않는다. 그 이유는
        // 타임스템프 값이 중복이 되면 조회할때마다 값이 변경될 수도 있기 때문이다.
        // 그 예로 같은 타임스템프를 가진 4명이 존재하는데 쿠폰이 3장이라면
        // 조회에 따라 사용자가 변경될 수 있는 문제가 생기고
        // sorted set은 set보다 속도가 느리기때문에 아무래도 set으로 풀수 있는 방법을 찾아야 한다.
        return redisTemplate.opsForZSet().addIfAbsent(key, value, score);
    }

    public Long sAdd(String key, String value) {
        return redisTemplate.opsForSet().add(key,value);
    }

    public Long sCard(String key) {
        return redisTemplate.opsForSet().size(key);
    }

    public Boolean sIsMember(String key, String value) {
        return redisTemplate.opsForSet().isMember(key,value);
    }

    public Long rPush(String key, String value) {
        return redisTemplate.opsForList().rightPush(key, value);
    }

    public void issueRequest(long couponId, long userId, int totalIssueQuantity) {
        String issueRequestKey = CouponRedisUtils.getIssueRequestKey(couponId);
        CouponIssueRequest couponIssueRequest = new CouponIssueRequest(couponId,userId);
        try {
            String code = redisTemplate.execute(
                    issueScript,
                    List.of(issueRequestKey, issueRequestQueueKey),
                    String.valueOf(userId),
                    String.valueOf(totalIssueQuantity),
                    objectMapper.writeValueAsString(couponIssueRequest)
            );
            CouponIssueRequestCode.checkRequestResult(CouponIssueRequestCode.find(code));

        } catch (JsonProcessingException e) {
            throw new CouponIssueException( "input: %s".formatted(couponIssueRequest),
                    FAIL_COUPON_ISSUE_REQUEST);
        }
    }

    private RedisScript<String> issueRequestScript() {
        String script = """
                if redis.call('SISMEMBER', KEYS[1], ARGV[1]) == 1 then
                    return '2'
                end
                    
                if tonumber(ARGV[2]) > redis.call('SCARD', KEYS[1]) then
                    redis.call('SADD', KEYS[1], ARGV[1])
                    redis.call('RPUSH', KEYS[2], ARGV[3])
                    return '1'
                end
                                
                return '3'
                """;

        return RedisScript.of(script, String.class);
    }
}
