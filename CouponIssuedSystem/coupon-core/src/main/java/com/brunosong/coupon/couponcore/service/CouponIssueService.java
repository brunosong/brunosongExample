package com.brunosong.coupon.couponcore.service;

import com.brunosong.coupon.couponcore.exception.CouponIssueException;
import com.brunosong.coupon.couponcore.exception.ErrorCode;
import com.brunosong.coupon.couponcore.model.Coupon;
import com.brunosong.coupon.couponcore.model.CouponIssue;
import com.brunosong.coupon.couponcore.repository.mysql.CouponIssueJpaRepository;
import com.brunosong.coupon.couponcore.repository.mysql.CouponIssueRepository;
import com.brunosong.coupon.couponcore.repository.mysql.CouponJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.brunosong.coupon.couponcore.exception.ErrorCode.COUPON_NOT_EXIST;
import static com.brunosong.coupon.couponcore.exception.ErrorCode.DUPLICATED_COUPON_ISSUE;

@Service
@RequiredArgsConstructor
public class CouponIssueService {

    private final CouponJpaRepository couponJpaRepository;
    private final CouponIssueJpaRepository couponIssueJpaRepository;
    private final CouponIssueRepository couponIssueRepository;

    @Transactional
    public void issue(long couponId, long userId) {
        Coupon coupon = findCoupon(couponId);
        coupon.issue();
        saveCouponIssue(couponId, userId);
    }

    @Transactional(readOnly = true)
    public Coupon findCoupon(long couponId) {
        return couponJpaRepository.findById(couponId).orElseThrow(() -> {
            throw new CouponIssueException("쿠폰 정책이 존재하지 않습니다. %s".formatted(couponId), COUPON_NOT_EXIST);
        });
    }

    @Transactional
    public CouponIssue saveCouponIssue(long couponId, long userId) {
        checkAlreadyIssuance(couponId,userId);
        CouponIssue couponIssue = CouponIssue.builder()
                .couponId(couponId)
                .userId(userId)
                .build();

        return couponIssueJpaRepository.save(couponIssue);
    }

    private void checkAlreadyIssuance(long couponId, long userId) {
        CouponIssue issue = couponIssueRepository.findFirstCouponIssue(couponId, userId);
        if (issue != null) {
            throw new CouponIssueException("이미 발급된 쿠폰입니다. userId : %s, couponId : %s".formatted(userId,couponId),
                    DUPLICATED_COUPON_ISSUE);
        }
    }
}