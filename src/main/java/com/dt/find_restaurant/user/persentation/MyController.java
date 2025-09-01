package com.dt.find_restaurant.user.persentation;

import com.dt.find_restaurant.global.response.APIResponse;
import com.dt.find_restaurant.security.domain.CustomUserDetails;
import com.dt.find_restaurant.user.application.MyService;
import com.dt.find_restaurant.user.dto.UserInfoResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(
        name = "내 정보",
        description = "내 정보 관련 API"
)
@Slf4j
@RequestMapping("/api/my")
@RestController
@RequiredArgsConstructor
public class MyController {

    final MyService service;

    @Operation(
            summary = "내 정보 조회",
            description = "인증된 사용자의 이메일을 기반으로 해당 사용자의 정보를 조회합니다. 사용자 정보는 UserInfoResponseDto 형태로 반환됩니다."
    )
    @GetMapping
    public APIResponse<UserInfoResponseDto> myInfo(@AuthenticationPrincipal CustomUserDetails userDetails) {
        UserInfoResponseDto myInfo = service.getMyInfo(userDetails.getUsername());
        return APIResponse.success(myInfo);
    }

    //내가 작성한 핀

    //내가 작성한 댓글
}
