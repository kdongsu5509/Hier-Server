package com.dt.find_restaurant.Pin.dto;

import jakarta.validation.constraints.NotNull;

public record PinRequest(
        @NotNull
        String name,
        @NotNull
        Double lat,
        @NotNull
        Double lng,
        @NotNull
        String creatorName,
        @NotNull
        String pinKakaoMapUrl
) {
}
