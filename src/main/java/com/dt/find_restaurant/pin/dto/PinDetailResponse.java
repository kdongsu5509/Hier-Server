package com.dt.find_restaurant.pin.dto;

import com.dt.find_restaurant.pin.domain.Address;
import java.time.LocalDateTime;
import java.util.UUID;

public record PinDetailResponse(
        UUID id,
        String restaurantName,
        String text,
        String kakaoMapUrl,
        Double grade,
        Long likeCount,
        String category,
        Address address,
        String createdBy,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
