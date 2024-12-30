package com.brunosong.exam.service;

import com.brunosong.exam.domain.Member;
import com.brunosong.exam.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public Long join(Member member) {
        validateDuplicateMember(member.getName());
        memberRepository.save(member);
        return member.getId();
    }

    public Member findOne(Long memberId) {
        return memberRepository.findById(memberId);
    }

    private void validateDuplicateMember(String name) {
        List<Member> members = memberRepository.findByName(name);
        if (!members.isEmpty()) {
            throw new IllegalArgumentException("같은 이름에 회원이 존재합니다.");
        }
    }

}
