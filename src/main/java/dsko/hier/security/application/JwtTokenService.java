package dsko.hier.security.application;

import dsko.hier.security.domain.JwtRepository;
import dsko.hier.security.domain.UserRepository;
import dsko.hier.security.dto.response.TokenResponse;
import io.jsonwebtoken.JwtException;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtTokenService {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final JwtRepository jwtRepository;
    private final RedisTemplate<String, String> redisTemplate;

    /**
     * 생성 -> Redis에 저장 -> 반환
     */

    /**
     * Issue -> JwtResul을 담을 수 있는 녀석으로 교체하여도 무방하다.
     * @param userEmail
     * @param role
     * @return
     */
    @Transactional
    public TokenResponse issueJwtAuth(String userEmail, String role) {
        String accessToken = jwtTokenProvider.createAccessToken(userEmail, role);
        String refreshToken = jwtTokenProvider.createRefreshToken(userEmail, role);

        JwtEntity saved = saveAndReturnJwt(userEmail, accessToken, refreshToken);

        return Issue.of(
                saved.getAccessToken(),
                saved.getRefreshToken()
        );
    }

    @Transactional
    public Issue reissueJwtToken(String refreshToken) {
        validateRefreshToken(refreshToken);

        String userEmail = jwtUtil.getUsername(refreshToken);
        User user = userRepository.findByEmail(userEmail).orElseThrow(
                () -> new JwtException(JWT_NOT_FOUND.getMessage())
        );
        String role = user.getRole();

        String newAccessToken = jwtUtil.createAccessToken(userEmail, role);
        String newRefreshToken = jwtUtil.createRefreshToken(userEmail, role);

        JwtEntity savedJwt = saveAndReturnJwt(
                userEmail,
                newAccessToken,
                newRefreshToken
        );

        return Issue.of(
                savedJwt.getAccessToken(),
                savedJwt.getRefreshToken()
        );
    }

    public boolean validateAccessToken(String accessToken) {

        //1. DB IO 필요없는 만료 시간부터 확인.
        validateAccessTokenWithExpriedTime(accessToken);
        log.info("checking accessToken: {}", accessToken);
        //2. DB에 존재하는가?
        JwtEntity jwtEntity = jwtRepository.findJwtByAccessToken(accessToken).orElseThrow(
                () -> new JwtException(JWT_NOT_FOUND.getMessage())
        );

        //3. 토큰이 DB에 저장된 토큰과 일치하는가?
        boolean equals = accessToken.equals(jwtEntity.getAccessToken());
        if (!equals) {
            throw new JwtException(JWT_MISSED.getMessage());
        }

        return true;
    }

    @Transactional
    public Authentication getAuthentication(String validAccessToken) {
        String userEmail = jwtUtil.getUsername(validAccessToken);
        String role = jwtUtil.getRole(validAccessToken);

        Collection<? extends GrantedAuthority> authorities =
                Collections.singletonList(new SimpleGrantedAuthority(role));

        CustomUserDetails princinpal = new CustomUserDetails(
                userRepository.findByEmail(userEmail).orElseThrow(
                        () -> new JwtException(JWT_NOT_FOUND.getMessage())
                ) // UserDetailsService를 통해 User 객체를 가져옴
        );

        return new UsernamePasswordAuthenticationToken(princinpal, null, authorities);
    }

    /* ======= 내부 유틸 ======= */

    private void validateRefreshToken(String refreshToken) {
        //1. DB IO 필요없는 만료 시간부터 확인.
        LocalDateTime expirationTime = jwtUtil.getExpirationFromToken(refreshToken);
        if (isExpired(expirationTime, LocalDateTime.now())) {
            throw new JwtException(JWT_EXPIRED.getMessage());
        }

        //2. DB에 존재하는가?
        JwtEntity jwtEntity = jwtRepository.findJwtByRefreshToken(refreshToken).orElseThrow(
                () -> new JwtException(JWT_NOT_FOUND.getMessage())
        );

        //3. DB에 저장된 refreshToken과 일치하는가?
        boolean equals = refreshToken.equals(jwtEntity.getRefreshToken());
        if (!equals) {
            throw new JwtException(JWT_MISSED.getMessage());
        }
    }

    private JwtEntity saveAndReturnJwt(String userEmail, String accessToken, String refreshToken) {
        JwtEntity jwtEntity = new JwtEntity(
                accessToken,
                refreshToken,
                userEmail,
                jwtUtil.getExpirationFromToken(accessToken),
                jwtUtil.getExpirationFromToken(refreshToken)
        );
        log.info("Saving JWT for user: {}, accessToken: {}, refreshToken: {}", userEmail, accessToken, refreshToken);
        return jwtRepository.save(jwtEntity);
    }

    private boolean isExpired(LocalDateTime exp, LocalDateTime now) {
        // exp == null → 만료로 간주
        return exp == null || exp.isBefore(now);
    }

    private void validateAccessTokenWithExpriedTime(String accessToken) {
        LocalDateTime expirationTime = jwtUtil.getExpirationFromToken(accessToken);
        if (isExpired(expirationTime, LocalDateTime.now())) {
            throw new JwtException(JWT_EXPIRED.getMessage());
        }
    }

}
