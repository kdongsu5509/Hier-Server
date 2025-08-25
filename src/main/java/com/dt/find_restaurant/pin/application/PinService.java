package com.dt.find_restaurant.pin.application;

import com.dt.find_restaurant.global.exception.CustomExceptions.PinException;
import com.dt.find_restaurant.global.exception.CustomExcpMsgs;
import com.dt.find_restaurant.pin.domain.Category;
import com.dt.find_restaurant.pin.domain.Pin;
import com.dt.find_restaurant.pin.domain.PinRepository;
import com.dt.find_restaurant.pin.dto.PinDetailResponse;
import com.dt.find_restaurant.pin.dto.PinRequest;
import com.dt.find_restaurant.pin.dto.PinSimpleResponse;
import com.dt.find_restaurant.pin.dto.PinUpdateRequest;
import com.dt.find_restaurant.security.domain.User;
import com.dt.find_restaurant.security.domain.UserRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class PinService {

    private final UserRepository userRepository;
    private final PinRepository pinRepository;

    public UUID createPin(String userEmail, PinRequest req) {
        notAllowAlreadyExistInfomation(req);

        Pin newPin = req.toPin();

        Category categoryObj = Category.create(req.category());
        newPin.updateCategory(categoryObj);

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new PinException(CustomExcpMsgs.USER_NOT_FOUND.getMessage() + userEmail));
        newPin.updateUser(user);

        return pinRepository.saveAndReturnId(newPin);
    }

    public List<PinSimpleResponse> getAllPins() {
        //1. 모든 핀을 가져온다.
        return pinRepository.findAll()
                .stream()
                .map(this::toPinSimpleResponse)
                .toList();
    }

    public PinDetailResponse getPinById(UUID pinId) {
        return toPinDetailResponse(getRequestPinFromDatabase(pinId));
    }

    public void updatePin(String userEmail, UUID pinId, PinUpdateRequest req) {
        //2. 핀 정보가 존재하는지 확인
        Pin findPin = getRequestPinFromDatabase(pinId);
        //1. 해당 핀이 내가 생성한 핀인지 확인.
        isMyPin(userEmail, findPin);
        //3. 핀 정보가 존재한다면 업데이트
        findPin.updatePinByUser(req);
    }


    public void deletePin(String userEmail, UUID pinId) {
        //1. 핀 정보가 존재하는지 확인
        Pin findPin = getRequestPinFromDatabase(pinId);
        //2. 해당 핀이 내가 생성한 핀인지 확인.
        isMyPin(userEmail, findPin);
        //3. 핀 정보가 존재한다면 삭제
        pinRepository.deleteById(pinId);
    }

    private Pin getRequestPinFromDatabase(UUID pinId) {
        return pinRepository.findById(pinId).orElseThrow(
                () -> new PinException(CustomExcpMsgs.PIN_NOT_FOUND.getMessage())
        );
    }

    private static void isMyPin(String userEmail, Pin findPin) {
        if (!findPin.getUser().getEmail().equals(userEmail)) {
            throw new PinException(CustomExcpMsgs.PIN_UNAUTHORIZED.getMessage());
        }
    }

    private void notAllowAlreadyExistInfomation(PinRequest req) {
        Optional<Pin> byLat = pinRepository.findByLat(req.latitude());
        boolean isSameLongitude = byLat.isPresent() && byLat.get().getAddress().getLongitude().equals(req.longitude());
        if (byLat.isPresent() && isSameLongitude) {
            throw new PinException(CustomExcpMsgs.ALREADY_EXISTS.getMessage());
        }
    }


    private PinSimpleResponse toPinSimpleResponse(Pin pin) {
        return new PinSimpleResponse(
                pin.getId(),
                pin.getPlaceName(),
                pin.getKakaoMapUrl(),
                pin.getGrade(),
                pin.getLikeCount(),
                pin.getAddress(),
                pin.getCategory().getName(),
                pin.getUser().getUserName(),
                pin.getCreatedAt(),
                pin.getUpdatedAt()
        );
    }

    private PinDetailResponse toPinDetailResponse(Pin pin) {
        return new PinDetailResponse(
                pin.getPlaceName(),
                pin.getText(),
                pin.getKakaoMapUrl(),
                pin.getGrade(),
                pin.getLikeCount(),
                pin.getCategory().getName(),
                pin.getAddress(),
                pin.getUser().getUserName(),
                pin.getCreatedAt(),
                pin.getUpdatedAt()
        );
    }
}
