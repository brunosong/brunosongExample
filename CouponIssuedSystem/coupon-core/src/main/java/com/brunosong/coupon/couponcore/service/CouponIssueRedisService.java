package com.brunosong.coupon.couponcore.service;

import com.brunosong.coupon.couponcore.exception.CouponIssueException;
import com.brunosong.coupon.couponcore.repository.redis.RedisRepository;
import com.brunosong.coupon.couponcore.repository.redis.dto.CouponRedisEntity;
import com.brunosong.coupon.couponcore.util.CouponRedisUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.brunosong.coupon.couponcore.exception.ErrorCode.DUPLICATED_COUPON_ISSUE;
import static com.brunosong.coupon.couponcore.exception.ErrorCode.INVALID_COUPON_ISSUE_QUANTITY;

@RequiredArgsConstructor
@Service
public class CouponIssueRedisService {

    private final RedisRepository redisRepository;

    public void checkCouponIssueQuantity(CouponRedisEntity couponRedisEntity, long userId) {
        if (!availableTotalIssueQuantity(couponRedisEntity.totalQuantity(), couponRedisEntity.id())) {
            throw new CouponIssueException("발급 가능한 수량을 초과합니다. couponId : %s, userId : %s"
                    .formatted(couponRedisEntity.id(), userId),
                    INVALID_COUPON_ISSUE_QUANTITY);
        }
        if (!availableUserIssueQuantity(couponRedisEntity.id(), userId)) {
            throw new CouponIssueException("이미 발급된 쿠폰 입니다. couponId : %s, userId : %s"
                    .formatted(couponRedisEntity.id(), userId),
                    DUPLICATED_COUPON_ISSUE);
        }
    }

    public boolean availableTotalIssueQuantity(Integer totalQuantity, long couponId) {
        if (totalQuantity == null) {
            return true;
        }
        String key = CouponRedisUtils.getIssueRequestKey(couponId);
        return totalQuantity > redisRepository.sCard(key);
    }

    public boolean availableUserIssueQuantity(long couponId, long userId) {
        String key = CouponRedisUtils.getIssueRequestKey(couponId);
        return !redisRepository.sIsMember(key, String.valueOf(userId));
    }
}
