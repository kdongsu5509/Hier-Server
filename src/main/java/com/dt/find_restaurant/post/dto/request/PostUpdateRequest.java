package com.dt.find_restaurant.post.dto.request;

public record PostUpdateRequest(
        String restaurantName,
        String kakaoMapUrl,
        Double latitude,
        Double longitude,
        String text,
        Double grade
) {
    // 이제 클라이언트는 바꾸고 싶은 필드만 JSON에 담아 보내면 됩니다.
    // { "text": "내용만 바꿀래요!", "grade": 8.5 }
}
