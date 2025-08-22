package com.dt.find_restaurant.pin.application;

import com.dt.find_restaurant.pin.domain.CategoryRepository;
import com.dt.find_restaurant.pin.dto.CategoryResponseDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<CategoryResponseDto> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(category -> new CategoryResponseDto(
                        category.getName()))
                .toList();
    }
}
