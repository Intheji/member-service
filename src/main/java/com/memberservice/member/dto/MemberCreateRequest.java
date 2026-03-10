package com.memberservice.member.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record MemberCreateRequest(
        @NotBlank(message = "이름은 필수입니다.")
        String name,

        @Min(value = 1, message = "나이는 1 이상이어야 합니다.")
        @Max(value = 150, message = "나이는 150 이하여야 합니다.")
        int age,

        @NotBlank(message = "MBTI는 필수입니다.")
        String mbti
) {
}
