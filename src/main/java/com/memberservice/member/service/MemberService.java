package com.memberservice.member.service;

import com.memberservice.member.dto.MemberCreateRequest;
import com.memberservice.member.dto.MemberResponse;
import com.memberservice.member.entity.Member;
import com.memberservice.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public MemberResponse create(MemberCreateRequest request) {
        Member member = new Member(request.name(), request.age(), request.mbti());
        Member saved = memberRepository.save(member);

        return new MemberResponse(
                saved.getId(),
                saved.getName(),
                saved.getAge(),
                saved.getMbti()
        );
    }

    public MemberResponse getById(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Member not found")
        );

        return new MemberResponse(
                member.getId(),
                member.getName(),
                member.getAge(),
                member.getMbti()
        );
    }
}
