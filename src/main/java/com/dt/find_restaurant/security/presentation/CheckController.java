package com.dt.find_restaurant.security.presentation;

import com.dt.find_restaurant.global.response.APIResponse;
import com.dt.find_restaurant.security.application.UserService;
import com.dt.find_restaurant.security.presentation.dto.TargetDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(
        name = "검증",
        description = "검증 관련 API"
)
@Slf4j
@RestController
@RequestMapping("/api/check")
@RequiredArgsConstructor
public class CheckController {

    private final UserService userService;

    @Operation(
            summary = "이메일 중복 확인",
            description = "주어진 이메일이 이미 사용 중인지 확인합니다."
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "중복 확인할 이메일",
            required = true,
            content = @io.swagger.v3.oas.annotations.media.Content(
                    mediaType = "application/json",
                    schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = TargetDto.class)
            )
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses(
            value = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "이메일 중복 확인 성공",
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    mediaType = "application/json",
                                    schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = APIResponse.class)
                            )
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "400",
                            description = "잘못된 요청"
                    )
            }
    )
    @PostMapping("/email")
    public APIResponse<Void> checkEmailDuplicate(@Validated @RequestBody TargetDto target) {
        userService.isEmailUnique(target.target());
        return APIResponse.success();
    }

    @Operation(
            summary = "이름 중복 확인",
            description = "주어진 이름이 이미 사용 중인지 확인합니다."
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "중복 확인할 이름",
            required = true,
            content = @io.swagger.v3.oas.annotations.media.Content(
                    mediaType = "application/json",
                    schema = @io.swagger.v3.oas.annotations.media.Schema(type = "string", example = "이순신")
            )
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses(
            value = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "이름 중복 확인 성공",
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    mediaType = "application/json",
                                    schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = APIResponse.class)
                            )
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "400",
                            description = "잘못된 요청"
                    )
            }
    )
    @PostMapping("/name")
    public APIResponse<Void> checkNameDuplicate(@Validated @RequestBody TargetDto target) {
        userService.isNameUnique(target.target());
        return APIResponse.success();
    }
}
