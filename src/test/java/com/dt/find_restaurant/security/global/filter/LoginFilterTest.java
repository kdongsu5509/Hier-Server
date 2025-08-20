package com.dt.find_restaurant.security.global.filter;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.dt.find_restaurant.security.domain.User;
import com.dt.find_restaurant.security.domain.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional // 테스트 후 DB 롤백
class LoginFilterTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    // 테스트에 사용할 사용자 정보
    private final String testEmail = "test@user.com";
    private final String testPassword = "password123";

    @BeforeEach
    void setUp() {
        User user = User.create(
                testEmail,
                passwordEncoder.encode(testPassword),
                "USER",
                "Test User",
                null,
                true
        );
        userRepository.save(user);
    }

    @Test
    @DisplayName("로그인 성공: 올바른 정보로 로그인 시, 200 OK와 JWT 토큰을 반환한다")
    void login_WithValidCredentials_ShouldSucceed() throws Exception {
        // given

        // when & then
        mockMvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED) // 중요: JSON이 아닌 Form 데이터
                        .param("username", testEmail) // obtainUsername(request)가 읽는 파라미터
                        .param("password", testPassword)
                )
                .andExpect(status().isOk()) // 200 OK 상태 코드 확인
                .andExpect(jsonPath("$.accessToken").exists()) // accessToken이 존재하는지 확인
                .andExpect(jsonPath("$.refreshToken").exists()) // refreshToken이 존재하는지 확인
                .andDo(print()); // 요청/응답 내용 출력
    }

    @Test
    @DisplayName("로그인 실패: 비밀번호가 틀리면 401 Unauthorized를 반환한다")
    void login_WithInvalidPassword_ShouldFail() throws Exception {
        // given
        String wrongPassword = "wrongpassword";

        // when & then
        mockMvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("username", testEmail)
                        .param("password", wrongPassword)
                )
                .andExpect(status().isUnauthorized()) // 401 Unauthorized 상태 코드 확인
                .andExpect(jsonPath("$.error").exists()) // 에러 메시지가 존재하는지 확인
                .andDo(print());
    }

    @Test
    @DisplayName("로그인 실패: 존재하지 않는 사용자로 로그인 시도 시 401 Unauthorized를 반환한다")
    void login_WithNonExistentUser_ShouldFail() throws Exception {
        // given
        String nonExistentEmail = "non-existent@user.com";

        // when & then
        mockMvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("username", nonExistentEmail)
                        .param("password", testPassword)
                )
                .andExpect(status().isUnauthorized()) // 401 Unauthorized 상태 코드 확인
                .andDo(print());
    }
}