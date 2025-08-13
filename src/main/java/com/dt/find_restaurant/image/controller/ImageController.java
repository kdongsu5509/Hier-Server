package com.dt.find_restaurant.image.controller;

import com.dt.find_restaurant.image.service.ImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Tag(
        name = "이미지",
        description = "이미지 관련 API"
)
@RestController
@RequestMapping("/image")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @Operation(
            summary = "이미지 업로드",
            description = "이미지를 업로드합니다. 파라미터에 파일을 포함해야 합니다."
    )
    @io.swagger.v3.oas.annotations.Parameter(
            name = "file",
            description = "업로드할 이미지 파일",
            required = true
    )
    @ApiResponses(
            value = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "이미지 업로드 성공"
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "400",
                            description = "잘못된 요청 (파일이 없거나 허용되지 않는 확장자)"
                    )
            }
    )
    @PostMapping
    public String uploadImage(@RequestParam("file") MultipartFile file) {
        return imageService.uploadImage(file);
    }

    @Operation(
            summary = "이미지 삭제",
            description = "이미지를 삭제합니다. 파라미터에 파일 URL을 포함해야 합니다."
    )
    @io.swagger.v3.oas.annotations.Parameter(
            name = "fileUrl",
            description = "삭제할 이미지의 S3 URL",
            required = true
    )
    @ApiResponses(
            value = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "이미지 삭제 성공"
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "400",
                            description = "잘못된 요청 (파일 URL이 비어있거나 잘못된 형식)"
                    )
            }
    )
    @DeleteMapping
    public void deleteImage(@RequestParam("fileUrl") String fileUrl) {
        imageService.deleteImage(fileUrl);
    }
}
