package com.dt.find_restaurant.Pin.service;

import com.dt.find_restaurant.Pin.dto.PinRequest;
import com.dt.find_restaurant.Pin.repository.PinEntity;
import com.dt.find_restaurant.Pin.repository.PinRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PinService {

    private final PinRepository pinRepository;

    public PinEntity createPin(PinRequest req) {
        PinEntity pinEntity = PinEntity.toPinEntity(req);
        return pinRepository.save(pinEntity);
    }

    public void deletePin(UUID pinId) {
        pinRepository.deleteById(pinId);
    }

    public List<PinEntity> getAllPins() {
        return pinRepository.findAll();
    }

    public PinEntity getPinById(UUID pinId) {
        return pinRepository.findById(pinId)
                .orElseThrow(() -> new IllegalArgumentException("핀 정보가 존재하지 않습니다."));
    }
}
