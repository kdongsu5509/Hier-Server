package com.dt.find_restaurant.security.presentation.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record UserDto(
        @NotNull @Email
        String email,
        @NotNull
        String password,
        @NotNull
        String userName,
        @Nullable
        String profileImageUrl,
        @NotNull
        String role //NORMAL, ADMIN
) {
}
