package com.dt.find_restaurant.pin.domain;

import static jakarta.persistence.FetchType.LAZY;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "pin_images")
public class PinImageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String imageUrl;

    @Setter // 연관관계 편의 메서드에서 사용하기 위해 Setter 추가
    @ManyToOne(fetch = LAZY)
    @JsonIgnore
    @JoinColumn(name = "pin_id")
    private Pin pin;

    private PinImageEntity(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public static PinImageEntity create(String imageUrl) {
        return new PinImageEntity(imageUrl);
    }
}