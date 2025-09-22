package dsko.hier.security.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import dsko.hier.security.dto.EmailCheckDto;
import dsko.hier.security.dto.EmailSignUpDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest // 전체 애플리케이션 컨텍스트를 로드합니다.
@Transactional // 테스트 후 DB 롤백을 보장합니다.
class EmailAccountControllerTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();
    }

    @Test
    @DisplayName("[회원가입:성공] 이메일로 회원가입 요청 시 성공적으로 처리한다")
    void signUpViaEmailAndPassword_success() throws Exception {
        // Given
        EmailSignUpDto requestDto = new EmailSignUpDto("test@example.com", "testuser", "password123");

        // When & Then
        mockMvc.perform(post("/api/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").exists());
    }

    @Test
    @DisplayName("[이메일 중복 체크] : 중복된 이메일이 없으면 200 OK 반환")
    void checkEmailNotDuplicate_success() throws Exception {
        // Given
        EmailCheckDto emailCheckDto = new EmailCheckDto("unique@test.com");

        // When & Then
        mockMvc.perform(post("/api/signup/check-email")
                        .content(objectMapper.writeValueAsString(emailCheckDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}