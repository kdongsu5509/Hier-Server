package com.dt.find_restaurant.user.controller;

import com.dt.find_restaurant.global.response.APIResponse;
import com.dt.find_restaurant.user.domain.UserCommand;
import com.dt.find_restaurant.user.domain.UserRequest;
import com.dt.find_restaurant.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/user")
@Tag(description = "유저관련 API", name = "유저 API")
public class UserController {

    private final UserService userService;

    @Operation(
            summary = "회원가입",
            description = "이메일과 비밀번호를 사용하여 새로운 사용자를 등록합니다."
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "회원가입 요청 정보",
            required = true,
            content = @io.swagger.v3.oas.annotations.media.Content(
                    mediaType = "application/json",
                    schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = UserRequest.SignUp.class)
            )
    )
    @ApiResponses(
            value = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "회원가입 성공"
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "400",
                            description = "잘못된 요청"
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "500",
                            description = "서버 오류"
                    )
            }
    )
    @PostMapping("/signup")
    public APIResponse<Void> signup(@RequestBody @Valid UserRequest.SignUp request) {
        userService.saveUser(UserCommand.Signup.of(request.getEmail(), request.getPassword(), request.getName()));
        return APIResponse.success();
    }
}
