# 📌 Team Member Service - Cloud Native Architecture

Spring Boot 기반 팀원 소개 서비스를 AWS 인프라 위에 구축한 프로젝트입니다. 단일 서버 구조가 아닌 Stateless 아키텍처를 기반으로, DB / 파일 저장소 / 서버를 분리하고  
Docker, CI/CD, Auto Scaling, HTTPS, CDN를 적용한 클라우드 네이티브 서비스입니다.

  ---

# 1. 프로젝트 소개

## 1-1. 프로젝트 개요
아무것도 없는 상태에서 네트워크를 직접 구축하고, DB와 파일 저장소를 분리하여 서버가 죽어도 데이터가 안전한 Stateless 아키텍처를 구현한 프로젝트입니다.

## 1-2. 프로젝트 목표
- AWS 인프라를 직접 설계하고 구축한다.
- Spring Boot 애플리케이션을 운영 가능한 형태로 배포한다.
- 서버, DB, 파일 저장소를 분리하여 Stateless 구조를 이해한다.
- Docker 및 CI/CD를 통해 배포 자동화를 경험한다.
- ALB + ASG + HTTPS + CloudFront를 활용하여 고가용성과 보안을 강화한다.

  ---

# 2. 기술 스택

## Backend
- Java 17
- Spring Boot
- Spring Data JPA
- MySQL
- H2 Database
- Spring Boot Actuator

## Infra / Cloud
- AWS VPC
- AWS EC2
- AWS RDS (MySQL)
- AWS S3
- AWS Systems Manager Parameter Store
- AWS ALB
- AWS Auto Scaling Group
- AWS ACM
- AWS Route 53
- AWS CloudFront
- AWS NAT Gateway

## DevOps
- Docker
- Docker Hub
- GitHub Actions

  ---

# 3. 전체 아키텍처

  ```text
  Client
    │
    ▼
  CloudFront
    │
    ▼
  Route 53
    │
    ▼
  ALB (HTTPS)
    │
    ▼
  Auto Scaling Group
    │
    ▼
  EC2 (Spring Boot + Docker)
    │
    ├── RDS (MySQL)
    ├── S3 (Profile Image Storage)
    └── Parameter Store (Secrets / Config)
  ```

# 4. 주요 기능

## 4-1. 팀원 정보 저장 API
- `POST /api/members`
- 팀원의 이름, 나이, MBTI를 JSON으로 받아 저장

## 4-2. 팀원 정보 조회 API
- `GET /api/members/{id}`
- 저장된 팀원 정보를 조회

## 4-3. 프로필 이미지 업로드
- `POST /api/members/{id}/profile-image`
- `MultipartFile` 이미지를 S3에 업로드하고 DB에 이미지 경로 저장

## 4-4. 프로필 이미지 조회
- `GET /api/members/{id}/profile-image`
- Presigned URL을 생성하여 반환
- Presigned URL 유효기간: 7일

## 4-5. 상태 확인
- `GET /actuator/health`
- 애플리케이션 상태 확인

## 4-6. 운영 정보 확인
- `GET /actuator/info`
- Parameter Store에 저장한 값(예: team-name) 확인

  ---

# 5. 환경 분리

## local
- H2 Database 사용
- 로컬 개발용 프로필

## prod
- MySQL(RDS) 사용
- 운영 배포용 프로필

  ---

# 6. 로그 전략
- API 요청 시 INFO 레벨 로그 출력
- 예외 발생 시 ERROR 레벨 로그 및 스택트레이스 출력

**예시**
  ```text
  [API - LOG] POST /api/members
  [API - LOG] GET /api/members/1
  ```

  ---

# 7. API 명세

## 7-1. 팀원 생성
**Request**
- Method: `POST`
- URL: `/api/members`



## 7-2. 팀원 조회
**Request**
- Method: `GET`
- URL: `/api/members/{id}`


## 7-3. 프로필 이미지 업로드
**Request**
- Method: `POST`
- URL: `/api/members/{id}/profile-image`
- Content-Type: `multipart/form-data`

**Response Example**
  ```json
  {
    "message": "프로필 이미지 업로드 완료"
  }
  ```

## 7-4. 프로필 이미지 조회
**Request**
- Method: `GET`
- URL: `/api/members/{id}/profile-image`

**Response Example**
  ```json
  {
    "presignedUrl": "https://example-presigned-url",
    "expiresAt": "2026-03-20T12:00:00"
  }
  ```

  ---

# 8. 단계별 구현 내용

## LV 0 - AWS Budget 설정
클라우드 실습 중 비용 사고를 방지하기 위해 AWS Budget을 설정했습니다.

**설정 내용**
- 월 예산: `$100`
- 80% 도달 시 이메일 알림 설정

**AWS Budget 설정 화면**
<img width="1665" height="930" alt="스크린샷 2026-03-10 122425" src="https://github.com/user-attachments/assets/c37c06a9-f73e-4b63-b1aa-8f5b8d6de914" />

  ---

## LV 1 - 네트워크 구축 및 핵심 기능 배포
안전한 네트워크 환경을 만들고, 운영 가능한 상태의 애플리케이션을 배포했습니다.

**구현 내용**
- VPC 생성
- Public / Private Subnet 분리
- Public Subnet에 EC2 생성
- Spring Boot 애플리케이션 배포
- H2 / MySQL 프로필 분리
- Actuator Health 엔드포인트 노출

**Health Check URL**
- `http://{EC2_PUBLIC_IP}:8080/actuator/health`


**설정 완료된 EC2의 퍼블릭 IP**
- 54.180.134.95

  ---

## LV 2 - DB 분리 및 보안 연결
RDS와 Parameter Store를 활용하여 운영 환경의 DB 연결을 안전하게 구성했습니다.

**구현 내용**
- Public Subnet에 MySQL RDS 생성
- EC2 ↔ RDS 보안 그룹 체이닝 구성
- Parameter Store에 DB 접속 정보 저장
- Parameter Store의 `team-name` 값을 `/actuator/info`에 노출

**Parameter Store 예시**
- `/member-service/prod/db/url`
- `/member-service/prod/db/username`
- `/member-service/prod/db/password`
- `/member-service/prod/team-name`

**actuator info URL**
- `http://54.180.134.95:8080/actuator/info`

**응답 예시**
  ```json
  {
    "team-name": "YOUR_TEAM_NAME"
  }
  ```

**제출 항목**
- actuator info url: http://54.180.134.95:8080/actuator/info
<img width="245" height="127" alt="스크린샷 2026-03-11 171716" src="https://github.com/user-attachments/assets/d0561cf5-ee07-4cef-a825-3bd01c0cee24" />

- RDS 보안 그룹 인바운드 규칙
<img width="2236" height="425" alt="image" src="https://github.com/user-attachments/assets/1287e636-f981-4945-90b6-72538fe5fb20" />

  ---

## LV 3 - 프로필 사진 기능 추가와 권한 관리
서버 디스크가 아닌 S3에 이미지를 저장하도록 구성했습니다.

**구현 내용**
- S3 버킷 생성
- 모든 퍼블릭 액세스 차단 활성화
- IAM Role 또는 IAM Policy를 통해 EC2에 S3 접근 권한 부여
- MultipartFile 업로드 API 구현
- Presigned URL 생성 API 구현
- Presigned URL 유효기간 7일 설정

**S3 버킷 정책 방향**
- 버킷 퍼블릭 공개 금지
- 애플리케이션은 IAM Role 기반 접근
- 사용자 다운로드는 Presigned URL로만 허용

**제출 항목**
- [Presigned URL](https://member-service-profile.s3.ap-northeast-2.amazonaws.com/members/3/profile/fd0af6d1-2402-42c9-bb8a-1fd029c98edf_KoAsXaqnGdy8MccsH5aWYVwkzy-jf4oNrHp-wyhv9Rx5B5qRL_9bjykEc9JASXcB90f5NyQ9llkHE9FUfAxEIA.webp?X-Amz-Security-Token=IQoJb3JpZ2luX2VjELH%2F%2F%2F%2F%2F%2F%2F%2F%2F%2FwEaDmFwLW5vcnRoZWFzdC0yIkcwRQIgfaFrncGMAkpg%2Fg%2BW1Mm6%2F8yf3b%2BgiV%2BsgOKdv1wp1N8CIQCVkcQCJ46feU%2FUDMBiX%2FaOCEnPLHVJNTt4hNv8yYoTASrKBQh6EAAaDDE1NTMyNjA0OTQ1OSIMU0Q8siMmjtoZ2YXwKqcFz%2B55nMM6J7uNXfycJxUApMuG7dWPY5TcNpLEX6Go%2Fnj7GbBk0M2wATWIbT4g4sj1pRSG5H%2F2sHbLef4LGSFpvdrpFIUpJy74fM22vH0v89xwq%2Bn9Uc8ZCuUDGo1YuuO%2FAeFKO1c0l99Rfglh363MB%2BmdXXwQdautnTdZsEUGyWthRkoAxhHgLnxUKlEpG2HwMFefnWc4kEp8blgdVR0HsN7dZ0o2J6RqjrUwuL9Lh76T1gjY4Nbn82EKJMY4ev5QP2OoC1B6GPI1mASO8Wk8%2Fq8486OFdhQL6K6aDOhaRe2xj9oSJ73BeHLz2vdSkkpAoEsOw64nNAqRMogXCUzcQumhwi4fQ34pL19jBDhhvzvzTXd6LYwhGkeP%2BteA4IpLlZJcVoj%2B%2FivkEXlEEnJpJP874TU%2BVc8hu5veRP3F26gABTZbvf4ncRhMDFGUnwy4m7B9kaOS7AfbcwqLZXaWxislVxIZpPpzvzCCi3fWqtpbKirO9hWgNO8%2FOeiWKFBrv8HfoQmXM%2FVLqLzMjymcs0k3xwwFnsq%2FYLaYEyiLuVH3TrjEAS45pOIxbEAGXZtqaKL79MsnQDJDx6s5oS%2B1uso0g74XZFoFsgw%2Bv9zl3gyST%2FNAxeVidBWAGoMDY0UeiB183I%2BYyRuaPMPgTTIKjexB19oY0jrNfLGl%2FhlSWsMTabrq9641sGx5CpC%2BTUF3hKZOk0JSJ8DEdS033NuKYhuky%2B1WpRl%2FhVxjwLhq5%2F3rvkoTwKLoxBfthcmLOYoXIfMUXkRir5RqjwzeDWUYAJfatOTf1TiEkgduUW0SX8GT%2FnqKRuXLQNnK3Q6VOspV%2B%2BWfoUTo0xeyieExTxxKNns8KnyYS62YkgiflgO2%2B1wT%2F%2BShpi78sIxsF0GbDHqTsZWAgBWWTjCd1cvNBjqxAfSbQNBrIVFRptlJzOIsbfkDnm3DYT%2FbvyCQqlFJvZdrRlLlxNlUxgHxKRZvmX1KSDfLLBJGNDECDASyM1xJ2nZ6%2FnYZvqwmK7Ggu%2BSbOaWE%2Fsf%2BO4B%2FeuR3Et06NYQUAlzK2MXXaaNRFac4UCJ%2BnI78vAzHI9iFq7Zpe5e03n4OKV9MgmcR64Xw9UvQEFaWFghqZclsl3CNw9%2FkMdImzknLSd1WDEJTTIvLnb6o5GZPvQ%3D%3D&X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Date=20260312T170302Z&X-Amz-SignedHeaders=host&X-Amz-Credential=ASIASIKRHNSZ6QFHB6VA%2F20260312%2Fap-northeast-2%2Fs3%2Faws4_request&X-Amz-Expires=604800&X-Amz-Signature=f2663d507fcf72a6674d4a1eb5f592dd8d172290d7b2ef45be4d41dd52e26d3f)
- 만료 시간: 2026년 3월 19일 17시 3분에 Presigned URL 만료예정
<img width="1636" height="505" alt="image" src="https://github.com/user-attachments/assets/bc197e4d-a178-4cd4-96f0-08859e7344c5" />


  ---

## LV 4 - Docker & CI/CD 파이프라인 구축
로컬 환경 차이를 제거하고 자동 배포를 위해 Docker와 GitHub Actions를 도입했습니다.

**구현 내용**
- Dockerfile 작성
- GitHub Actions 워크플로우 작성
- Main 브랜치 Push 시 Build & Test 수행
- Docker 이미지 빌드 및 Docker Hub Push
- EC2에서 최신 이미지를 Pull 받아 재배포
- 컨테이너 재기동

**Dockerfile 예시**
  ```dockerfile
  FROM eclipse-temurin:17-jdk
  ARG JAR_FILE=build/libs/*.jar
  COPY ${JAR_FILE} app.jar
  ENTRYPOINT ["java", "-jar", "/app.jar"]
  ```

**CI/CD 흐름**
- GitHub Push
- GitHub Actions 실행
- Build & Test
- Docker Image Build
- Docker Hub Push
- EC2 서버에서 최신 이미지 Pull
- 컨테이너 재기동

**제출 항목**
- GitHub Actions 성공 캡처
<img width="1282" height="74" alt="스크린샷 2026-03-12 003024" src="https://github.com/user-attachments/assets/0af3d71b-6737-4380-a4d3-dd1746856bbe" />
- EC2 터미널 `docker ps` 캡처
<img width="1546" height="51" alt="스크린샷 2026-03-12 170304" src="https://github.com/user-attachments/assets/c560f6d9-4049-4406-80c8-54b1b13ddb07" />


  ---

## LV 5 - 고가용성 아키텍처와 보안 도메인 연결
고가용성과 보안을 위해 ALB, ASG, HTTPS, 도메인을 적용했습니다.

**구현 내용**
- Public Subnet에 NAT Gateway 생성
- Private Subnet 라우팅 테이블 수정
- EC2와 RDS를 Private 환경으로 이전
- Route 53에서 도메인 구입 및 Hosted Zone 설정
- ACM 인증서 발급
- ALB 생성 및 HTTPS 443 리스너 설정
- HTTP 80 → HTTPS Redirect 설정
- Launch Template 생성
- Auto Scaling Group 구성
- ALB Target Group 연결

**HTTPS 도메인 URL**
- `https://thisissparta.click/actuator/health`

**제출 항목**
- 대상 그룹 화면
<img width="1644" height="784" alt="스크린샷 2026-03-12 223608" src="https://github.com/user-attachments/assets/35ba3938-6087-44f6-b0ff-88af102ad569" />

  ---

## LV 6 - 글로벌 성능 최적화 (CloudFront CDN)
프로필 이미지 조회 성능 향상을 위해 CloudFront를 적용했습니다.

**구현 내용**
- S3를 Origin으로 하는 CloudFront Distribution 생성
- 이미지 조회를 S3 URL 대신 CloudFront URL로 제공
- 글로벌 엣지 캐싱 적용

**CloudFront 이미지 URL**
- https://dibsepblqhfqi.cloudfront.net/members/1/profile/582a4840-301d-40e4-9f8d-f068e6b090a0_KoAsXaqnGdy8MccsH5aWYVwkzy-jf4oNrHp-wyhv9Rx5B5qRL_9bjykEc9JASXcB90f5NyQ9llkHE9FUfAxEIA.webp

**제출 항목**
<img width="1396" height="959" alt="스크린샷 2026-03-12 224401" src="https://github.com/user-attachments/assets/76b9046c-9343-47b9-af1c-c5bcd00fcd27" />


  ---

# 9. 디렉토리 구조
  ```text
  member-service
  ├─ src
  │  ├─ main
  │  │  ├─ java
  │  │  └─ resources
  │  │     ├─ application.yml
  │  │     ├─ application-local.yml
  │  │     └─ application-prod.yml
  │  └─ test
  ├─ .github
  │  └─ workflows
  │     └─ deploy.yml
  ├─ Dockerfile
  ├─ README.md
  └─ images
  ```

  ---

# 10. 보안 및 운영 포인트

## 보안
- DB 비밀번호를 코드에 직접 작성하지 않음
- Parameter Store를 통한 환경 변수 관리
- S3 버킷 퍼블릭 액세스 차단
- IAM Role 기반 권한 부여
- HTTPS 적용
- RDS 보안 그룹에 IP가 아닌 EC2 보안 그룹 ID만 허용

## 운영
- Profile 분리로 로컬 / 운영 환경 구분
- Actuator를 통한 상태 확인
- Docker 기반 일관된 실행 환경
- GitHub Actions 기반 자동 배포
- ALB + ASG 기반 확장성 확보
- CloudFront 기반 정적 리소스 성능 개선

  ---

# 11. 트러블슈팅

## 11-1. Docker 이미지 아키텍처 불일치
**문제**
- EC2에서 컨테이너 실행 시 `exec format error` 발생

**원인**
- 로컬 환경과 EC2 환경의 CPU 아키텍처가 달라 이미지 호환 문제가 발생

**해결**
- Docker 이미지 빌드 시 플랫폼 명시

## 11-2. RDS 연결 실패
**문제**
- 애플리케이션이 운영 환경에서 MySQL에 연결되지 않음

**원인**
- RDS 보안 그룹 설정 미흡
- Parameter Store 값 주입 오류
- DB URL 오타 또는 데이터베이스 미생성

**해결**
- RDS 인바운드 규칙에 EC2 보안 그룹 연결
- Parameter Store 값 재확인
- 애플리케이션 실행 프로필 및 DB 설정 점검

## 11-3. ALB Health Check 실패
**문제**
- Target Group 상태가 Healthy로 올라오지 않음

**원인**
- Health Check Path 설정 오류
- 애플리케이션 미기동
- 보안 그룹 및 포트 설정 오류

**해결**
- Health Check Path 확인
- 컨테이너 실행 상태 확인
- EC2 / ALB 보안 그룹 재점검

  ---

# 12. 회고
이번 프로젝트를 통해 단순히 API를 만드는 수준을 넘어서 실제 운영을 고려한 클라우드 인프라 설계와 배포 자동화까지 경험할 수 있었습니다...

특히 아래 내용을 직접 구축해 보며 많은 것을 배웠습니다.
- 아키텍처 설계
- AWS 네트워크 구성
- 운영 환경 보안 관리
- Docker 기반 배포
- GitHub Actions 기반 CI/CD
- ALB / ASG 기반 고가용성 구성
- HTTPS 및 도메인 연결
- CloudFront CDN 적용

이 프로젝트를 통해 로컬에서만 동작하는 애플리케이션이 아니라 실제로 외부 사용자가 접근 가능한 운영형 서비스를 만드는 경험을 할 수 있었습니다. 그리고 꼼꼼함과 기도의 영역이 어떤 것인지 알게 되었습니다. 아쉬운 건 과제하는 것에 급급하여 트러블 슈팅을 제대로 작성 못한 것이 아쉽습니다. TIL도 더 열심히 작성했어야 되는데!!! 그래도 도전까지 해낸 제 자신이 자랑스럽고 꼭 이 과정이 익숙해지도록 더 열심히 노력하고 싶습니다. 정말로...... 그래도 한 번 해 봤으니까 자신감도 생긴 것 같습니다. 팀 프로젝트까지 잘 해내고 싶습니다.
