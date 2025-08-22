package com.dt.find_restaurant.pin.infra;

import com.dt.find_restaurant.pin.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryJpaRepository extends JpaRepository<Category, Long> {
}
