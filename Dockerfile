# 1. 빌드 스테이지: JDK 환경에서 .jar 파일 생성
FROM eclipse-temurin:21-jdk-jammy as builder
WORKDIR /app
COPY . .
RUN ./gradlew build -x test

# 2. 실행 스테이지: JRE 환경에 .jar 파일만 복사
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]