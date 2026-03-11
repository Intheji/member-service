package com.memberservice.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

// prod 프로필에서 사용 실제 s3 파일 저장소 구현
@Service
@Profile("prod")
@RequiredArgsConstructor
public class S3FileStorageService implements FileStorageService {

    private static final long EXPIRE_DAYS = 7L;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.region.static}")
    private String region;

    // 파일을 s3에 업로드하고 저장된 파일의 key를 반환
    @Override
    public String upload(Long memberId, MultipartFile file) {
        validateFile(file);

        String key = createKey(memberId, file.getOriginalFilename());

        try (S3Client s3Client = S3Client.builder()
                .region(Region.of(region))
                .build()) {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(key)
                    .contentType(file.getContentType())
                    .build();

            s3Client.putObject(
                    putObjectRequest,
                    RequestBody.fromBytes(file.getBytes())
            );

            return key;
        } catch (IOException e) {
            throw new RuntimeException("파일 업로드 중 오류가 발생했습니다.", e);
        }
    }

    // 저장된 s3 파일 키 기반으로 presigned url을 생성
    @Override
    public String generatePresignedUrl(String key) {
        try (S3Presigner presigner = S3Presigner.builder()
                .region(Region.of(region))
                .build()) {

            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucket)
                    .key(key)
                    .build();

            GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                    .signatureDuration(Duration.ofDays(EXPIRE_DAYS))
                    .getObjectRequest(getObjectRequest)
                    .build();

            return presigner.presignGetObject(presignRequest)
                    .url()
                    .toString();
        }
    }

    // 현재 시간 기준 7일 뒤 만료 시간
    @Override
    public LocalDateTime getExpirationTime() {
        return LocalDateTime.now().plusDays(EXPIRE_DAYS);
    }

    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("업로드할 파일이 비어 있습니다.");
        }
    }

    private String createKey(Long memberId, String originalFilename) {
        String safeFilename = originalFilename == null ? "unknown-file" : originalFilename;
        return "members/" + memberId + "/profile/" + UUID.randomUUID() + "_" + safeFilename;
    }
}
