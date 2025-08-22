package com.dt.find_restaurant.pin.infra;

import static org.assertj.core.api.Assertions.assertThat;

import com.dt.find_restaurant.pin.domain.Address;
import com.dt.find_restaurant.pin.domain.Pin;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class PinJpaRepositoryTest {

    @Autowired
    private PinJpaRepository pinJpaRepository;

    //Save Before Every Test
    private final Double LAT = 35.123456;
    private final Double LNG = 128.123456;
    private final String ADDRESS = "경상남도 거제시 아주동";
    private final String RESTAURANT_NAME = "거제 맛집";
    private final String DESCRIPTION = "아주아주아주아주아주아주아주아주아주아주아주아주아주아주아주아주 맛있음";
    private final String KAKAO_MAP_URL = "http://kakao.com/image.png";

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
                KAKAO_MAP_URL,
                addr
        );

        pinJpaRepository.save(newPin);
    }


    @Test
    void findByAddressLatitude() {
        //Given

        //when
        Optional<Pin> byAddresslatitude = pinJpaRepository.findByAddressLatitude(LAT);

        //then
        // .get()을 호출하는 것보다 isPresent()로 확인하는 것이 더 안전하고 명확합니다.
        assertThat(byAddresslatitude).isPresent();
    }
}