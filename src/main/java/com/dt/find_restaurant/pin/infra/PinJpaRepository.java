package com.dt.find_restaurant.pin.infra;

import com.dt.find_restaurant.pin.domain.Pin;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PinJpaRepository extends JpaRepository<Pin, UUID> {
    Optional<Pin> findByAddressLatitude(Double latitude);
}
