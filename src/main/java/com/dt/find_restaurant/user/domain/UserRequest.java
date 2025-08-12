package com.dt.find_restaurant.user.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserRequest {

    @Getter
    public static class SignUp {

        @Schema(description = "사용자 이메일", example = "user@example.com", type = "string")
        @NotBlank(message = "이메일이 누락되었습니다.")
        @Email(message = "이메일 형식이 잘못되었습니다.")
        private String email;

        @Schema(description = "사용자 비밀번호 (최소 8자)", example = "password123", type = "string")
        @NotBlank(message = "비밀번호가 누락되었습니다.")
        @Size(min = 8, message = "비밀번호는 최소 8자 이상이어야 합니다.")
        private String password;

        @Schema(description = "사용자 이름", example = "홍길동", type = "string")
        @NotBlank(message = "이름이 누락되었습니다.")
        @Size(max = 10, message = "이름의 길이를 줄여주세요")
        private String name;
    }
}
