package com.brunosong.coupon.couponcore.repository.mysql;

import com.brunosong.coupon.couponcore.model.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponJpaRepository extends JpaRepository<Coupon, Long> {
}
