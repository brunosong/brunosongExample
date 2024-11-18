package com.brunosong.coupon.couponcore.repository.redis;

import com.brunosong.coupon.couponcore.exception.CouponIssueException;
import com.brunosong.coupon.couponcore.exception.ErrorCode;

public enum CouponIssueRequestCode {
    SUCCESS(1),
    DUPLICATED_COUPON_ISSUE(2),
    INVALID_COUPON_ISSUE_QUANTITY(3);

    int code;
    CouponIssueRequestCode(int code) {
        this.code = code;
    }

    public static CouponIssueRequestCode find(String code) {
        int codeValue = Integer.parseInt(code);
        if (codeValue == 1) return SUCCESS;
        if (codeValue == 2) return DUPLICATED_COUPON_ISSUE;
        if (codeValue == 3) return INVALID_COUPON_ISSUE_QUANTITY;
        throw new IllegalArgumentException("존재하지 않는 코드입니다. %s".formatted(code));
    }

    public static void checkRequestResult(CouponIssueRequestCode code) {
        if (code == INVALID_COUPON_ISSUE_QUANTITY) {
            throw new CouponIssueException("발급 가능한 수량을 초과합니다.",
                    ErrorCode.INVALID_COUPON_ISSUE_QUANTITY);
        }
        if (code == DUPLICATED_COUPON_ISSUE) {
            throw new CouponIssueException("이미 발급된 쿠폰 입니다.",
                    ErrorCode.DUPLICATED_COUPON_ISSUE);
        }
    }
}
