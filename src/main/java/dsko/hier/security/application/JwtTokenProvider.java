package dsko.hier.security.application;

import dsko.hier.security.config.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Jwts.SIG;
import jakarta.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final JwtProperties properties;
    private final ZoneId zoneId = ZoneId.systemDefault();
    private final JwtProperties jwtProperties;
    private SecretKey secretKey;

    private static final String USERNAME_CLAIM = "username";

    @PostConstruct
    private void initSecretKey() {
        this.secretKey = new SecretKeySpec(
                properties.getSecret().getBytes(StandardCharsets.UTF_8),
                SIG.HS256.key().build().getAlgorithm()
        );
    }

    public String createAccessToken(String username, String role) {
        LocalDateTime expiredTime = LocalDateTime.now().plusMinutes(jwtProperties.getAccessExpirationMinutes());
        return Jwts.builder()
                .claim("category", "access")
                .claim(USERNAME_CLAIM, username)
                .claim("role", "ROLE_" + role)
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(expiredTime.atZone(zoneId).toInstant()))
                .signWith(secretKey)
                .compact();
    }

    public String createRefreshToken(String username, String role) {
        LocalDateTime expiredTime = LocalDateTime.now().plusDays(jwtProperties.getRefreshExpirationDays());
        return Jwts.builder()
                .claim("category", "refresh")
                .claim(USERNAME_CLAIM, username)
                .claim("role", "ROLE_" + role)
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(expiredTime.atZone(zoneId).toInstant()))
                .signWith(secretKey)
                .compact();
    }

    public String getUsernameFromToken(String token) {
        return parseClaims(token)
                .get(USERNAME_CLAIM, String.class);
    }

    public String getRoleFromToken(String token) {
        return parseClaims(token)
                .get("role", String.class);
    }

    public LocalDateTime getExpirationDateFromToken(String token) {
        Date expiration = parseClaims(token).getExpiration();
        return LocalDateTime.ofInstant(expiration.toInstant(), zoneId);
    }

    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseClaimsJws(token)
                .getPayload();
    }

}
