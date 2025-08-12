package com.dt.find_restaurant.security.jwt.service;

import com.dt.find_restaurant.security.jwt.component.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtUtil {

    private final JwtProperties jwtProperties;
    private final ZoneId zoneId = ZoneId.systemDefault();
    private SecretKey secretKey;

    @PostConstruct
    private void initSecretKey() {
        this.secretKey = new SecretKeySpec(
                jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8),
                Jwts.SIG.HS256.key().build().getAlgorithm()
        );
    }

    public Map<String, Object> createAccessToken(String username, String role) {
        LocalDateTime expiredTime = getExpiredTime(true);
        String newAccessToken = makeNewJwts(username, role, true, expiredTime);

        return makeReturnValue(true, newAccessToken, expiredTime);
    }

    public Map<String, Object> createRefreshToken(String username, String role) {
        LocalDateTime expiredTime = getExpiredTime(false);
        String newRefreshToken = makeNewJwts(username, role, false, expiredTime);

        return makeReturnValue(false, newRefreshToken, expiredTime);
    }

    private LocalDateTime getExpiredTime(boolean isAccess) {
        if (isAccess) {
            return LocalDateTime.now().plusMinutes(jwtProperties.getAccessExpirationMinutes());
        } else {
            return LocalDateTime.now().plusDays(jwtProperties.getRefreshExpirationDays());
        }
    }

    private String makeNewJwts(String username, String role, boolean isAccess, LocalDateTime expiredTime) {
        String category = isAccess ? "access" : "refresh";
        return Jwts.builder()
                .claim("category", category)
                .claim("username", username)
                .claim("role", role)
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(expiredTime.atZone(zoneId).toInstant()))
                .signWith(secretKey)
                .compact();
    }

    private static Map<String, Object> makeReturnValue(boolean isAccess, String newAccessToken,
                                                       LocalDateTime expiredTime) {
        if (isAccess) {
            return Map.of(
                    JwtProperties.ACCESS_TOKEN, newAccessToken,
                    JwtProperties.ACCESS_TOKEN_EXP, expiredTime
            );
        } else {
            return Map.of(
                    JwtProperties.REFRESH_TOKEN, newAccessToken,
                    JwtProperties.REFRESH_TOKEN_EXP, expiredTime
            );
        }
    }

    public String getUsername(String token) {
        return parseClaims(token).get("username", String.class);
    }

    public String getRole(String token) {
        return parseClaims(token).get("role", String.class);
    }

    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // ===================== 만료 시간 =====================

    public LocalDateTime getExpirationFromToken(String token) {
        Claims claims = parseClaims(token);
        return claims.getExpiration().toInstant().atZone(zoneId).toLocalDateTime();
    }

    public LocalDateTime getRefreshExpiredTime() {
        return LocalDateTime.now().plusDays(jwtProperties.getRefreshExpirationDays());
    }

    // ===================== 쿠키 =====================

    public Cookie createRefreshCookie(String refreshToken) {
        int maxAge = (int) (jwtProperties.getRefreshExpirationDays() * 24 * 60 * 60); // 일 → 초 변환

        Cookie cookie = new Cookie(jwtProperties.getRefreshCookieName(), refreshToken);
        cookie.setMaxAge(maxAge);
        cookie.setHttpOnly(true);
        cookie.setPath("/");

        return cookie;
    }

    public Cookie createLogoutCookie() {
        Cookie cookie = new Cookie(jwtProperties.getRefreshCookieName(), null);
        cookie.setMaxAge(0); // 삭제
        cookie.setPath("/");
        return cookie;
    }
}
