package com.brunosong.coupon.couponapi.exception.handler;

import com.brunosong.coupon.couponapi.controller.dto.CouponIssueResponseDto;
import com.brunosong.coupon.couponcore.exception.CouponIssueException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class CouponExceptionHandler {

//    @ResponseBody
//    @ExceptionHandler(value = {CouponIssueException.class})
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    public ErrorDTO couponIssueException(CouponIssueException exception) {
//        return new ErrorDTO(exception.getMessage(), exception.getErrorCode());
//    }

    @ResponseBody
    @ExceptionHandler(value = {CouponIssueException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CouponIssueResponseDto couponIssueException(CouponIssueException exception) {
        return new CouponIssueResponseDto(false, exception.getErrorCode().message);
    }
}
