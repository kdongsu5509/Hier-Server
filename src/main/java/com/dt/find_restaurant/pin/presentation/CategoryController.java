package com.dt.find_restaurant.pin.presentation;

import com.dt.find_restaurant.pin.application.CategoryService;
import com.dt.find_restaurant.pin.dto.CategoryResponseDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(
        name = "핀 카테고리",
        description = "핀 카테고리 관련 API"
)
@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService; // 서비스 계층 의존성 주입

    @GetMapping("/all")
    public List<CategoryResponseDto> getAllCategories() {
        return categoryService.getAllCategories();
    }
}
