package com.dt.find_restaurant.pin.dto;

import com.dt.find_restaurant.pin.domain.Address;
import com.dt.find_restaurant.pin.domain.Pin;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import org.hibernate.validator.constraints.Length;

public record PinRequest(
        @NotNull
        String restaurantName,
        @NotNull @Length(min = 30, message = "최소 30자 이상 입력해주세요.")
        String text,
        @NotNull
        String mapUrl,
        @NotNull
        Double latitude,
        @NotNull
        Double longitude,
        @NotNull
        String koreanAddress,
        @NotNull
        String category,
        @Schema(description = "Pin 사진 URL들", example = "[\"https://example.com/image1.jpg\", \"https://example.com/image2.jpg\"]")
        @NotNull
        List<String> imageUrl
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
                mapUrl,
                newPinAddress,
                imageUrl
        );
    }
}

