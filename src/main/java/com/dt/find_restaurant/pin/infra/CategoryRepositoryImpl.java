package com.dt.find_restaurant.pin.infra;

import com.dt.find_restaurant.pin.domain.Category;
import com.dt.find_restaurant.pin.domain.CategoryRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CategoryRepositoryImpl implements CategoryRepository {

    private final CategoryJpaRepository repository;

    @Override
    public Long save(Category category) {
        return repository.save(category).getId();
    }

    @Override
    public Category findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Category not found with id: " + id));
    }

    @Override
    public List<Category> findAll() {
        return repository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
