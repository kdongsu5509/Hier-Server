package com.dt.find_restaurant.pin.infra;

import com.dt.find_restaurant.pin.domain.Pin;
import com.dt.find_restaurant.pin.domain.PinRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PinRepositoryImpl implements PinRepository {

    private final PinJpaRepository jpaRepository;

    @Override
    public UUID saveAndReturnId(Pin pin) {
        Pin save = jpaRepository.save(pin);
        return save.getId();
    }

    public Optional<Pin> findById(UUID id) {
        return jpaRepository.findById(id);
    }

    @Override
    public Optional<Pin> findByLat(Double lat) {
        return jpaRepository.findByAddressLatitude(lat);
    }

    @Override
    public List<Pin> findAll() {
        return jpaRepository.findAll();
    }

    @Override
    public void deleteById(UUID id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public List<Pin> findByUserEmail(String userEmail) {
        return List.of();
    }
}
