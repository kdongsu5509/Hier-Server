package com.dt.find_restaurant.user.dto;

public record UserInfoResponseDto(
        String email,
        String password,
        String userName,
        String profileImageUrl
) {
}
