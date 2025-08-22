package com.dt.find_restaurant.pin.dto;

import com.dt.find_restaurant.pin.domain.Address;
import java.time.LocalDateTime;

public record PinDetailResponse(
//        UUID id,
        String restaurantName,
        String text,
        String kakaoMapUrl,
        Double grade,
        Long likeCount,
        Address address,
        String createdBy,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
