package dsko.hier.security.application;

import dsko.hier.security.domain.UserRepository;
import dsko.hier.security.dto.request.EmailAndPassword;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class LoginService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;

    public void emailAndPasswordLogin(EmailAndPassword req) {

        log.info("로그인 서비스 진입");
        log.info("로그인 서비스에 들어온 이메일 : {}", req.email());

        // 1. 토큰 생성

        // 2. 인증 매니저에게 토큰 전달하여 인증 처리
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        req.email(), req.password())
        );

        log.info("로그인 완료 : {}", authenticate.isAuthenticated());

        // 3. 인증 정보 기반으로 JWT 토큰 생성
        UserDetails principal = (UserDetails) authenticate.getPrincipal();
        jwtTokenService.issueJwtToken(principal.getUsername(), principal.getAuthorities().iterator().next().getAuthority());
        // 4. JWT 토큰 반환
    }
}
