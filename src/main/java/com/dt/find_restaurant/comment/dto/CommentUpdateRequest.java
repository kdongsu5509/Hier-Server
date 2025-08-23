package com.dt.find_restaurant.comment.dto;

import com.dt.find_restaurant.comment.domain.CommentType;
import jakarta.annotation.Nullable;

public record CommentUpdateRequest(
        @Nullable
        String comment,
        @Nullable
        Double grade,
        @Nullable
        CommentType commentType
) {
}
