package com.brunosong.coupon.couponcore.exception;

import lombok.Getter;

@Getter
public class CouponIssueException extends RuntimeException {
    private final ErrorCode errorCode;
    private final String message;

    public CouponIssueException(String message, ErrorCode errorCode) {
        this.errorCode = errorCode;
        this.message = message;
    }

    @Override
    public String getMessage() {
        return "[%s] %s".formatted(errorCode, message);
    }

}
