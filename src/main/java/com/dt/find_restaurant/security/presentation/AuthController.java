package com.dt.find_restaurant.security.presentation;

import com.dt.find_restaurant.security.application.JwtService;
import com.dt.find_restaurant.security.domain.JwtResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(
        name = "인증",
        description = "인증 관련 API"
)
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final JwtService jwtService;

    @Operation(
            summary = "Access/Refresh 재발급",
            description = "유효한 Refresh Token을 사용하여 새로운 Access Token과 Refresh Token을 발급받습니다. " +
                    "Refresh Token이 유효하지 않거나 만료된 경우, 401 Unauthorized 응답을 받습니다."
    )
    @PostMapping("/reissue")
    public JwtResult.Issue reissue(@Validated @RequestParam String refreshToken) {
        return jwtService.reissueJwtToken(refreshToken);
    }
}
