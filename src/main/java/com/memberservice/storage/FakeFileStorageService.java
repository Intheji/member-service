package com.memberservice.storage;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.UUID;

// 로컬에서 사용함 로컬에서 테스트하려고 만듦
@Service
@Profile("local")
public class FakeFileStorageService implements FileStorageService {

    private static final long EXPIRE_DAYS = 7L;

    // 실제 업로드 안 하고 업로드 된 것처럼 보이게 만드는 키 생성해서 반환
    @Override
    public String upload(Long memberId, MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        String safeFilename = originalFilename == null ? "unknown-file" : originalFilename;

        return "members/" + memberId + "/profile/" + UUID.randomUUID() + "_" + safeFilename;
    }

    // 로컬 테스트용 가짜 url 반환
    @Override
    public String generatePresignedUrl(String key) {
        return "https://fake-s3.local/" + key + "?signature=fake-signature";
    }

    // 7일 뒤 만료 시간으로 반환
    @Override
    public LocalDateTime getExpirationTime() {
        return LocalDateTime.now().plusDays(EXPIRE_DAYS);
    }
}
