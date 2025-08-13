package com.dt.find_restaurant.post.repository;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

import com.dt.find_restaurant.comment.repository.Comment;
import com.dt.find_restaurant.global.util.BaseTimeEntity;
import com.dt.find_restaurant.post.dto.request.PostUpdateRequest;
import com.dt.find_restaurant.user.domain.User;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostEntity extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_email")
    private User user;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "post")
    private List<Comment> comments = new ArrayList<>();

    private Long commentCount;

    private String restaurantName;

    private Double latitude;
    private Double longitude;

    @Lob
    private String text;

    private Double grade;

    private String kakaoMapUrl;

    private PostEntity(String restaurantName, Double latitude, Double longitude, String text, Double grade,
                       String kakaoMapUrl) {
        this.restaurantName = restaurantName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.text = text;
        this.grade = grade;
        this.kakaoMapUrl = kakaoMapUrl;
        this.commentCount = 0L; // 초기 댓글 수는 0으로 설정
    }

    public static PostEntity create(String restaurantName, Double latitude, Double longitude, String text, Double grade,
                                    String kakaMapUrl) {
        return new PostEntity(restaurantName, latitude, longitude, text, grade, kakaMapUrl);
    }

    public void updateFrom(PostUpdateRequest request) {
        if (request.restaurantName() != null) {
            this.restaurantName = request.restaurantName();
        }
        if (request.latitude() != null) {
            this.latitude = request.latitude();
        }
        if (request.longitude() != null) {
            this.longitude = request.longitude();
        }
        if (request.text() != null) {
            this.text = request.text();
        }
        if (request.kakaoMapUrl() != null) {
            this.kakaoMapUrl = request.kakaoMapUrl();
        }
    }

    public void addCommentCount() {
        this.commentCount++;
    }

    public void minusCommentCount() {
        if (this.commentCount > 0) {
            this.commentCount--;
        }
    }
}
