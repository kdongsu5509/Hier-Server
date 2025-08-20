package com.dt.find_restaurant.admin.dto;

import java.util.UUID;
import lombok.Builder;

@Builder
public record UserInfoResponseDto(
        UUID uuid,
        String email,
        String username,
        String profileImageUrl,
        String role,
        boolean isEnable
) {

}
