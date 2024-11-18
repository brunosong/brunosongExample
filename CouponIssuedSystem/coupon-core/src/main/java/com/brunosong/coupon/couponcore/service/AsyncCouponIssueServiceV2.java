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
public class AsyncCouponIssueServiceV2 {

    private final RedisRepository redisRepository;
    private final CouponCacheService couponCacheService;

    @Transactional
    public void issue(long couponId, long userId) {
        CouponRedisEntity coupon = couponCacheService.getCouponCache(couponId);
        coupon.checkIssuableCoupon();
        issueRequest(couponId, userId, coupon.totalQuantity());
    }

    private void issueRequest(long couponId, long userId, Integer totalIssueQuantity) {
        if (totalIssueQuantity == null) {
            redisRepository.issueRequest(couponId, userId, Integer.MAX_VALUE);
        }
        redisRepository.issueRequest(couponId,userId, totalIssueQuantity);
    }


}
