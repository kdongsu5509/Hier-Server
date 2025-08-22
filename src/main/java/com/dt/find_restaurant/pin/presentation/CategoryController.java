package com.dt.find_restaurant.pin.presentation;

import com.dt.find_restaurant.global.response.APIResponse;
import com.dt.find_restaurant.pin.application.CategoryService;
import com.dt.find_restaurant.pin.dto.CategoryResponseDto;
import io.swagger.v3.oas.annotations.Operation;
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

    @Operation(
            summary = "모든 카테고리 조회",
            description = "모든 핀 카테고리를 조회합니다."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses(
            value = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "카테고리 조회 성공",
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    mediaType = "application/json",
                                    schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = CategoryResponseDto.class)
                            )
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "500",
                            description = "서버 오류"
                    )
            }
    )
    @GetMapping("/all")
    public APIResponse<List<CategoryResponseDto>> getAllCategories() {
        List<CategoryResponseDto> allCategories = categoryService.getAllCategories();
        return APIResponse.success(allCategories);
    }
}
