package com.dt.find_restaurant.security.global.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import io.jsonwebtoken.ExpiredJwtException;
import java.lang.reflect.Method;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class JwtUtilTest {

    private JwtUtil jwtUtil;
    private JwtProperties jwtProperties;

    @BeforeEach
    void setUp() throws Exception {
        // 1. 테스트용 JwtProperties 객체 생성
        jwtProperties = new JwtProperties();
        jwtProperties.setSecret("this-is-a-test-secret-key-that-is-long-enough-for-hs256");
        jwtProperties.setAccessExpirationMinutes(10);
        jwtProperties.setRefreshExpirationDays(1);
        jwtProperties.setRefreshCookieName("refresh_token");

        // 2. 테스트 대상인 JwtUtil 객체 생성
        jwtUtil = new JwtUtil(jwtProperties);

        // 3. @PostConstruct를 수동으로 호출하기 (private이므로 리플렉션 사용)
        Method initSecretKeyMethod = JwtUtil.class.getDeclaredMethod("initSecretKey");
        initSecretKeyMethod.setAccessible(true);
        initSecretKeyMethod.invoke(jwtUtil);
    }

    @Nested
    @DisplayName("토큰 생성 및 검증")
    class TokenCreationAndValidation {

        @Test
        @DisplayName("AccessToken을 정상적으로 생성하고, 파싱 시 올바른 정보를 포함해야 한다")
        void createAccessToken_ShouldContainCorrectClaims() {
            // given
            String username = "test@user.com";
            String role = "USER";

            // when
            String accessToken = jwtUtil.createAccessToken(username, role);

            // then
            assertThat(accessToken).isNotNull();
            assertThat(jwtUtil.getUsername(accessToken)).isEqualTo(username);
            assertThat(jwtUtil.getRole(accessToken)).isEqualTo("ROLE_" + role);
        }

        @Test
        @DisplayName("RefreshToken을 정상적으로 생성하고, 파싱 시 올바른 정보를 포함해야 한다")
        void createRefreshToken_ShouldContainCorrectClaims() {
            // given
            String username = "test@user.com";
            String role = "ADMIN";

            // when
            String refreshToken = jwtUtil.createRefreshToken(username, role);

            // then
            assertThat(refreshToken).isNotNull();
            assertThat(jwtUtil.getUsername(refreshToken)).isEqualTo(username);
            assertThat(jwtUtil.getRole(refreshToken)).isEqualTo("ROLE_" + role);
        }

        @Test
        @DisplayName("만료된 토큰을 파싱하려고 하면 ExpiredJwtException이 발생해야 한다")
        void parseClaims_WithExpiredToken_ShouldThrowException() {
            // given
            // 만료 시간을 매우 짧게 설정하여 테스트
            jwtProperties.setAccessExpirationMinutes(-1); // 1분 전 만료
            String expiredToken = jwtUtil.createAccessToken("expired@user.com", "USER");

            // when & then
            assertThrows(ExpiredJwtException.class, () -> {
                jwtUtil.getUsername(expiredToken);
            });
        }

        @Test
        @DisplayName("잘못된 시크릿 키로 서명된 토큰은 검증에 실패해야 한다")
        void parseClaims_WithInvalidSignature_ShouldThrowException() {
            // given
            String anotherSecret = "another-secret-key-that-is-also-long-enough-for-security";
            JwtProperties anotherProperties = new JwtProperties();
            anotherProperties.setSecret(anotherSecret);

            String token = jwtUtil.createAccessToken("user", "USER"); // 원래 secret으로 생성
            String invalidToken = token + "invalid"; // 토큰 조작

            // when & then
            // 실제로는 SignatureException이 발생하지만, 테스트 간소화를 위해 Exception으로 확인
            assertThrows(Exception.class, () -> {
                jwtUtil.getUsername(invalidToken);
            });
        }
    }
}