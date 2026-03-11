package com.memberservice.member.dto;

public record ProfileImageUploadResponse(
        Long memberId,
        String profileImageKey
) {
}
