package com.memberservice.member.controller;

import com.memberservice.member.dto.MemberCreateRequest;
import com.memberservice.member.dto.MemberResponse;
import com.memberservice.member.dto.ProfileImageResponse;
import com.memberservice.member.dto.ProfileImageUploadResponse;
import com.memberservice.member.service.MemberImageService;
import com.memberservice.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;
    private final MemberImageService memberImageService;

    // 팀원 정보 저장 API
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MemberResponse createMember(@Valid @RequestBody MemberCreateRequest request) {
        return memberService.create(request);
    }

    // 팀원 단건 조회 API
    @GetMapping("/{id}")
    public MemberResponse getMemberById(@PathVariable Long id) {
        return memberService.getById(id);
    }


    // 팀원 프로필 이미지 업로드 API
    @PostMapping("/{id}/profile-image")
    public ProfileImageUploadResponse uploadProfileImage(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file
    ) {
        return memberImageService.uploadProfileImage(id, file);
    }

    // 팀원 프로필 이미지 조회 API
    @GetMapping("/{id}/profile-image")
    public ProfileImageResponse getProfileImage(@PathVariable Long id) {
        return memberImageService.getProfileImage(id);
    }
}
