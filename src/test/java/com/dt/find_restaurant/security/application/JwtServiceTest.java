package com.dt.find_restaurant.security.application;

import static com.dt.find_restaurant.global.exception.CustomExcpMsgs.JWT_EXPIRED;
import static com.dt.find_restaurant.global.exception.CustomExcpMsgs.JWT_NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.dt.find_restaurant.global.exception.CustomExceptions.JwtException;
import com.dt.find_restaurant.security.domain.CustomUserDetails;
import com.dt.find_restaurant.security.domain.JwtEntity;
import com.dt.find_restaurant.security.domain.JwtRepository;
import com.dt.find_restaurant.security.domain.JwtResult;
import com.dt.find_restaurant.security.domain.User;
import com.dt.find_restaurant.security.domain.UserRepository;
import com.dt.find_restaurant.security.global.util.JwtUtil;
import io.jsonwebtoken.Jwts;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {

    @Mock
    private JwtUtil jwtUtil;
    @Mock
    private JwtRepository jwtRepository;
    @Mock
    private UserRepository userRepository;
    // userDetailsService는 현재 코드에서 직접 사용되지 않으므로 Mock 객체에서 제외

    @InjectMocks
    private JwtService jwtService;

    private final String userEmail = "test@user.com";
    private final String role = "ADMIN";
    private final String accessToken = "test-access-token";
    private final String refreshToken = "test-refresh-token";

    private SecretKey secretKey;
    private final String keyOrigin = "asdfffffffffffffaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";

    @BeforeEach
    void setUp() {
        // secretKey 설정 (테스트용)
        this.secretKey = new SecretKeySpec(
                keyOrigin.getBytes(StandardCharsets.UTF_8),
                Jwts.SIG.HS256.key().build().getAlgorithm()
        );
    }

    @Nested
    @DisplayName("토큰 발급 테스트")
    class IssueTokenTests {

        @Test
        @DisplayName("성공: 사용자 이메일과 역할을 받아 새로운 JWT 인증 정보를 발급한다")
        void issueJwtAuth_Success() {
            // given
            JwtEntity jwtEntity = new JwtEntity(accessToken, refreshToken, userEmail,
                    LocalDateTime.now().plusMinutes(10), LocalDateTime.now().plusDays(7));
            given(jwtUtil.createAccessToken(userEmail, role)).willReturn(accessToken);
            given(jwtUtil.createRefreshToken(userEmail, role)).willReturn(refreshToken);
            given(jwtRepository.save(any(JwtEntity.class))).willReturn(jwtEntity);

            // when
            JwtResult.Issue result = jwtService.issueJwtAuth(userEmail, role);

            // then
            assertThat(result.getAccessToken()).isEqualTo(accessToken);
            assertThat(result.getRefreshToken()).isEqualTo(refreshToken);
            then(jwtRepository).should().save(any(JwtEntity.class));
        }
    }


    @Nested
    @DisplayName("토큰 재발급 테스트")
    class ReissueTokenTests {

        @Test
        @DisplayName("성공: 유효한 Refresh Token으로 새로운 Access/Refresh Token을 재발급한다")
        void reissueJwtToken_WithValidToken_Success() {
            // given
            String prevRefresh = Jwts.builder()
                    .claim("category", "refresh")
                    .claim("username", "ds.ko@kakao.com")
                    .claim("role", "ROLE_ADMIN")
                    .issuedAt(Date.from(Instant.now().plus(2, ChronoUnit.DAYS))) // 발행일을 이틀 전으로 설정
                    .expiration(Date.from(Instant.now().plus(1, ChronoUnit.DAYS))) // 만료일을 하루 전으로 설정
                    .signWith(secretKey)
                    .compact();
            JwtEntity existingJwt = new JwtEntity("old-access", prevRefresh, userEmail, null, null);
            User user = User.create(userEmail, "password123", "ADMIN", "test", null, true);
            String newAccessToken = "new-access-token";
            String newRefreshToken = "new-refresh-token";
            JwtEntity newJwtEntity = new JwtEntity(newAccessToken, newRefreshToken, userEmail, null, null);

            given(jwtUtil.getExpirationFromToken(prevRefresh)).willReturn(LocalDateTime.now().plusDays(1));
            given(jwtRepository.findJwtByRefreshToken(prevRefresh)).willReturn(Optional.of(existingJwt));
            given(jwtUtil.getUsername(prevRefresh)).willReturn(userEmail);
            given(userRepository.findByEmail(userEmail)).willReturn(user);
            given(jwtUtil.createAccessToken(userEmail, role)).willReturn(newAccessToken);
            given(jwtUtil.createRefreshToken(userEmail, role)).willReturn(newRefreshToken);
            given(jwtRepository.save(any(JwtEntity.class))).willReturn(newJwtEntity);

            // when
            JwtResult.Issue result = jwtService.reissueJwtToken(prevRefresh);

            // then
            assertThat(result.getAccessToken()).isEqualTo(newAccessToken);
            assertThat(result.getRefreshToken()).isEqualTo(newRefreshToken);
        }

        @Test
        @DisplayName("실패: 만료된 Refresh Token으로 재발급 요청 시 JwtException 발생")
        void reissueJwtToken_WithExpiredToken_ShouldThrowException() {
            // given
            given(jwtUtil.getExpirationFromToken(refreshToken)).willReturn(LocalDateTime.now().minusDays(1));

            // when & then
            assertThatThrownBy(() -> jwtService.reissueJwtToken(refreshToken))
                    .isInstanceOf(JwtException.class)
                    .hasMessage(JWT_EXPIRED.getMessage());
        }

        @Test
        @DisplayName("실패: DB에 존재하지 않는 Refresh Token으로 재발급 요청 시 JwtException 발생")
        void reissueJwtToken_WithNonExistentToken_ShouldThrowException() {
            // given
            given(jwtUtil.getExpirationFromToken(refreshToken)).willReturn(LocalDateTime.now().plusDays(1));
            given(jwtRepository.findJwtByRefreshToken(refreshToken)).willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> jwtService.reissueJwtToken(refreshToken))
                    .isInstanceOf(JwtException.class)
                    .hasMessage(JWT_NOT_FOUND.getMessage());
        }
    }

    @Nested
    @DisplayName("Access Token 검증 테스트")
    class ValidateAccessTokenTests {

        @Test
        @DisplayName("성공: 유효한 Access Token 검증 시 true를 반환한다")
        void validateAccessToken_WithValidToken_ShouldReturnTrue() {
            // given
            JwtEntity jwtEntity = new JwtEntity(accessToken, refreshToken, userEmail, null, null);
            given(jwtUtil.getExpirationFromToken(accessToken)).willReturn(LocalDateTime.now().plusMinutes(5));
            given(jwtRepository.findJwtByAccessToken(accessToken)).willReturn(Optional.of(jwtEntity));

            // when
            boolean isValid = jwtService.validateAccessToken(accessToken);

            // then
            assertThat(isValid).isTrue();
        }

        @Test
        @DisplayName("실패: DB에 없는 Access Token 검증 시 JwtException 발생")
        void validateAccessToken_WithNonExistentToken_ShouldThrowException() {
            // given
            given(jwtUtil.getExpirationFromToken(accessToken)).willReturn(LocalDateTime.now().plusMinutes(5));
            given(jwtRepository.findJwtByAccessToken(accessToken)).willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> jwtService.validateAccessToken(accessToken))
                    .isInstanceOf(JwtException.class)
                    .hasMessage(JWT_NOT_FOUND.getMessage());
        }
    }

    @Nested
    @DisplayName("인증 객체 생성 테스트")
    class GetAuthenticationTests {
        @Test
        @DisplayName("성공: 유효한 Access Token으로 Authentication 객체를 생성한다")
        void getAuthentication_WithValidToken_Success() {
            // given
            User user = User.create(userEmail, "password123", "ADMIN", "test", null, true);
            given(jwtUtil.getUsername(accessToken)).willReturn(userEmail);
            given(jwtUtil.getRole(accessToken)).willReturn("ROLE_ADMIN");
            given(userRepository.findByEmail(userEmail)).willReturn(user);

            // when
            Authentication authentication = jwtService.getAuthentication(accessToken);

            // then
            assertThat(authentication).isNotNull();
            assertThat(authentication.getPrincipal()).isInstanceOf(CustomUserDetails.class);
            assertThat(authentication.getAuthorities()).hasSize(1);
            assertThat(authentication.getAuthorities().iterator().next().getAuthority()).isEqualTo("ROLE_ADMIN");
        }
    }
}