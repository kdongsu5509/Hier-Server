package com.dt.find_restaurant.security.infra;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

import com.dt.find_restaurant.security.domain.JwtEntity;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class) // JUnit5에서 Mockito를 사용하기 위한 확장
class JwtRepositoryImplTest {

    @Mock // 가짜(Mock) 객체로 만듦
    private JwtJpaRepository jwtJpaRepository;

    @InjectMocks // @Mock으로 만든 객체를 주입하여 테스트 대상 클래스를 생성
    private JwtRepositoryImpl jwtRepositoryImpl;

    @Test
    @DisplayName("save: JWT 엔티티를 저장하면, JpaRepository의 save가 호출된다")
    void save_JwtEntity_ShouldCallJpaRepositorySave() {
        JwtEntity jwtEntity = new JwtEntity("accessToken", "refreshToken", "test@email.com", LocalDateTime.now(),
                LocalDateTime.now());
        given(jwtJpaRepository.save(any(JwtEntity.class))).willReturn(jwtEntity);

        // when
        JwtEntity savedEntity = jwtRepositoryImpl.save(jwtEntity);

        // then
        assertThat(savedEntity).isEqualTo(jwtEntity); // 반환된 객체가 예상과 같은지 확인
        then(jwtJpaRepository).should(times(1)).save(jwtEntity); // JpaRepository의 save가 1번 호출되었는지 검증
    }

    @Test
    @DisplayName("findJwtByUserEmail: 사용자 이메일로 조회 시, JpaRepository의 find가 호출되고 결과를 반환한다")
    void findJwtByUserEmail_WhenExists_ShouldReturnJwtEntity() {
        // given
        String email = "test@email.com";
        JwtEntity jwtEntity = new JwtEntity("accessToken", "refreshToken", "test@email.com", LocalDateTime.now(),
                LocalDateTime.now());
        given(jwtJpaRepository.findJwtEntityByEmail(email)).willReturn(Optional.of(jwtEntity));

        // when
        Optional<JwtEntity> result = jwtRepositoryImpl.findJwtByUserEmail(email);

        // then
        assertThat(result).isPresent();
        assertThat(result.get().getEmail()).isEqualTo(email);
        then(jwtJpaRepository).should().findJwtEntityByEmail(email);
    }

    @Test
    @DisplayName("findJwtByUserEmail: 존재하지 않는 이메일로 조회 시, 빈 Optional을 반환한다")
    void findJwtByUserEmail_WhenNotExists_ShouldReturnEmptyOptional() {
        // given
        String email = "non-existent@email.com";
        given(jwtJpaRepository.findJwtEntityByEmail(email)).willReturn(Optional.empty());

        // when
        Optional<JwtEntity> result = jwtRepositoryImpl.findJwtByUserEmail(email);

        // then
        assertThat(result).isEmpty();
        then(jwtJpaRepository).should().findJwtEntityByEmail(email);
    }

    @Test
    @DisplayName("deleteByUserEmail: 사용자 이메일로 삭제 시, JpaRepository의 deleteByEmail이 호출된다")
    void deleteByUserEmail_ShouldCallJpaRepositoryDelete() {
        // given
        String email = "test@email.com";

        // when
        jwtRepositoryImpl.deleteByUserEmail(email);

        // then
        then(jwtJpaRepository).should(times(1)).deleteByEmail(email); // deleteByEmail이 1번 호출되었는지 검증
    }

    @Test
    @DisplayName("findJwtByRefreshToken: 리프레시 토큰으로 조회 시, JpaRepository의 find가 호출된다")
    void findJwtByRefreshToken_ShouldReturnJwtEntity() {
        // given
        String refreshToken = "refresh-token";
        JwtEntity jwtEntity = new JwtEntity("accessToken", "refresh-token", "test@email.com", LocalDateTime.now(),
                LocalDateTime.now());
        given(jwtJpaRepository.findJwtByRefreshToken(refreshToken)).willReturn(Optional.of(jwtEntity));

        // when
        Optional<JwtEntity> result = jwtRepositoryImpl.findJwtByRefreshToken(refreshToken);

        // then
        assertThat(result).isPresent();
        assertThat(result.get().getRefreshToken()).isEqualTo(refreshToken);
        then(jwtJpaRepository).should().findJwtByRefreshToken(refreshToken);
    }
}