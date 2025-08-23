package com.dt.find_restaurant.comment.domain;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.LAZY;

import com.dt.find_restaurant.global.util.BaseTimeEntity;
import com.dt.find_restaurant.pin.domain.Pin;
import com.dt.find_restaurant.security.domain.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
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

    @Lob
    private String comment;

    private Double grade;

    @Enumerated(STRING)
    private CommentType type;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL, orphanRemoval = true, fetch = LAZY)
    private List<CommentImageEntity> images = new ArrayList<>();

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "pin_id")
    private Pin pin;

    private Comment(String comment, Double grade, Pin pin, CommentType commentType) {
        this.comment = comment;
        this.grade = grade;
        this.pin = pin;
        this.type = commentType;
    }

    public static Comment create(String comment, Double grade, Pin pin, CommentType commentType) {
        return new Comment(comment, grade, pin, commentType);
    }

    public void updateImage(CommentImageEntity commentImageEntity) {
        this.images.add(commentImageEntity);
        commentImageEntity.setComment(this); // 연관관계 설정
    }

    public void updateUser(User user) {
        this.user = user;
    }

    public void updateComment(String comment, Double grade, CommentType type) {
        if(comment != null) {
            this.comment = comment;
        }
        if(grade != null) {
            this.grade = grade;
        }
        if(type != null) {
            this.type = type;
        }
    }
}
