package com.dt.find_restaurant.admin.presentation;

import com.dt.find_restaurant.admin.application.AdminService;
import com.dt.find_restaurant.admin.dto.UserInfoResponseDto;
import com.dt.find_restaurant.global.response.APIResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(
        name = "관리자",
        description = "관리자 관련 API-관리자 권한이 없으면 접근할 수 없습니다."
)
@RestController
@RequiredArgsConstructor // final 필드 주입을 위한 어노테이션
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService; // 서비스 계층 의존성 주입

    @Operation(summary = "모든 사용자 정보 조회", description = "관리자가 모든 사용자의 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사용자 정보 조회 성공",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @ApiResponse(responseCode = "403", description = "관리자 권한이 없습니다.",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @GetMapping("/users")
    public APIResponse<List<UserInfoResponseDto>> getAllUsers() {
        List<UserInfoResponseDto> allUsers = adminService.findAllUsers();
        return APIResponse.success(allUsers);
    }

    @Operation(summary = "사용자 계정 잠금 해제", description = "관리자가 특정 사용자의 계정 잠금을 해제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사용자 계정 잠금 해제 성공",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @ApiResponse(responseCode = "403", description = "관리자 권한이 없습니다.",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @ApiResponse(responseCode = "404", description = "해당 사용자를 찾을 수 없습니다.",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @PostMapping("/user/unlock/{userId}")
    public APIResponse<String> unlockUserAccount(@PathVariable UUID userId) {
        adminService.unlockUserAccount(userId);
        return APIResponse.success("User account unlocked successfully.");
    }
    //핀의 생성은 관리자만 가능합니다!
}