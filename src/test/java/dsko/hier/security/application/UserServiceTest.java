package dsko.hier.security.application;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import dsko.hier.security.domain.EmailPasswordAccount;
import dsko.hier.security.domain.EmailPasswordAccountRepository;
import dsko.hier.security.domain.User;
import dsko.hier.security.domain.UserRepository;
import dsko.hier.security.domain.UserRole;
import dsko.hier.security.dto.EmailSignUpDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    EmailPasswordAccountRepository emailPasswordAccountRepository;

    @InjectMocks
    UserService userService;

    @Test
    @DisplayName("회원가입 성공")
    void signUp_success() {
        // Given
        EmailSignUpDto request = new EmailSignUpDto("test@example.com", "nickname", "password");

        User mockUser = User.builder()
                .email(request.email())
                .nickname(request.nickname())
                .role(UserRole.USER)
                .build();
        EmailPasswordAccount mockAccount = new EmailPasswordAccount("test@example.com", mockUser);

        when(userRepository.save(any(User.class))).thenReturn(mockUser);
        when(emailPasswordAccountRepository.save(any(EmailPasswordAccount.class))).thenReturn(mockAccount);

        // When
        userService.signUp(request);

        // Then
        verify(userRepository, times(1)).save(any(User.class));
        verify(emailPasswordAccountRepository, times(1)).save(any(EmailPasswordAccount.class));
    }
}
