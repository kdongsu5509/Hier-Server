package com.dt.find_restaurant.pin.dto;

import com.dt.find_restaurant.pin.domain.Address;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 간단한 핀 정보만을 응답에 사용하는 DTO입니다.
 */
public record PinSimpleResponse(
        UUID id,
        String restaurantName,
        String kakaoMapUrl,
        Double grade,
        Long likeCount,
        Address address,
        String category,
        String createdBy,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
