package com.dt.find_restaurant.comment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record CommentRequest(
        @Schema(description = "댓글 작성자 이름", example = "홍길동")
        @NotNull
        String creatorName,

        @Schema(description = "별점", example = "9.5")
        @Nullable
        Double grade,

        @Schema(description = "댓글 사진 URL들", example = "[\"https://example.com/image1.jpg\", \"https://example.com/image2.jpg\"]")
        @Nullable
        List<String> imageUrl,

        @Schema(description = "리뷰 혹은 댓글 내용", example = "이 음식점은 정말 맛있어요!")
        @NotNull
        String text
) {
}
