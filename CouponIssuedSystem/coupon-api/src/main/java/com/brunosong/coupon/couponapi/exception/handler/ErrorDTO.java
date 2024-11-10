package com.brunosong.coupon.couponapi.exception.handler;

import com.brunosong.coupon.couponcore.exception.ErrorCode;

public record ErrorDTO(String message, ErrorCode errorCode) {
}
