package com.brunosong.coupon.couponcore.model;

import com.brunosong.coupon.couponcore.exception.CouponIssueException;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static com.brunosong.coupon.couponcore.exception.ErrorCode.*;

@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "coupons")
public class Coupon  extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private CouponType couponType;

    private Integer totalQuantity;

    @Column(nullable = false)
    private int issuedQuantity;

    @Column(nullable = false)
    private int discountAmount;

    @Column(nullable = false)
    private int minAvailableAmount;

    @Column(nullable = false)
    private LocalDateTime dateIssuedStart;

    @Column(nullable = false)
    private LocalDateTime dateIssuedEnd;

    public boolean availableIssueQuantity() {
        if (totalQuantity == null) {
            return true;
        }
        return totalQuantity > issuedQuantity;
    }

    public boolean availableIssueDate() {
        LocalDateTime now = LocalDateTime.now();
        return dateIssuedStart.isBefore(now) && dateIssuedEnd.isAfter(now);
    }

    public void issue() {
        if (!availableIssueQuantity()) {
            throw new CouponIssueException(("발급 가능한 수량을 초과 합니다. total : %s, " +
                    "issued: %s").formatted(totalQuantity,issuedQuantity),
                    INVALID_COUPON_ISSUE_QUANTITY);
        }
        if (!availableIssueDate()) {
            throw new CouponIssueException(("발급 가능한 일자가 아닙니다. request : %s, " +
                    "issuedStart: %s, issuedEnd: %s").formatted(LocalDateTime.now(),dateIssuedStart, dateIssuedEnd),
                    INVALID_COUPON_ISSUE_DATE);
        }
        issuedQuantity++;
    }
}
