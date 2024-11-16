package com.brunosong.coupon.couponapi.service;

import com.brunosong.coupon.couponapi.controller.dto.CouponIssueRequestDto;
import com.brunosong.coupon.couponcore.component.DistributeLockExecutor;
import com.brunosong.coupon.couponcore.service.AsyncCouponIssueServiceV1;
import com.brunosong.coupon.couponcore.service.CouponIssueService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class CouponIssueRequestService {

    private final CouponIssueService couponIssueService;
    private final DistributeLockExecutor distributeLockExecutor;
    private final AsyncCouponIssueServiceV1 asyncCouponIssueServiceV1;

    public void issueRequestV1(CouponIssueRequestDto requestDto) {
//        distributeLockExecutor.execute("lock_" + requestDto.couponId(),10000,10000,
//                                        () -> couponIssueService.issue(requestDto.couponId(), requestDto.userId()) );
        couponIssueService.issue(requestDto.couponId(), requestDto.userId());
        log.info("쿠폰 발급 완료. couponId : %s, userId : %s".formatted(requestDto.couponId(), requestDto.userId()));
    }

    public void asyncIssueRequestV1(CouponIssueRequestDto requestDto) {
        asyncCouponIssueServiceV1.issue(requestDto.couponId(), requestDto.userId());
        log.info("쿠폰 발급 완료. couponId : %s, userId : %s".formatted(requestDto.couponId(), requestDto.userId()));
    }
}
