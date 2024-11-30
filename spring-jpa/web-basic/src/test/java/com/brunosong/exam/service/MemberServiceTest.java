package com.brunosong.exam.service;

import com.brunosong.exam.domain.Address;
import com.brunosong.exam.domain.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
@SpringBootTest
@Transactional
class MemberServiceTest {

    @BeforeEach
    void clear() {

    }

    @Autowired
    MemberService memberService;

    @Test
    void join() {
        Member member = Member.builder()
                .name("송호식")
                .address(new Address("서울", "관악", "구로"))
                .build();

        Long join = memberService.join(member);

        Assertions.assertThat(join).isEqualTo(1L);
    }

    @Test
    @DisplayName("중복회원 이름 저장시 예외가 발생한다")
    void join2() {
        Member member = Member.builder()
                .name("송호식")
                .address(new Address("서울", "관악", "구로"))
                .build();

        Long join = memberService.join(member);

        Member member2 = Member.builder()
                .name("송호식")
                .address(new Address("부산", "관악", "구로"))
                .build();

        assertThrows(IllegalArgumentException.class,() -> memberService.join(member2));
    }

}