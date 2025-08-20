package com.dt.find_restaurant.security.global.filter;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

import com.dt.find_restaurant.security.application.JwtService;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@ExtendWith(MockitoExtension.class)
class JwtFilterTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private JwtFilter jwtFilter;

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

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("유효한 JWT 토큰이 헤더에 있을 경우, SecurityContext에 인증 정보가 저장된다")
    void doFilterInternal_WithValidToken_ShouldSetAuthentication() throws ServletException, IOException {
        // given (주어진 상황)
        String validToken = "valid-jwt-token";
        Authentication authentication = new UsernamePasswordAuthenticationToken("user", null, null);

        given(request.getHeader("Authorization")).willReturn("Bearer " + validToken);
        given(jwtService.validateAccessToken(validToken)).willReturn(true);
        given(jwtService.getAuthentication(validToken)).willReturn(authentication);

        // when (무엇을 할 때)
        jwtFilter.doFilterInternal(request, response, filterChain);

        // then (결과)
        // SecurityContext에 Authentication 객체가 저장되었는지 확인
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isEqualTo(authentication);
        // filterChain.doFilter가 호출되었는지 확인
        then(filterChain).should(times(1)).doFilter(request, response);
    }

    @Test
    @DisplayName("JWT 토큰이 헤더에 없을 경우, 아무런 인증 정보도 저장되지 않는다")
    void doFilterInternal_WithNoToken_ShouldNotSetAuthentication() throws ServletException, IOException {
        // given
        given(request.getHeader("Authorization")).willReturn(null);

        // when
        jwtFilter.doFilterInternal(request, response, filterChain);

        // then
        // SecurityContext가 비어있는지 확인
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
        // JwtService의 어떤 메서드도 호출되지 않았는지 확인
        then(jwtService).should(never()).validateAccessToken(anyString());
        then(jwtService).should(never()).getAuthentication(anyString());
        // filterChain.doFilter가 호출되었는지 확인
        then(filterChain).should(times(1)).doFilter(request, response);
    }

    @Test
    @DisplayName("유효하지 않은 만료기간을 가진 JWT 토큰이 헤더에 있을 경우, SecurityContext가 비워진다")
    void doFilterInternal_WithInvalidToken_ShouldClearContext() throws ServletException, IOException {
        // given
        String expiredToken = Jwts.builder()
                .claim("category", "access")
                .claim("username", "ds.ko@kakao.com")
                .claim("role", "ROLE_ADMIN")
                .issuedAt(Date.from(Instant.now().minus(2, ChronoUnit.DAYS))) // 발행일을 이틀 전으로 설정
                .expiration(Date.from(Instant.now().minus(1, ChronoUnit.DAYS))) // 만료일을 하루 전으로 설정
                .signWith(secretKey)
                .compact();
        given(request.getHeader("Authorization")).willReturn("Bearer " + expiredToken);
        // validateAccessToken이 예외를 던지는 상황을 시뮬레이션
        given(jwtService.validateAccessToken(expiredToken)).willThrow(new RuntimeException("Invalid Token"));

        // 만약의 경우를 대비해 미리 컨텍스트에 임의의 값을 넣어둠
        SecurityContextHolder.getContext()
                .setAuthentication(new UsernamePasswordAuthenticationToken("pre-existing", null));

        // when
        jwtFilter.doFilterInternal(request, response, filterChain);

        // then
        // catch 블록이 실행되어 SecurityContext가 비워졌는지 확인
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
        // filterChain.doFilter가 호출되었는지 확인
        then(filterChain).should(times(1)).doFilter(request, response);
    }
}