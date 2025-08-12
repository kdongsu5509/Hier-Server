package com.dt.find_restaurant.comment.repository;

import static jakarta.persistence.EnumType.*;
import static jakarta.persistence.FetchType.*;

import com.dt.find_restaurant.comment.dto.CommentUpdateRequest;
import com.dt.find_restaurant.global.util.BaseTimeEntity;
import com.dt.find_restaurant.post.repository.PostEntity;
import com.dt.find_restaurant.user.domain.User;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Getter
@Entity
@Slf4j
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "post_id")
    private PostEntity post;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_email")
    private User user;

    private String imageUrl;

    @Lob
    private String text;

    private Double grade;

    @Enumerated(STRING)
    private CommentType type;

    private Comment(PostEntity post, User user, String imageUrl, String text, Double grade, CommentType type) {
        this.post = post;
        this.user = user;
        this.imageUrl = imageUrl;
        this.text = text;
        this.grade = grade;
        this.type = type;
    }

    public static Comment create(PostEntity post, User user, String imageUrl, String text, Double grade,
                                 CommentType type) {
        return new Comment(post, user, imageUrl, text, grade, type);
    }

    public void updateFrom(CommentUpdateRequest request) {
        if (request.text() != null) {
            this.text = request.text();
        }
        if (request.imageUrl() != null) {
            this.imageUrl = request.imageUrl();
        }
        if (request.grade() != null) {
            this.grade = request.grade();
        }
        if (request.commentType() != null) {
            CommentType commentType = CommentType.valueOf(request.commentType());
            this.type = commentType;
        }
    }

}
