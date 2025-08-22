package com.dt.find_restaurant.pin.domain;

import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

import com.dt.find_restaurant.global.util.BaseTimeEntity;
import com.dt.find_restaurant.pin.dto.PinUpdateRequest;
import com.dt.find_restaurant.security.domain.User;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = PROTECTED)
public class Pin extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    String restaurantName;

    @Lob
    String text;
    String kakaoMapUrl;

    Double grade;

    @Embedded
    Address address;

    Long likeCount;

    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = LAZY)
    User user;


    private Pin(String restaurantName, String text, String kakaoMapUrl, Double grade, Address address, Long likeCount) {
        this.restaurantName = restaurantName;
        this.text = text;
        this.kakaoMapUrl = kakaoMapUrl;
        this.grade = grade;
        this.address = address;
        this.likeCount = likeCount;
    }

    public static Pin createNewPin(String restaurantName, String text, String kakaoMapUrl, Address address) {
        return new Pin(restaurantName, text, kakaoMapUrl, 0D, address, 0L);
    }

    public void updateUser(User user) {
        this.user = user;
    }

    public void updatePinByUser(PinUpdateRequest req) {
        //First. Update Adress Cause this is embedded object
        updateAddressToNewAddressObject(req);

        //Second. Update Others
        if (req.restaurantName() != null) {
            this.restaurantName = req.restaurantName();
        }
        if (req.text() != null) {
            this.text = req.text();
        }
        if (req.kakaoMapUrl() != null) {
            this.kakaoMapUrl = req.kakaoMapUrl();
        }
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
