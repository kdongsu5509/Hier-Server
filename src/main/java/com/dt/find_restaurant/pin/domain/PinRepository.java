package com.dt.find_restaurant.pin.domain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public interface PinRepository {
    UUID saveAndReturnId(Pin pin);

    Optional<Pin> findById(UUID id);

    Optional<Pin> findByLat(Double lat);

    List<Pin> findAll();

    void deleteById(UUID id);
}
