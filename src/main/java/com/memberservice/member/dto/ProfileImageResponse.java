package com.memberservice.member.dto;

import java.time.LocalDateTime;

public record ProfileImageResponse(
        Long memberId,
        String presignedUrl,
        LocalDateTime expiredAt
) {
}
