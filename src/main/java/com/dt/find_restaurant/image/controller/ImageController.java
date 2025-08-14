package com.dt.find_restaurant.image.controller;

import com.dt.find_restaurant.image.service.ImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
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
@RequestMapping("/api/images")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @Operation(
            summary = "이미지 업로드",
            description = "여러 이미지를 업로드합니다. 요청 파라미터에 파일들을 포함해야 합니다."
    )
    @io.swagger.v3.oas.annotations.Parameter(
            name = "files",
            description = "업로드할 이미지 파일들",
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
                            description = "잘못된 요청 (파일이 비어있거나 잘못된 형식)"
                    )
            }
    )
    @PostMapping
    public List<String> uploadImages(@RequestParam("files") List<MultipartFile> files) {
        // 서비스 계층의 메서드도 여러 파일을 처리하도록 변경된 메서드를 호출합니다.
        return imageService.uploadImages(files);
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
    @PostMapping("/delete")
    public void deleteImage(@RequestParam("fileUrl") String fileUrl) {
        imageService.deleteImage(fileUrl);
    }
}
