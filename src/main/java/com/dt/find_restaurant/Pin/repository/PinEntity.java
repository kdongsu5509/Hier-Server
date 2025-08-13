package com.dt.find_restaurant.Pin.repository;

import static lombok.AccessLevel.PROTECTED;

import com.dt.find_restaurant.Pin.dto.PinRequest;
import com.dt.find_restaurant.global.util.BaseTimeEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = PROTECTED)
public class PinEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    String name;
    Double lat;
    Double lng;
    String creatorName;
    String pinKakaoMapUrl;

    private PinEntity(String name, Double lat, Double lng, String creatorName, String pinKakaoMapUrl) {
        this.name = name;
        this.lat = lat;
        this.lng = lng;
        this.creatorName = creatorName;
        this.pinKakaoMapUrl = pinKakaoMapUrl;
    }

    public static PinEntity create(String name, Double lat, Double lng, String creatorName, String pinKakaoMapUrl) {
        return new PinEntity(name, lat, lng, creatorName, pinKakaoMapUrl);
    }

    public static PinEntity toPinEntity(PinRequest pinRequest) {
        return PinEntity.create(
                pinRequest.name(),
                pinRequest.lat(),
                pinRequest.lng(),
                pinRequest.creatorName(),
                pinRequest.pinKakaoMapUrl()
        );
    }
}
