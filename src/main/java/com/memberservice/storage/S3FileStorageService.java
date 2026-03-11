package com.memberservice.storage;

import com.memberservice.common.exception.FileUploadFailException;
import com.memberservice.common.exception.InvalidFileException;
import io.awspring.cloud.s3.S3Template;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

// prod 프로필에서 사용 실제 s3 파일 저장소 구현
@Service
@Profile("prod")
@RequiredArgsConstructor
public class S3FileStorageService implements FileStorageService {

    private static final Duration PRESIGNED_URL_EXPIRATION = Duration.ofDays(7);
    private static final String DEFAULT_FILE_NAME = "unknown-file";

    private final S3Template s3Template;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Override
    public String upload(Long memberId, MultipartFile file) {
        // 업로드 가능한 파일인지 검증
        validateFile(file);

        // 회원별 프로필 이미지 key 생성
        String key = createKey(memberId, file.getOriginalFilename());

        try {
            // S3에 파일 업로드
            s3Template.upload(bucket, key, file.getInputStream());
            return key;
        } catch (IOException e) {
            throw new FileUploadFailException("파일 업로드에 실패했습니다.");
        }
    }

    @Override
    public String generatePresignedUrl(String key) {
        // 일정 시간 동안만 접근 가능한 다운로드 URL 생성
        URL signedUrl = s3Template.createSignedGetURL(bucket, key, PRESIGNED_URL_EXPIRATION);
        return signedUrl.toString();
    }

    @Override
    public LocalDateTime getExpirationTime() {
        // presigned URL 만료 시각 반환
        return LocalDateTime.now().plus(PRESIGNED_URL_EXPIRATION);
    }

    private void validateFile(MultipartFile file) {
        // 파일이 없거나 비어 있으면 예외 처리
        if (file == null || file.isEmpty()) {
            throw new InvalidFileException("업로드할 파일이 비어 있습니다.");
        }
    }

    private String createKey(Long memberId, String originalFilename) {
        // 원본 파일명이 없으면 기본 이름 사용
        String fileName = (originalFilename == null || originalFilename.isBlank())
                ? DEFAULT_FILE_NAME
                : originalFilename.trim().replace(" ", "_");

        // S3에 저장될 최종 경로 생성
        return "members/" + memberId + "/profile/" + UUID.randomUUID() + "_" + fileName;
    }
}
