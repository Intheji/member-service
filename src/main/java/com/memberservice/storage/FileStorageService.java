package com.memberservice.storage;

import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

// 파일 저장소에 대한 공통 인터페이스
public interface FileStorageService {

    // 파일 업로드 저장된 파일의 키를 반환
    String upload(Long memberId, MultipartFile file);

    // 저장된 파일 키를 기반으로 presigned url을 생성
    String generatePresignedUrl(String key);

    // presigned url 만료 시간
    LocalDateTime getExpirationTime();
}
