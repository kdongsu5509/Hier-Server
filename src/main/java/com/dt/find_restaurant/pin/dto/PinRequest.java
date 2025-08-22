package com.dt.find_restaurant.pin.dto;

import com.dt.find_restaurant.pin.domain.Address;
import com.dt.find_restaurant.pin.domain.Pin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record PinRequest(
        @NotNull
        String restaurantName,
        @NotNull @Min(value = 30, message = "최소 30자 이상 입력해주세요.")
        String text,
        @NotNull
        String kakaoMapUrl,
        @NotNull
        Double latitude,
        @NotNull
        Double longitude,
        @NotNull
        String koreanAddress
) {
    public Pin toPin() {
        Address newPinAddress = new Address(
                latitude,
                longitude,
                koreanAddress
        );
        return Pin.createNewPin(
                restaurantName,
                text,
                kakaoMapUrl,
                newPinAddress
        );
    }
}

