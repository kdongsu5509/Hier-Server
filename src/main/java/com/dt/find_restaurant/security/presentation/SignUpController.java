package com.dt.find_restaurant.security.presentation;

import com.dt.find_restaurant.global.response.APIResponse;
import com.dt.find_restaurant.security.application.UserService;
import com.dt.find_restaurant.security.presentation.dto.UserDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(
        name = "사용자",
        description = "사용자 관련 API"
)
@Slf4j
@RestController
@RequestMapping("/api/signup")
@RequiredArgsConstructor
public class SignUpController {

    private final UserService userService;

    @Operation(
            summary = "회원 가입",
            description = "새로운 사용자를 등록합니다. 요청 본문에 사용자 정보를 포함해야 합니다."
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "회원 가입 요청 본문",
            required = true,
            content = @io.swagger.v3.oas.annotations.media.Content(
                    mediaType = "application/json",
                    schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = UserDto.class)
            )
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses(
            value = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "회원 가입 성공"
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "400",
                            description = "잘못된 요청"
                    )
            }
    )
    @PostMapping
    public void signUp(@Validated @RequestBody UserDto req) {
        userService.signUp(req);
    }

    @Operation(summary = "관리자 회원 가입", description = "관리자가 새로운 관리자를 등록합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "관리자 회원 가입 성공",
                    content = @Content(schema = @Schema(implementation = APIResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(schema = @Schema(implementation = APIResponse.class)))
    })
    @PostMapping("/admin")
    public APIResponse<Void> singUpAsAdmin(@Validated @RequestBody UserDto req) {
        userService.signUpAsAdmin(req);
        return APIResponse.success();
    }

    @Operation(
            summary = "이메일 중복 확인",
            description = "주어진 이메일이 이미 사용 중인지 확인합니다."
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "중복 확인할 이메일",
            required = true,
            content = @io.swagger.v3.oas.annotations.media.Content(
                    mediaType = "application/json",
                    schema = @io.swagger.v3.oas.annotations.media.Schema(type = "string", example = "test@test.com")
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
    @PostMapping("/check")
    public APIResponse<Boolean> checkEmailDuplicate(@RequestBody String email) {
        boolean isDuplicate = userService.isEmailDuplicate(email);
        return APIResponse.success(isDuplicate);
    }
}
