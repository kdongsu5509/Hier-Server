package com.dt.find_restaurant.security.jwt.service;

import com.dt.find_restaurant.security.jwt.component.JwtProperties;
import com.dt.find_restaurant.security.jwt.domain.JwtEntity;
import com.dt.find_restaurant.security.jwt.domain.JwtResult;
import com.dt.find_restaurant.security.jwt.domain.JwtResult.Issue;
import com.dt.find_restaurant.security.jwt.repository.JwtRepository;
import com.dt.find_restaurant.user.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtService {

    private final JwtUtil jwtUtil;
    private final JwtRepository jwtRepository;
    private final UserRepository userRepository;
    private final UserDetailsService userDetailsService;
    private final JwtProperties jwtProperties;

    @Transactional
    public String validateAccessToken(HttpServletRequest request) {
        String accessToken = extractBearer(request, "Authorization");
        if (accessToken == null) {
            log.warn("Access Token 헤더가 누락되었거나 형식이 잘못되었습니다.");
            return null;
        }

        JwtEntity jwtEntity = tokenValidateWithDBStoredThings(accessToken);
        if (jwtEntity == null) {
            return null;
        }

        return accessToken;
    }

    @Transactional
    public Issue issueJwtAuth(String userEmail, String role) {
        Map<String, Object> accessTokenInfos = jwtUtil.createAccessToken(userEmail, role);
        Map<String, Object> refreshTokenInfos = jwtUtil.createRefreshToken(userEmail, role);

        JwtEntity saved = saveAndReturnJwt(userEmail, accessTokenInfos, refreshTokenInfos);

        String refresh = (String) refreshTokenInfos.get(JwtProperties.REFRESH_TOKEN);
        Cookie cookie = jwtUtil.createRefreshCookie(refresh);

        return Issue.of(
                saved.getAccessToken(),
                saved.getRefreshToken(),
                cookie
        );
    }

    @Transactional
    public JwtResult.Issue reissueAccessByRefresh(HttpServletRequest request) {
        String refreshToken = extractBearer(request, "Authorization");
        if (refreshToken == null) {
            log.warn("Refresh Token 헤더가 누락되었거나 형식이 잘못되었습니다.");
            return null;
        }

        JwtEntity entity = tokenValidateWithDBStoredThings(refreshToken);
        if (entity == null) {
            return null;
        }

        String role = userRepository.findByEmail(entity.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다. email=" + entity.getEmail()))
                .getRole();

        Map<String, Object> accessInfo = jwtUtil.createAccessToken(entity.getEmail(), role);

        Map<String, Object> refreshInfo = jwtUtil.createRefreshToken(entity.getEmail(), role);

        JwtEntity savedJwt = saveAndReturnJwt(
                entity.getEmail(),
                accessInfo,
                refreshInfo
        );

        Cookie refreshCookie = jwtUtil.createRefreshCookie(savedJwt.getRefreshToken());

        return JwtResult.Issue.of(
                savedJwt.getAccessToken(),
                savedJwt.getRefreshToken(),
                refreshCookie
        );
    }

    @Transactional
    public Authentication getAuthentication(String validAccessToken) {
        String userEmail = jwtUtil.getUsername(validAccessToken);
        UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }


    /* ======= 내부 유틸 ======= */

    private JwtEntity saveAndReturnJwt(String userEmail, Map<String, Object> accessTokenInfos,
                                       Map<String, Object> refreshTokenInfos) {
        JwtEntity jwtEntity = new JwtEntity(
                (String) accessTokenInfos.get(JwtProperties.ACCESS_TOKEN),
                (String) refreshTokenInfos.get(JwtProperties.REFRESH_TOKEN),
                userEmail,
                (LocalDateTime) accessTokenInfos.get(JwtProperties.ACCESS_TOKEN_EXP),
                (LocalDateTime) refreshTokenInfos.get(JwtProperties.REFRESH_TOKEN_EXP)
        );
        jwtRepository.save(jwtEntity);
        return jwtEntity;
    }

    private JwtEntity tokenValidateWithDBStoredThings(String token) {
        // 1) 토큰 종류 판별 + 엔티티 조회
        log.info("범인은 나 입니다. : JwtService.tokenValidateWithDBStoredThings()");
        JwtProperties.TokenType kind;
        Optional<JwtEntity> opt = jwtRepository.findJwtByAccessToken(token);
        if (opt.isPresent()) {
            kind = JwtProperties.TokenType.ACCESS;
        } else {
            opt = jwtRepository.findJwtByRefreshToken(token);
            if (opt.isEmpty()) {
                log.info("DB에 존재하지 않는 Token 입니다.");
                return null;
            }
            kind = JwtProperties.TokenType.REFRESH;
        }

        JwtEntity entity = opt.get();

        // 2) 만료 시각 선택
        LocalDateTime exp = switch (kind) {
            case ACCESS -> entity.getAccessTokenExpiredAt();
            case REFRESH -> entity.getRefreshTokenExpiredAt();
        };

        // 3) 만료 판정 (null → 만료로 간주)
        if (isExpired(exp, LocalDateTime.now())) {
            log.info("만료된 {} Token 감지 → 삭제 후 무효 처리. email={}", kind, entity.getEmail());
            jwtRepository.delete(entity);
            return null;
        }

        // 4) 유효
        log.debug("{} Token 유효. email={}", kind, entity.getEmail());
        return entity;
    }

    private String extractBearer(HttpServletRequest request, String headerName) {
        String value = request.getHeader(headerName);
        if (value == null || !value.startsWith("Bearer ")) {
            return null;
        }
        return value.replaceFirst("^Bearer\\s+", "").trim();
    }

    private boolean isExpired(LocalDateTime exp, LocalDateTime now) {
        // exp == null → 만료로 간주
        return exp == null || exp.isBefore(now);
    }
}
