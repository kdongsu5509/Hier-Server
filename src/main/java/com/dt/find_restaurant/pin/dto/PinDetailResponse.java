package com.dt.find_restaurant.pin.dto;

import com.dt.find_restaurant.pin.domain.Address;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record PinDetailResponse(
        UUID id,
        String restaurantName,
        String text,
        String mapUrl,
        Double grade,
        Long likeCount,
        List<String> imageUrls,
        Boolean isLiked,
        String category,
        Address address,
        String createdBy,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
