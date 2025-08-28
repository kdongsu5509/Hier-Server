package com.dt.find_restaurant.comment.dto;

import com.dt.find_restaurant.comment.domain.CommentType;
import java.time.LocalDateTime;
import java.util.UUID;

public record CommentResponse(
        UUID id,
        String comment,
        Double grade,
        CommentType type,
        String username,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
