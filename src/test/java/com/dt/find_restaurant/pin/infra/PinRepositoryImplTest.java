package com.dt.find_restaurant.pin.infra;

import static org.assertj.core.api.Assertions.assertThat;

import com.dt.find_restaurant.pin.domain.Address;
import com.dt.find_restaurant.pin.domain.Pin;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class PinRepositoryImplTest {

    @Autowired
    private PinRepositoryImpl pinRepositoryImpl;

    private final Double LAT = 35.123456;
    private final Double LNG = 128.123456;
    private final String ADDRESS = "경상남도 거제시 아주동";
    private final String RESTAURANT_NAME = "거제 맛집";
    private final String DESCRIPTION = "아주아주아주아주아주아주아주아주아주아주아주아주아주아주아주아주 맛있음";
    private final String MAP_URL = "http://kakao.com/image.png";
    private final List<String> IMAGES = List.of(
            "http://kakao.com/image1.png",
            "http://kakao.com/image2.png"
    );

    @BeforeEach
    void setUp() {
        Address addr = new Address(
                LAT,
                LNG,
                ADDRESS
        );
        Pin newPin = Pin.createNewPin(
                RESTAURANT_NAME,
                DESCRIPTION,
                MAP_URL,
                addr,
                IMAGES
        );

        pinRepositoryImpl.saveAndReturnId(newPin);
    }

    @Test
    @DisplayName("정상적인 핀 저장의 경우 성공함.")
    void test_success() {
        //given
        Address addr = new Address(
                35.123456,
                128.123456,
                "경상남도 거제시 아주동"
        );
        Pin newPin = Pin.createNewPin(
                "거제 맛집",
                "아주아주아주아주아주아주아주아주아주아주아주아주아주아주아주아주 맛있음",
                "http://kakao.com/image.png",
                addr,
                IMAGES
        );

        //when
        UUID save = pinRepositoryImpl.saveAndReturnId(newPin);

        //then
        assertThat(save).isNotNull();
        assertThat(pinRepositoryImpl.findById(save)).isNotNull();
    }

    @Test
    @DisplayName("위도에 해당하는 핀을 찾을 수 있다.")
    void test_findByLat() {
        // given
        Double lat = 35.123456;

        // when
        var pinOptional = pinRepositoryImpl.findByLat(lat);

        // then
        assertThat(pinOptional).isPresent();
        assertThat(pinOptional.get().getAddress().getLatitude()).isEqualTo(lat);
        assertThat(pinOptional.get().getAddress().getLongitude()).isEqualTo(LNG);
    }
}