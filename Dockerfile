# 1. java 17 환경
FROM eclipse-temurin:17-jdk-focal

# 2. 작업 디렉토리
WORKDIR /app

# 3. 빌드가 된 jar 파일을 컨테이너 내부로 복사
COPY build/libs/*.jar app.jar

# 4. 실행 명령어
ENTRYPOINT ["java", "-jar", "app.jar"]