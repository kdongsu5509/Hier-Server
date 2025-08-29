package com.dt.find_restaurant.pin.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.dt.find_restaurant.pin.domain.Pin;
import com.dt.find_restaurant.pin.domain.PinRepository;
import com.dt.find_restaurant.pin.dto.PinDetailResponse;
import com.dt.find_restaurant.pin.dto.PinRequest;
import com.dt.find_restaurant.pin.dto.PinSimpleResponse;
import com.dt.find_restaurant.security.domain.User;
import com.dt.find_restaurant.security.domain.UserRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class PinServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PinRepository pinRepository;

    @InjectMocks
    private PinService pinService;

    private final String TEST_EMAIL = "test@example.com";
    private User testUser;
    private UUID testUuid;
    List<String> images = List.of("http://kakao.com/image1.png", "http://kakao.com/image2.png");

    @BeforeEach
    void setUp() {
        testUuid = UUID.randomUUID();
        // User.create() 정적 팩토리 메소드를 사용하여 테스트 객체 생성
        testUser = User.create(
                TEST_EMAIL,
                "password123",
                "ROLE_USER",
                "testuser",
                "http://example.com/profile.jpg"
        );
        // 테스트의 일관성을 위해 ReflectionTestUtils로 UUID를 주입
        ReflectionTestUtils.setField(testUser, "id", testUuid);
    }

    @Test
    @DisplayName("핀 생성 테스트 - 성공")
    void createPin() {
        //given
        PinRequest pinRequest = new PinRequest(
                "거제 맛집",
                "아주아주아주아주아주아주아주아주아주아주아주아주 맛있음",
                "http://kakao.com/image.png",
                35.123456,
                128.123456,
                "경상남도 거제시 아주동",
                "식당",
                images
        );
        UUID pinUuid = UUID.randomUUID();

        given(userRepository.findByEmail(TEST_EMAIL)).willReturn(Optional.of(testUser));
        given(pinRepository.saveAndReturnId(any(Pin.class))).willReturn(pinUuid);
        given(pinRepository.findByLat(any())).willReturn(Optional.empty()); // 핀 생성 시 이미 존재하는 핀 정보가 없음을 가정

        //when
        UUID savedPinId = pinService.createPin(TEST_EMAIL, pinRequest);

        //then
        assertThat(savedPinId).isEqualTo(pinUuid);
    }

    @Test
    @DisplayName("여러 개의 핀의 정보 잘 가져옴")
    void getAllPins() {
        //given
        String categoryName = "식당";
        Pin pin1 = Pin.createNewPin(
                "거제 맛집 1",
                "맛있음 1",
                "http://kakao.com/image1.png",
                new com.dt.find_restaurant.pin.domain.Address(35.123456, 128.123456, "경상남도 거제시 아주동"),
                categoryName,
                images

        );
        Pin pin2 = Pin.createNewPin(
                "거제 맛집 2",
                "맛있음 2",
                "http://kakao.com/image2.png",
                new com.dt.find_restaurant.pin.domain.Address(35.654321, 128.654321, "경상남도 거제시 아주동"),
                categoryName,
                images
        );
        User user1 = User.create(
                "test1@test.com",
                "password123",
                "ROLE_USER",
                "testuser",
                "http://example.com/profile.jpg"
        );
        // 테스트의 일관성을 위해 ReflectionTestUtils로 UUID를 주입
        ReflectionTestUtils.setField(testUser, "id", UUID.randomUUID());

        User user2 = User.create(
                "test2@test.com",
                "password123",
                "ROLE_USER",
                "testuser",
                "http://example.com/profile.jpg"
        );
        // 테스트의 일관성을 위해 ReflectionTestUtils로 UUID를 주입
        ReflectionTestUtils.setField(testUser, "id", UUID.randomUUID());

        //핀에 사용자 정보 설정
        pin1.updateUser(user1);
        pin2.updateUser(user2);

        given(pinRepository.findAll()).willReturn(List.of(pin1, pin2));

        //when
        List<PinSimpleResponse> allPins = pinService.getAllPins();

        //then
        assertThat(allPins).hasSize(2);
        assertThat(allPins.get(0).restaurantName()).isEqualTo("거제 맛집 1");
        assertThat(allPins.get(1).restaurantName()).isEqualTo("거제 맛집 2");
    }

    @Test
    @DisplayName("핀 ID로 핀 정보 조회")
    void getPinById() {
        //given
        String categoryName = "식당";
        Pin pin1 = Pin.createNewPin(
                "거제 맛집 1",
                "맛있음 1",
                "http://kakao.com/image1.png",
                new com.dt.find_restaurant.pin.domain.Address(35.123456, 128.123456, "경상남도 거제시 아주동"),
                categoryName,
                images
        );
        pin1.updateUser(testUser);
        ReflectionTestUtils.setField(pin1, "id", UUID.randomUUID());

        given(pinRepository.findById(any(UUID.class))).willReturn(Optional.of(pin1));

        //when
        PinDetailResponse pinById = pinService.getPinById(pin1.getId());

        //then
        assertThat(pinById).isNotNull();
        assertThat(pinById.restaurantName()).isEqualTo("거제 맛집 1");
        assertThat(pinById.text()).isEqualTo("맛있음 1");
        assertThat(pinById.mapUrl()).isEqualTo("http://kakao.com/image1.png");
        assertThat(pinById.address().getLatitude()).isEqualTo(35.123456);
        assertThat(pinById.address().getLongitude()).isEqualTo(128.123456);
        assertThat(pinById.address().getKoreanAddress()).isEqualTo("경상남도 거제시 아주동");
        assertThat(pinById.createdBy()).isEqualTo(testUser.getUserName());
    }

    //TODO : update 테스트는 JPA 의존성을 확인해야하기 때문에 차후 통합 테스트에서 작성 예정.
}