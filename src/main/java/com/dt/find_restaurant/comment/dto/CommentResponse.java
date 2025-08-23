package com.dt.find_restaurant.comment.dto;

import com.dt.find_restaurant.comment.domain.CommentType;
import java.time.LocalDateTime;

public record CommentResponse(
        String comment,
        Double grade,
        CommentType type,
        String username,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
