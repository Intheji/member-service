package com.memberservice.member.dto;

public record MemberResponse(
        Long id,
        String name,
        int age,
        String mbti
) {

}
