package com.dt.find_restaurant.bookMark.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record BookMarkResponseDto(
        UUID pinId,
        String placeName,
        String category,
        String koreanAddress,
        LocalDateTime createdAt
) {
}
