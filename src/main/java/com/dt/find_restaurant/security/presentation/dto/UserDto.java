package com.dt.find_restaurant.security.presentation.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record UserDto(
        @NotNull(message = "이메일은 필수입니다.") @Email(message = "유효한 이메일 형식이 아닙니다.")
        String email,
        @NotNull(message = "비밀번호는 필수입니다.")
        String password,
        @NotNull(message = "사용자 이름은 필수입니다.")
        String username,
        @Nullable
        String profileImageUrl
) {
}
