package com.dt.find_restaurant.pin.domain;

import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository {
    Long save(Category category);

    Category findById(Long id);

    List<Category> findAll();

    void deleteById(Long id);
}
