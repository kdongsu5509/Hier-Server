package com.dt.find_restaurant.pin.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Min;

public record PinUpdateRequest(
        @Nullable
        String restaurantName,
        @Nullable @Min(value = 30, message = "최소 30자 이상 입력해주세요.")
        String text,
        @Nullable
        String category,
        @Nullable
        String kakaoMapUrl,
        @Nullable
        Double latitude,
        @Nullable
        Double longitude,
        @Nullable
        String koreanAddress
) {
}
