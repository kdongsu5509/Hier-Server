package com.dt.find_restaurant.comment.dto;

import com.dt.find_restaurant.comment.repository.CommentType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

public record CommentRequest(
        @Schema(description = "댓글 사진 URL", example = "https://example.com/image.jpg")
        @NotNull
        String imageUrl,
        @Schema(description = "리뷰 혹은 댓글 내용", example = "이 음식점은 정말 맛있어요!")
        @NotNull
        String text,
        @Schema(description = "리뷰", example = "9.5")
        @Nullable
        Double grade,
        @Schema(description = "댓글 타입", example = "NORMAL or REVIEW")
        @NotNull
        CommentType commentType


) {
}
