package com.dt.find_restaurant.comment.repository;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.LAZY;

import com.dt.find_restaurant.global.util.BaseTimeEntity;
import com.dt.find_restaurant.pin.domain.Pin;
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
public class CommentEntity extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @Lob
    private String comment;

    private Double grade;

    @Enumerated(STRING)
    private CommentType type;

    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL, orphanRemoval = true, fetch = LAZY)
    private List<CommentImageEntity> images = new ArrayList<>();

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "pin_id")
    private Pin pin;

    private CommentEntity(String comment, Double grade, Pin pin, CommentType commentType) {
        this.comment = comment;
        this.grade = grade;
        this.pin = pin;
        this.type = commentType;
    }

    public static CommentEntity create(String comment, Double grade, Pin pin, CommentType commentType) {
        return new CommentEntity(comment, grade, pin, commentType);
    }

    public void addCommentImage(CommentImageEntity commentImageEntity) {
        this.images.add(commentImageEntity);
        commentImageEntity.setComment(this); // 연관관계 설정
    }
}
