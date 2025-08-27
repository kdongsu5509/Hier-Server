package com.dt.find_restaurant.security.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.dt.find_restaurant.global.exception.CustomExceptions.UserException;
import com.dt.find_restaurant.security.domain.User;
import com.dt.find_restaurant.security.domain.UserRepository;
import com.dt.find_restaurant.security.presentation.dto.UserDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private BCryptPasswordEncoder passwordEncoder; // PasswordEncoder 인터페이스로 Mock하는 것이 더 좋습니다.

    @Mock
    private UserRepository userRepository;

    @Test
    @DisplayName("join: 회원가입 요청 시, 비밀번호를 암호화하여 저장한다")
    void signUp_with_valid_dto_should_save_encoded_user() {
        // given
        UserDto requestDto = new UserDto(
                "test@example.com",
                "password123",
                "tester",
                null
        );
        String rawPassword = requestDto.password();
        String encodedPassword = "encodedPassword123";

        // passwordEncoder.encode()가 호출되면 미리 정의된 암호화된 비밀번호를 반환하도록 설정
        given(passwordEncoder.encode(rawPassword)).willReturn(encodedPassword);

        // when
        userService.signUp(requestDto);

        // then
        // userRepository.save() 메소드로 전달된 User 객체를 캡처
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        then(userRepository).should().save(userCaptor.capture());

        // 캡처된 User 객체의 필드 검증
        User savedUser = userCaptor.getValue();
        assertThat(savedUser.getEmail()).isEqualTo(requestDto.email());
        assertThat(savedUser.getUserName()).isEqualTo(requestDto.username());
        assertThat(savedUser.getPassword()).isEqualTo(encodedPassword); // 비밀번호가 암호화되었는지 확인
        assertThat(savedUser.getPassword()).isNotEqualTo(rawPassword);   // 원본 비밀번호와 다른지 확인
    }

    @Test
    @DisplayName("isEmailUnique: 이메일이 중복되면 exception를 터뜨린다")
    void isEmailUnique_with_notunique_email_should_return_false() {
        given(userRepository.existsByUserName("tester"))
                .willReturn(Boolean.TRUE); // 이미 존재하는 사용자 이름

        Assertions.assertThrows(
                UserException.class,
                () -> userService.isNameUnique("tester")
        );
    }
}