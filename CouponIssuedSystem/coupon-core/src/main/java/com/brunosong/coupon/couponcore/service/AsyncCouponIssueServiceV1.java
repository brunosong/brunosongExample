package com.brunosong.coupon.couponcore.service;

import com.brunosong.coupon.couponcore.component.DistributeLockExecutor;
import com.brunosong.coupon.couponcore.exception.CouponIssueException;
import com.brunosong.coupon.couponcore.model.Coupon;
import com.brunosong.coupon.couponcore.repository.redis.RedisRepository;
import com.brunosong.coupon.couponcore.repository.redis.dto.CouponIssueRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.brunosong.coupon.couponcore.exception.ErrorCode.*;
import static com.brunosong.coupon.couponcore.util.CouponRedisUtils.getIssueRequestKey;
import static com.brunosong.coupon.couponcore.util.CouponRedisUtils.getIssueRequestQueueKey;

@RequiredArgsConstructor
@Service
public class AsyncCouponIssueServiceV1 {

    private final RedisRepository redisRepository;
    private final CouponIssueRedisService couponIssueRedisService;
    private final CouponIssueService couponIssueService;
    private final DistributeLockExecutor distributeLockExecutor;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Transactional
    public void issue(long couponId, long userId) {

        Coupon coupon = couponIssueService.findCoupon(couponId);
        if (!coupon.availableIssueDate()) {
            throw new CouponIssueException("발급 가능한 일자가 아닙니다. couponId: %s, userId: %s, issueStart: %s, issueEnd: %s"
                                        .formatted(couponId, userId,coupon.getDateIssuedStart(), coupon.getDateIssuedEnd()),
                    INVALID_COUPON_ISSUE_DATE);
        }
        distributeLockExecutor.execute("lock %s".formatted(couponId), 3000, 3000, () -> {
            if (!couponIssueRedisService.availableTotalIssueQuantity(coupon.getTotalQuantity(),couponId)) {
                throw new CouponIssueException("발급 가능한 수량을 초과합니다. couponId : %s, userId : %s".formatted(couponId, userId),
                        INVALID_COUPON_ISSUE_QUANTITY);
            }
            if (!couponIssueRedisService.availableUserIssueQuantity(couponId, userId)) {
                throw new CouponIssueException("이미 발급된 쿠폰 입니다. couponId : %s, userId : %s".formatted(couponId, userId),
                        DUPLICATED_COUPON_ISSUE);
            }
        });

        issueRequest(couponId, userId);
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
