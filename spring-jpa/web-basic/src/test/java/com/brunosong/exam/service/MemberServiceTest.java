package com.brunosong.exam.service;

import com.brunosong.exam.domain.Address;
import com.brunosong.exam.domain.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MemberServiceTest {

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