package com.dt.find_restaurant.comment.repository;

import static jakarta.persistence.FetchType.LAZY;

import com.dt.find_restaurant.Pin.repository.PinEntity;
import com.dt.find_restaurant.global.util.BaseTimeEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
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

    private String creatorName;

    @Lob
    private String comment;

    private Double grade;

    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL, orphanRemoval = true, fetch = LAZY)
    private List<CommentImageEntity> images = new ArrayList<>();

    @JsonIgnore
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "pin_id")
    private PinEntity pin;

    private CommentEntity(String creatorName, String comment, Double grade, PinEntity pinEntity) {
        this.creatorName = creatorName;
        this.comment = comment;
        this.grade = grade;
        this.pin = pinEntity; // 연관관계 설정
    }

    public static CommentEntity create(String creatorName, String comment, Double grade, PinEntity pinEntity) {
        return new CommentEntity(creatorName, comment, grade, pinEntity);
    }

    public void addCommentImage(CommentImageEntity commentImageEntity) {
        this.images.add(commentImageEntity);
        commentImageEntity.setComment(this); // 연관관계 설정
    }
}
