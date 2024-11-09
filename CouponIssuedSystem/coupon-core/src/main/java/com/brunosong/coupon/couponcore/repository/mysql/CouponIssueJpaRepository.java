package com.brunosong.coupon.couponcore.repository.mysql;

import com.brunosong.coupon.couponcore.model.CouponIssue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponIssueJpaRepository extends JpaRepository<CouponIssue, Long> {
}
