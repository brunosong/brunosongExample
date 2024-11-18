package com.brunosong.coupon.couponcore.service;

import com.brunosong.coupon.couponcore.component.DistributeLockExecutor;
import com.brunosong.coupon.couponcore.exception.CouponIssueException;
import com.brunosong.coupon.couponcore.repository.redis.RedisRepository;
import com.brunosong.coupon.couponcore.repository.redis.dto.CouponIssueRequest;
import com.brunosong.coupon.couponcore.repository.redis.dto.CouponRedisEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.brunosong.coupon.couponcore.exception.ErrorCode.FAIL_COUPON_ISSUE_REQUEST;
import static com.brunosong.coupon.couponcore.util.CouponRedisUtils.getIssueRequestKey;
import static com.brunosong.coupon.couponcore.util.CouponRedisUtils.getIssueRequestQueueKey;

@RequiredArgsConstructor
@Service
public class AsyncCouponIssueServiceV1 {

    private final RedisRepository redisRepository;
    private final CouponIssueRedisService couponIssueRedisService;
    private final DistributeLockExecutor distributeLockExecutor;
    private final CouponCacheService couponCacheService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Transactional
    public void issue(long couponId, long userId) {

        CouponRedisEntity coupon = couponCacheService.getCouponCache(couponId);
        coupon.checkIssuableCoupon();

        distributeLockExecutor.execute("lock %s".formatted(couponId), 3000, 3000, () -> {
            couponIssueRedisService.checkCouponIssueQuantity(coupon, userId);
            issueRequest(couponId, userId);
        });

    }

    private void issueRequest(long couponId, long userId) {
        CouponIssueRequest issueRequest = new CouponIssueRequest(couponId, userId);
        try {
            String value = objectMapper.writeValueAsString(issueRequest);
            redisRepository.sAdd(getIssueRequestKey(couponId), String.valueOf(userId));
            redisRepository.rPush(getIssueRequestQueueKey(), value);
        } catch (JsonProcessingException e) {
            throw new CouponIssueException( "input: %s".formatted(issueRequest),
                    FAIL_COUPON_ISSUE_REQUEST);
        }
    }


}
