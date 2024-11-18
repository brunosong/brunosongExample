package com.brunosong.coupon.couponcore.service;

import com.brunosong.coupon.couponcore.TestConfig;
import com.brunosong.coupon.couponcore.exception.CouponIssueException;
import com.brunosong.coupon.couponcore.exception.ErrorCode;
import com.brunosong.coupon.couponcore.model.Coupon;
import com.brunosong.coupon.couponcore.model.CouponType;
import com.brunosong.coupon.couponcore.repository.mysql.CouponJpaRepository;
import com.brunosong.coupon.couponcore.repository.redis.dto.CouponIssueRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.stream.IntStream;

import static com.brunosong.coupon.couponcore.util.CouponRedisUtils.getIssueRequestKey;
import static com.brunosong.coupon.couponcore.util.CouponRedisUtils.getIssueRequestQueueKey;

class AsyncCouponIssueServiceV1Test extends TestConfig {

    @Autowired
    AsyncCouponIssueServiceV1 sut;

    @Autowired
    RedisTemplate<String,String> redisTemplate;

    @Autowired
    CouponJpaRepository couponJpaRepository;

    @BeforeEach
    void clear() {
        Collection<String> redisKeys = redisTemplate.keys("*");
        redisTemplate.delete(redisKeys);
    }

    @Test
    @DisplayName("쿠폰 발급 - 쿠폰이 존재하지 않는다면 예외를 반환한다" )
    void issue_1() {
        //given
        long couponId = 500;
        long userId = 500;

        // when
        CouponIssueException exception = Assertions.assertThrows(CouponIssueException.class, () -> {
            sut.issue(couponId, userId);
        });

        // then
        Assertions.assertEquals(exception.getErrorCode(), ErrorCode.COUPON_NOT_EXIST);
    }

    @Test
    @DisplayName("쿠폰 발급 - 발급 가능 수량이 존재하지 않는다면 예외를 반환한다." )
    void issue_2() {
        //given
        long userId = 500;

        Coupon coupon = Coupon.builder()
                .title("선착순 테스트 쿠폰")
                .couponType(CouponType.FIRST_COME_FIRST_SERVED)
                .totalQuantity(10)
                .issuedQuantity(0)
                .dateIssuedStart(LocalDateTime.now().minusDays(1))
                .dateIssuedEnd(LocalDateTime.now().plusDays(1))
                .build();
        couponJpaRepository.save(coupon);
        IntStream.range(0, coupon.getTotalQuantity()).forEach(idx -> {
            redisTemplate.opsForSet().add(getIssueRequestKey(coupon.getId()), String.valueOf(idx));
        });

        // when
        CouponIssueException exception = Assertions.assertThrows(CouponIssueException.class, () -> {
            sut.issue(coupon.getId(), userId);
        });

        // then
        Assertions.assertEquals(exception.getErrorCode(), ErrorCode.INVALID_COUPON_ISSUE_QUANTITY);
    }

    @Test
    @DisplayName("쿠폰 발급 - 이미 발급된 유저라면 예외를 반환한다" )
    void issue_3() {
        //given
        long userId = 500;

        Coupon coupon = Coupon.builder()
                .title("선착순 테스트 쿠폰")
                .couponType(CouponType.FIRST_COME_FIRST_SERVED)
                .totalQuantity(10)
                .issuedQuantity(0)
                .dateIssuedStart(LocalDateTime.now().minusDays(1))
                .dateIssuedEnd(LocalDateTime.now().plusDays(1))
                .build();
        couponJpaRepository.save(coupon);
        redisTemplate.opsForSet().add(getIssueRequestKey(coupon.getId()), String.valueOf(userId));

        // when
        CouponIssueException exception = Assertions.assertThrows(CouponIssueException.class, () -> {
            sut.issue(coupon.getId(), userId);
        });

        // then
        Assertions.assertEquals(exception.getErrorCode(), ErrorCode.DUPLICATED_COUPON_ISSUE);
    }

    @Test
    @DisplayName("쿠폰 발급 - 발급 기한이 유효하지 않다면 예외를 반환한다." )
    void issue_4() {
        //given
        long userId = 500;

        Coupon coupon = Coupon.builder()
                .title("선착순 테스트 쿠폰")
                .couponType(CouponType.FIRST_COME_FIRST_SERVED)
                .totalQuantity(10)
                .issuedQuantity(0)
                .dateIssuedStart(LocalDateTime.now().minusDays(2))
                .dateIssuedEnd(LocalDateTime.now().minusDays(1))
                .build();
        couponJpaRepository.save(coupon);
        redisTemplate.opsForSet().add(getIssueRequestKey(coupon.getId()), String.valueOf(userId));

        // when
        CouponIssueException exception = Assertions.assertThrows(CouponIssueException.class, () -> {
            sut.issue(coupon.getId(), userId);
        });

        // then
        Assertions.assertEquals(exception.getErrorCode(), ErrorCode.INVALID_COUPON_ISSUE_DATE);
    }

    @Test
    @DisplayName("쿠폰 발급 - 쿠폰 발급을 기록한다." )
    void issue_5() {
        //given
        long userId = 500;

        Coupon coupon = Coupon.builder()
                .title("선착순 테스트 쿠폰")
                .couponType(CouponType.FIRST_COME_FIRST_SERVED)
                .totalQuantity(10)
                .issuedQuantity(0)
                .dateIssuedStart(LocalDateTime.now().minusDays(2))
                .dateIssuedEnd(LocalDateTime.now().plusDays(1))
                .build();
        couponJpaRepository.save(coupon);

        // when
        sut.issue(coupon.getId(), userId);

        // then
        Boolean isSaved = redisTemplate.opsForSet().isMember(getIssueRequestKey(coupon.getId()), String.valueOf(userId));
        Assertions.assertTrue(isSaved);
    }

    @Test
    @DisplayName("쿠폰 발급 - 쿠폰 발급 요청이 성공하면 쿠폰 발급 큐에 적재된다." )
    void issue_6() throws JsonProcessingException {
        //given
        long userId = 500;

        Coupon coupon = Coupon.builder()
                .title("선착순 테스트 쿠폰")
                .couponType(CouponType.FIRST_COME_FIRST_SERVED)
                .totalQuantity(10)
                .issuedQuantity(0)
                .dateIssuedStart(LocalDateTime.now().minusDays(2))
                .dateIssuedEnd(LocalDateTime.now().plusDays(1))
                .build();
        couponJpaRepository.save(coupon);
        CouponIssueRequest request = new CouponIssueRequest(coupon.getId(), userId);

        // when
        sut.issue(coupon.getId(), userId);

        // then
        String savedIssueRequest = redisTemplate.opsForList().leftPop(getIssueRequestQueueKey());
        Assertions.assertEquals(new ObjectMapper().writeValueAsString(request), savedIssueRequest);
    }

}