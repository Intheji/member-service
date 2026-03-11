package com.memberservice.member.service;

import com.memberservice.common.exception.MemberNotFoundException;
import com.memberservice.common.exception.ProfileImageNotFoundException;
import com.memberservice.member.dto.ProfileImageResponse;
import com.memberservice.member.dto.ProfileImageUploadResponse;
import com.memberservice.member.entity.Member;
import com.memberservice.member.repository.MemberRepository;
import com.memberservice.storage.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberImageService {

    private final MemberRepository memberRepository;
    private final FileStorageService fileStorageService;

    // 팀원 프로필 이미지 업로드
    // memberId로 팀원 조회하고 -> 스토리지에 파일을 업로드 한 뒤에 업로드된 파일 키를 반환받는다 -> 업로드된 파일 키를 팀원 엔티티에 저장
    @Transactional
    public ProfileImageUploadResponse uploadProfileImage(Long memberId, MultipartFile file) {
        Member member = memberRepository.findById(memberId).orElseThrow(
                () -> new MemberNotFoundException(memberId)
        );

        String uploadedKey = fileStorageService.upload(memberId, file);
        member.updateProfileImageKey(uploadedKey);

        return new ProfileImageUploadResponse(member.getId(), uploadedKey);
    }

    // 팀원 프로필 이미지 조회용 Presigned URL을 생성
    // memberId로 팀원 조회 -> 프로필 이미지 키가 없으면 예외 -> 이미지 키가 있으면 presigned url과 만료 시간 반환
    public ProfileImageResponse getProfileImage(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(
                () -> new MemberNotFoundException(memberId)
        );

        String profileImageKey = member.getProfileImageKey();
        if (profileImageKey == null || profileImageKey.isBlank()) {
            throw new ProfileImageNotFoundException(memberId);
        }

        String presignedUrl = fileStorageService.generatePresignedUrl(profileImageKey);

        return new ProfileImageResponse(
                member.getId(),
                presignedUrl,
                fileStorageService.getExpirationTime()
        );
    }
}
