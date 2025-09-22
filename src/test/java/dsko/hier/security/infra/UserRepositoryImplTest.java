package dsko.hier.security.infra;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import dsko.hier.security.domain.User;
import dsko.hier.security.domain.UserRole;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserRepositoryImplTest {

    @Mock
    private UserJpaRepository userJpaRepository;

    @InjectMocks
    private UserRepositoryImpl userRepository;

    @Test
    @DisplayName("사용자 저장 성공")
    void save_user_success() {
        // Given
        User user = User.builder()
                .email("test@example.com")
                .nickname("testuser")
                .role(UserRole.USER)
                .build();

        when(userJpaRepository.save(any(User.class))).thenReturn(user);

        // When
        User savedUser = userRepository.save(user);

        // Then
        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getEmail()).isEqualTo("test@example.com");
    }

    @Test
    @DisplayName("ID로 사용자 조회 성공")
    void findById_success() {
        // Given
        UUID userId = UUID.randomUUID();
        User user = User.builder()
                .email("test@example.com")
                .nickname("testuser")
                .role(UserRole.USER)
                .build();

        when(userJpaRepository.findById(userId)).thenReturn(Optional.of(user));

        // When
        Optional<User> foundUser = userRepository.findById(userId);

        // Then
        assertThat(foundUser).isPresent();
    }
}