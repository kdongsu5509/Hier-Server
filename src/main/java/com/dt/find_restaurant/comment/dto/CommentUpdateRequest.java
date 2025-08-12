package com.dt.find_restaurant.comment.dto;

public record CommentUpdateRequest(
        String text,
        Double grade,
        String imageUrl,
        String commentType
) {
}
