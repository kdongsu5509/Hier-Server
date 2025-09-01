package com.dt.find_restaurant.pin.infra;

import com.dt.find_restaurant.pin.domain.Pin;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PinJpaRepository extends JpaRepository<Pin, UUID> {
    Optional<Pin> findByAddressLatitude(Double latitude);

    @Override
    @EntityGraph(attributePaths = {"user", "category"})
    List<Pin> findAll();

    @Override
    @EntityGraph(attributePaths = {"user", "category"})
    Optional<Pin> findById(UUID id);

    @EntityGraph(attributePaths = {"user", "category"})
    List<Pin> findByUserEmail(String email);
}
