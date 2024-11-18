package com.brunosong.coupon.couponcore.repository.redis.dto;

import com.brunosong.coupon.couponcore.exception.CouponIssueException;
import com.brunosong.coupon.couponcore.model.Coupon;
import com.brunosong.coupon.couponcore.model.CouponType;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import java.time.LocalDateTime;

import static com.brunosong.coupon.couponcore.exception.ErrorCode.INVALID_COUPON_ISSUE_DATE;

public record CouponRedisEntity(
        Long id,
        CouponType couponType,
        Integer totalQuantity,

        @JsonSerialize(using =  LocalDateTimeSerializer.class)
        @JsonDeserialize(using = LocalDateTimeDeserializer.class)
        LocalDateTime dateIssuedStart,

        @JsonSerialize(using =  LocalDateTimeSerializer.class)
        @JsonDeserialize(using = LocalDateTimeDeserializer.class)
        LocalDateTime dateIssuedEnd
) {

    public CouponRedisEntity(Coupon coupon) {
        this(
                coupon.getId(),
                coupon.getCouponType(),
                coupon.getTotalQuantity(),
                coupon.getDateIssuedStart(),
                coupon.getDateIssuedEnd()
        );
    }

    private boolean availableIssueDate() {
        LocalDateTime now = LocalDateTime.now();
        return dateIssuedStart.isBefore(now) && dateIssuedEnd.isAfter(now);
    }

    public void checkIssuableCoupon() {
        if (!availableIssueDate()) {
            throw new CouponIssueException(("발급 가능한 일자가 아닙니다. couponId : %s, " +
                    "issuedStart: %s, issuedEnd: %s").formatted(id, dateIssuedStart, dateIssuedEnd),
                    INVALID_COUPON_ISSUE_DATE);
        }
    }
}
