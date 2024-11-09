package com.brunosong.coupon.couponcore.service;

import com.brunosong.coupon.couponcore.TestConfig;
import com.brunosong.coupon.couponcore.exception.CouponIssueException;
import com.brunosong.coupon.couponcore.exception.ErrorCode;
import com.brunosong.coupon.couponcore.model.Coupon;
import com.brunosong.coupon.couponcore.model.CouponIssue;
import com.brunosong.coupon.couponcore.model.CouponType;
import com.brunosong.coupon.couponcore.repository.mysql.CouponIssueJpaRepository;
import com.brunosong.coupon.couponcore.repository.mysql.CouponIssueRepository;
import com.brunosong.coupon.couponcore.repository.mysql.CouponJpaRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

import static com.brunosong.coupon.couponcore.exception.ErrorCode.*;
import static org.junit.jupiter.api.Assertions.*;


class CouponIssueServiceTest extends TestConfig {

    @Autowired
    CouponIssueService couponIssueService;

    @Autowired
    CouponIssueJpaRepository couponIssueJpaRepository;

    @Autowired
    CouponJpaRepository couponJpaRepository;

    @Autowired
    CouponIssueRepository couponIssueRepository;

    @BeforeEach
    void clean() {
        couponJpaRepository.deleteAllInBatch();
        couponIssueJpaRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("쿠펀 발급 내역이 존재하면 예외를 반환한다")
    void saveCouponIssue_1() {
        CouponIssue couponIssue = CouponIssue.builder()
                .couponId(1L)
                .userId(1L)
                .build();

        couponIssueJpaRepository.save(couponIssue);

        CouponIssueException exception = assertThrows(CouponIssueException.class, () -> {
            couponIssueService.saveCouponIssue(couponIssue.getCouponId(), couponIssue.getUserId());
        });

        Assertions.assertEquals(exception.getErrorCode(), DUPLICATED_COUPON_ISSUE);
    }

//    @Test
//    @DisplayName("쿠펀 발급 내역이 존재하지 않는다면 쿠폰을 발급한다")
//    void saveCouponIssue_2() {
//        long couponId = 1L;
//        long userId = 1L;
//        CouponIssue result = couponIssueService.saveCouponIssue(couponId, userId);
//
//        Assertions.assertTrue(couponIssueJpaRepository.findById(result.getCouponId()).isPresent());
//    }

    @Test
    @DisplayName("발급 수량, 기한, 중복 발급 문제가 없다면 쿠폰을 발급한다")
    void issue_1() {
        long userId = 1L;
        Coupon coupon = Coupon.builder()
                .couponType(CouponType.FIRST_COME_FIRST_SERVED)
                .title("선착순 테스트 쿠폰")
                .totalQuantity(100)
                .issuedQuantity(0)
                .dateIssuedStart(LocalDateTime.now().minusDays(1))
                .dateIssuedEnd(LocalDateTime.now().plusDays(1))
                .build();
        couponJpaRepository.save(coupon);

        // when
        couponIssueService.issue(coupon.getId(), userId);

        // then
        Coupon couponResult = couponJpaRepository.findById(coupon.getId()).get();
        Assertions.assertEquals(couponResult.getIssuedQuantity(), 1);

        CouponIssue couponIssueResult = couponIssueRepository.findFirstCouponIssue(coupon.getId(), userId);
        Assertions.assertNotNull(couponIssueResult);
    }

    @Test
    @DisplayName("발급 수량에 문제가 있다면 예외를 반환한다")
    void issue_2() {
        long userId = 1L;
        Coupon coupon = Coupon.builder()
                .couponType(CouponType.FIRST_COME_FIRST_SERVED)
                .title("선착순 테스트 쿠폰")
                .totalQuantity(100)
                .issuedQuantity(100)
                .dateIssuedStart(LocalDateTime.now().minusDays(1))
                .dateIssuedEnd(LocalDateTime.now().plusDays(1))
                .build();
        couponJpaRepository.save(coupon);

        // when
        CouponIssueException exception = assertThrows(CouponIssueException.class, () -> {
            couponIssueService.issue(coupon.getId(), userId);
        });

        // then
        Assertions.assertEquals(exception.getErrorCode(), INVALID_COUPON_ISSUE_QUANTITY);
    }

    @Test
    @DisplayName("발급 기한에 문제가 있다면 예외를 반환한다")
    void issue_3() {
        long userId = 1L;
        Coupon coupon = Coupon.builder()
                .couponType(CouponType.FIRST_COME_FIRST_SERVED)
                .title("선착순 테스트 쿠폰")
                .totalQuantity(100)
                .issuedQuantity(0)
                .dateIssuedStart(LocalDateTime.now().minusDays(2))
                .dateIssuedEnd(LocalDateTime.now().minusDays(1))
                .build();
        couponJpaRepository.save(coupon);

        // when
        CouponIssueException exception = assertThrows(CouponIssueException.class, () -> {
            couponIssueService.issue(coupon.getId(), userId);
        });

        // then
        Assertions.assertEquals(exception.getErrorCode(), INVALID_COUPON_ISSUE_DATE);
    }

    @Test
    @DisplayName("중복 발급 검증에 문제가 있다면 예외를 반환한다")
    void issue_4() {
        long userId = 1L;
        Coupon coupon = Coupon.builder()
                .couponType(CouponType.FIRST_COME_FIRST_SERVED)
                .title("선착순 테스트 쿠폰")
                .totalQuantity(100)
                .issuedQuantity(0)
                .dateIssuedStart(LocalDateTime.now().minusDays(1))
                .dateIssuedEnd(LocalDateTime.now().plusDays(1))
                .build();
        couponJpaRepository.save(coupon);

        CouponIssue couponIssue = CouponIssue.builder()
                .couponId(coupon.getId())
                .userId(userId)
                .build();
        couponIssueJpaRepository.save(couponIssue);

        // when
        CouponIssueException exception = assertThrows(CouponIssueException.class, () -> {
            couponIssueService.issue(coupon.getId(), userId);
        });

        // then
        Assertions.assertEquals(exception.getErrorCode(), DUPLICATED_COUPON_ISSUE);
    }

    @Test
    @DisplayName("쿠폰이 존재하지 않는다면 예외를 반환한다")
    void issue_5() {
        long userId = 1L;
        long couponId = 1L;

        // when
        CouponIssueException exception = assertThrows(CouponIssueException.class, () -> {
            couponIssueService.issue(couponId, userId);
        });

        // then
        Assertions.assertEquals(exception.getErrorCode(), COUPON_NOT_EXIST);
    }

}