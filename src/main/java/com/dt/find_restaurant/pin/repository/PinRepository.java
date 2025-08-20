package com.dt.find_restaurant.pin.repository;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PinRepository extends JpaRepository<PinEntity, UUID> {
    Optional<PinEntity> findByLat(Double lat);
}
