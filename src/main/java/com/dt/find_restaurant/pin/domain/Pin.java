package com.dt.find_restaurant.pin.domain;

import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

import com.dt.find_restaurant.global.domain.BaseEntity;
import com.dt.find_restaurant.pin.dto.PinUpdateRequest;
import com.dt.find_restaurant.security.domain.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Embedded;
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
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = PROTECTED)
public class Pin extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    String placeName;

    @Lob
    String text;
    String mapUrl;

    Double grade;

    @Embedded
    Address address;

    Long likeCount;

    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = LAZY)
    User user;

    String category;

    @OneToMany(mappedBy = "pin", cascade = CascadeType.ALL, orphanRemoval = true, fetch = LAZY)
    private List<PinImageEntity> images = new ArrayList<>();

    private Pin(String placeName, String text, String mapUrl, Double grade, Address address, String category,
                Long likeCount) {
        this.placeName = placeName;
        this.text = text;
        this.mapUrl = mapUrl;
        this.grade = grade;
        this.address = address;
        this.category = category;
        this.likeCount = likeCount;
    }

    public static Pin createNewPin(String restaurantName, String text, String mapUrl, Address address, String category,
                                   List<String> imageUrls) {
        Pin pin = new Pin(restaurantName, text, mapUrl, 0D, address, category, 0L);
        for (String imageUrl : imageUrls) {
            PinImageEntity pinImageEntity = PinImageEntity.create(imageUrl);
            pin.updateImage(pinImageEntity);
        }
        return pin;
    }

    public void updateUser(User user) {
        this.user = user;
    }

    public void updatePinByUser(PinUpdateRequest req) {
        //First. Update Adress Cause this is embedded object
        updateAddressToNewAddressObject(req);

        //Second. Update Others
        if (req.restaurantName() != null) {
            this.placeName = req.restaurantName();
        }
        if (req.text() != null) {
            this.text = req.text();
        }
        if (req.mapUrl() != null) {
            this.mapUrl = req.mapUrl();
        }
    }

    public void updateImage(PinImageEntity pinImageEntity) {
        this.images.add(pinImageEntity);
        pinImageEntity.setPin(this); // 연관관계 설정
    }

    private void updateAddressToNewAddressObject(PinUpdateRequest req) {
        if (req.latitude() != null) {
            this.address = new Address(
                    req.latitude(),
                    req.longitude(),
                    req.koreanAddress()
            );
        }
        if (req.longitude() != null) {
            this.address = new Address(
                    this.address.getLatitude(),
                    req.longitude(),
                    this.address.getKoreanAddress()
            );
        }
        if (req.koreanAddress() != null) {
            this.address = new Address(
                    this.address.getLatitude(),
                    this.address.getLongitude(),
                    req.koreanAddress()
            );
        }
    }
}
