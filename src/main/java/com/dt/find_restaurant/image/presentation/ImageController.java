package com.dt.find_restaurant.image.presentation;

import com.dt.find_restaurant.global.response.APIResponse;
import com.dt.find_restaurant.image.application.ImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(
        name = "이미지",
        description = "이미지 관련 API"
)
@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @Operation(
            summary = "프리사인드 URL 1개 생성",
            description = "주어진 파일 1개의 이름으로 S3에 이미지를 업로드하기 위한 프리사인드 URL을 생성합니다. 요청 본문에 파일 이름을 포함해야 합니다."
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "프리사인드 URL 생성 요청 본문",
            required = true,
            content = @io.swagger.v3.oas.annotations.media.Content(
                    mediaType = "application/json",
                    schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = String.class)
            )
    )
    @ApiResponses(
            value = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "프리사인드 URL 생성 성공"
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "400",
                            description = "잘못된 요청 (파일 이름이 비어있거나 잘못된 형식)"
                    )
            }
    )
    @PostMapping("/presigned-url")
    public APIResponse<Map<String, String>> getPresignedUrl(
            @RequestBody @NotNull String fileName) {
        return APIResponse.success(imageService.getPresignedUrl("image", fileName));
    }


    @Operation(
            summary = "프리사인드 URL 여러개 생성",
            description = "주어진 파일 여러개의 이름으로 S3에 이미지를 업로드하기 위한 프리사인드 URL들을 생성합니다. 요청 본문에 파일 이름 리스트를 포함해야 합니다."
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "프리사인드 URL 생성 요청 본문",
            required = true,
            content = @io.swagger.v3.oas.annotations.media.Content(
                    mediaType = "application/json",
                    schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = List.class)
            )
    )
    @ApiResponses(
            value = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "프리사인드 URL 생성 성공"
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "400",
                            description = "잘못된 요청 (파일 이름 리스트가 비어있거나 잘못된 형식)"
                    )
            }
    )
    @PostMapping("/presigned-urls")
    public APIResponse<Map<String, String>> getPresignedUrl(
            @RequestBody @NotNull List<String> fileNames) {
        return APIResponse.success(imageService.getPresignedUrl("images", fileNames));
    }

    @Operation(
            summary = "이미지 삭제",
            description = "이미지를 삭제합니다. 파라미터에 파일 URL을 포함해야 합니다."
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "삭제할 이미지 파일 이름",
            required = true,
            content = @io.swagger.v3.oas.annotations.media.Content(
                    mediaType = "application/json",
                    schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = String.class)
            )
    )
    @ApiResponses(
            value = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "이미지 삭제 성공"
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "400",
                            description = "잘못된 요청 (파일 이름이 비어있거나 잘못된 형식)"
                    )
            }
    )
    @PostMapping("/delete")
    public APIResponse<Map<String, String>> deleteImage(@RequestBody @NotNull String fileName) {
        Map<String, String> deletePreSingedUrl = imageService.getDeletePreSingedUrl(fileName);
        return APIResponse.success(deletePreSingedUrl);
    }
}
