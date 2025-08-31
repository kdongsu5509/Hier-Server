package com.dt.find_restaurant.pin.dto;

import com.dt.find_restaurant.pin.domain.Address;
import com.dt.find_restaurant.pin.domain.Pin;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import org.hibernate.validator.constraints.Length;

public record PinRequest(
        @NotNull(
                message = "음식점 이름은 필수입니다."
        )
        String restaurantName,
        @NotNull(message = "글은 필수입니다.") @Length(min = 30, message = "최소 30자 이상 입력해주세요.")
        String text,
        @NotNull(message = "지도 URL은 필수입니다.")
        String mapUrl,
        @NotNull(message = "위도는 필수입니다.")
        Double latitude,
        @NotNull(message = "경도는 필수입니다.")
        Double longitude,
        @NotNull(message = "한글 주소는 필수입니다.")
        String koreanAddress,
        @NotNull(message = "카테고리는 필수입니다.")
        String category,
        @Schema(description = "Pin 사진 URL들", example = "[\"https://example.com/image1.jpg\", \"https://example.com/image2.jpg\"]")
        @NotNull(message = "이미지 URL은 필수입니다.")
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
                category,
                imageUrl
        );
    }
}

