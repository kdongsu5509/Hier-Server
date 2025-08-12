package com.dt.find_restaurant.post.dto.request;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record Post(
        @Schema(
                description = "음식점 이름",
                example = "홍길동 맛집"
        )
        @NotNull
        String restaurantName,

        @Schema(
                description = "카카오 맵 URL",
                example = "https://map.kakao.com/link/to/홍길동 맛집,37.5665,126.978"
        )
        @NotNull
        String kakaoMapUrl,
        @Schema(
                description = "위도",
                example = "37.5665"
        )
        Double latitude,
        @Schema(
                description = "경도",
                example = "126.978"
        )
        @NotNull
        Double longitude,
        @Schema(
                description = "게시글 내용",
                example = "이 음식점은 정말 맛있어요!"
        )
        @NotNull
        String text,

        @Schema(
                description = "평점",
                example = "9.5"
        )
        Double grade
) {
}
