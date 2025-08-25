package com.dt.find_restaurant.bookMark.dto;

import java.util.UUID;

public record BookMarkResponseDto(
        UUID pinId,
        String placeName
) {
}
