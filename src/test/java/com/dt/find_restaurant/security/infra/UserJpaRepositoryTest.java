package com.dt.find_restaurant.security.infra;

import static org.assertj.core.api.Assertions.assertThat;

import com.dt.find_restaurant.security.domain.User;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest // JPA 관련 컴포넌트만 로드하여 테스트합니다. (인메모리 DB 사용)
class UserJpaRepositoryTest {

    @Autowired
    private UserJpaRepository userJpaRepository;

    @Test
    @DisplayName("findByEmail: 이메일로 사용자를 성공적으로 조회한다.")
    void findByEmail_Success() {
        // given (준비)
        final String email = "test@example.com";
        final String username = "testuser";
        User user = User.create(email, "password123", "USER", username);
        userJpaRepository.save(user);

        // when (실행)
        Optional<User> foundUserOptional = userJpaRepository.findByEmail(email);

        // then (검증)
        assertThat(foundUserOptional).isPresent(); // Optional이 비어있지 않은지 확인
        assertThat(foundUserOptional.get().getEmail()).isEqualTo(email);
        assertThat(foundUserOptional.get().getUserName()).isEqualTo(username);
    }

    @Test
    @DisplayName("findByEmail: 존재하지 않는 이메일로 조회하면 빈 Optional을 반환한다.")
    void findByEmail_Failure() {
        // given (준비) - 아무 데이터도 저장하지 않음

        // when (실행)
        Optional<User> foundUserOptional = userJpaRepository.findByEmail("nonexistent@example.com");

        // then (검증)
        assertThat(foundUserOptional).isEmpty(); // Optional이 비어있는지 확인
    }

    @Test
    @DisplayName("existsByUserName: 존재하는 닉네임이면 true를 반환한다.")
    void existsByUserName_True() {
        // given (준비)
        final String username = "existing_user";
        User user = User.create("test@example.com", "password123", "USER", username);
        userJpaRepository.save(user);

        // when (실행)
        boolean exists = userJpaRepository.existsByUserName(username);

        // then (검증)
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("existsByUserName: 존재하지 않는 닉네임이면 false를 반환한다.")
    void existsByUserName_False() {
        // given (준비) - 아무 데이터도 저장하지 않음

        // when (실행)
        boolean exists = userJpaRepository.existsByUserName("nonexistent_user");

        // then (검증)
        assertThat(exists).isFalse();
    }

    // findByUserName, existsByEmail에 대한 테스트도 위와 유사하게 작성할 수 있습니다.
}