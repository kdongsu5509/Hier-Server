package com.dt.find_restaurant.security.infra;

import static com.dt.find_restaurant.global.exception.CustomExcpMsgs.USER_NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.dt.find_restaurant.global.exception.CustomExceptions.UserException;
import com.dt.find_restaurant.security.domain.User;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class UserRepositoryImplTest {

    @InjectMocks
    private UserRepositoryImpl userRepository;

    @Mock
    private UserJpaRepository userJpaRepository;

    private User testUser;
    private UUID testUuid;

    @BeforeEach
    void setUp() {
        testUuid = UUID.randomUUID();
        // User.create() 정적 팩토리 메소드를 사용하여 테스트 객체 생성
        testUser = User.create(
                "test@example.com",
                "password123",
                "ROLE_USER",
                "testuser",
                "http://example.com/profile.jpg"
        );
        // 테스트의 일관성을 위해 ReflectionTestUtils로 UUID를 주입
        ReflectionTestUtils.setField(testUser, "id", testUuid);
    }

    @Test
    @DisplayName("save: User 객체를 저장하고 UUID를 반환한다")
    void save_user_and_return_uuid() {
        // given
        given(userJpaRepository.save(any(User.class))).willReturn(testUser);

        // when
        UUID savedUuid = userRepository.save(testUser);

        // then
        assertThat(savedUuid).isEqualTo(testUuid);
        then(userJpaRepository).should().save(testUser);
    }

    @Nested
    @DisplayName("findById 메소드는")
    class Describe_findById {
        @Test
        @DisplayName("존재하는 UUID로 조회 시 User 객체를 반환한다")
        void when_exists_id_return_user() {
            // given
            given(userJpaRepository.findById(testUuid)).willReturn(Optional.of(testUser));

            // when
            User foundUser = userRepository.findById(testUuid);

            // then
            assertThat(foundUser).isEqualTo(testUser);
        }

        @Test
        @DisplayName("존재하지 않는 UUID로 조회 시 UserException을 발생시킨다")
        void when_not_exists_id_throw_exception() {
            // given
            UUID notExistsUuid = UUID.randomUUID();
            given(userJpaRepository.findById(notExistsUuid)).willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> userRepository.findById(notExistsUuid))
                    .isInstanceOf(UserException.class)
                    .hasMessage(USER_NOT_FOUND.getMessage() + notExistsUuid);
        }
    }

    @Nested
    @DisplayName("findByEmail 메소드는")
    class Describe_findByEmail {
        @Test
        @DisplayName("존재하는 이메일로 조회 시 User 객체를 반환한다")
        void when_exists_email_return_user() {
            // given
            given(userJpaRepository.findByEmail(testUser.getEmail())).willReturn(Optional.of(testUser));

            // when
            Optional<User> foundUser = userRepository.findByEmail(testUser.getEmail());

            // then
            assertThat(foundUser.get()).isEqualTo(testUser);
        }

        @Test
        @DisplayName("존재하지 않는 이메일로 조회 시 Null을 반환한다")
        void when_not_exists_email_throw_exception() {
            // given
            String notExistsEmail = "nouser@example.com";
            given(userJpaRepository.findByEmail(notExistsEmail)).willReturn(Optional.empty());

            // when & then
            Optional<User> byEmail = userRepository.findByEmail(notExistsEmail);

            //then
            assertThat(byEmail).isEmpty();
        }
    }

    @Nested
    @DisplayName("deleteById 메소드는")
    class Describe_deleteById {
        @Test
        @DisplayName("존재하는 UUID로 삭제 시 정상적으로 deleteById를 호출한다")
        void when_exists_id_delete_successfully() {
            // given
            given(userJpaRepository.existsById(testUuid)).willReturn(true);

            // when
            userRepository.deleteById(testUuid);

            // then
            then(userJpaRepository).should().existsById(testUuid);
            then(userJpaRepository).should().deleteById(testUuid);
        }

        @Test
        @DisplayName("존재하지 않는 UUID로 삭제 시 UserException을 발생시킨다")
        void when_not_exists_id_throw_exception() {
            // given
            UUID notExistsUuid = UUID.randomUUID();
            given(userJpaRepository.existsById(notExistsUuid)).willReturn(false);

            // when & then
            assertThatThrownBy(() -> userRepository.deleteById(notExistsUuid))
                    .isInstanceOf(UserException.class)
                    .hasMessage(USER_NOT_FOUND.getMessage() + notExistsUuid);

        }
    }

    @Nested
    @DisplayName("update 메소드는")
    class Describe_update {
        @Test
        @DisplayName("존재하는 사용자로 수정 시 정상적으로 save를 호출한다")
        void when_exists_user_update_successfully() {
            // given
            given(userJpaRepository.existsById(testUuid)).willReturn(true);

            // when
            userRepository.update(testUser);

            // then
            then(userJpaRepository).should().existsById(testUuid);
            then(userJpaRepository).should().save(testUser);
        }

        @Test
        @DisplayName("존재하지 않는 사용자로 수정 시 UserException을 발생시킨다")
        void when_not_exists_user_throw_exception() {
            // given
            given(userJpaRepository.existsById(testUuid)).willReturn(false);

            // when & then
            assertThatThrownBy(() -> userRepository.update(testUser))
                    .isInstanceOf(UserException.class)
                    .hasMessage(USER_NOT_FOUND.getMessage() + testUuid);
        }
    }
}