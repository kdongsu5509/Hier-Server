package com.dt.find_restaurant.pin.dto;

public record AddressResponse(
        Double latitude,
        Double longitude,
        String koreanAddress
) {
}
