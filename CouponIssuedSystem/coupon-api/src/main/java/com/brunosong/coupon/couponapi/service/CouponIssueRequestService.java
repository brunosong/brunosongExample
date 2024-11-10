package com.brunosong.coupon.couponapi.service;

import com.brunosong.coupon.couponapi.controller.dto.CouponIssueRequestDto;
import com.brunosong.coupon.couponcore.service.CouponIssueService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class CouponIssueRequestService {

    private final CouponIssueService couponIssueService;

    public void issueRequestV1(CouponIssueRequestDto requestDto) {
        couponIssueService.issue(requestDto.couponId(), requestDto.userId());
        log.info("쿠폰 발급 완료. couponId : %s, userId : %s".formatted(requestDto.couponId(), requestDto.userId()));
    }
}
