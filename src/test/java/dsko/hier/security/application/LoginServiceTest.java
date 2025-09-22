package dsko.hier.security.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import dsko.hier.security.dto.request.EmailAndPassword;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
class LoginServiceTest {

    @Autowired
    private LoginService loginService;

    @MockitoBean
    private AuthenticationManager authenticationManager;

    @Test
    @DisplayName("유효한 이메일과 비밀번호로 로그인 시 인증이 성공한다")
    void login_success() {
        // Given
        EmailAndPassword req = new EmailAndPassword("test@example.com", "password123");

        // Mock: authenticationManager.authenticate()가 성공적으로 반환되도록 설정
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(new UsernamePasswordAuthenticationToken(req.email(), req.password()));

        // When & Then
        // 예외가 발생하지 않으면 성공loginService.emailAndPasswordLogin(req)
        loginService.emailAndPasswordLogin(req);
    }

    @Test
    @DisplayName("유효하지 않은 비밀번호로 로그인 시 BadCredentialsException 예외를 던진다")
    void login_failure_bad_credentials() {
        // Given
        EmailAndPassword req = new EmailAndPassword("test@example.com", "wrong_password");

        // Mock: authenticationManager.authenticate()가 BadCredentialsException을 던지도록 설정
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Invalid password"));

        // When & Then
        // BadCredentialsException 예외가 발생하는지 검증
        assertThatThrownBy(() -> loginService.emailAndPasswordLogin(req))
                .isInstanceOf(BadCredentialsException.class)
                .hasMessageContaining("Invalid password");
    }
}