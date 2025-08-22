package com.dt.find_restaurant.pin.presentation;

import com.dt.find_restaurant.global.response.APIResponse;
import com.dt.find_restaurant.pin.application.PinService;
import com.dt.find_restaurant.pin.dto.PinDetailResponse;
import com.dt.find_restaurant.pin.dto.PinRequest;
import com.dt.find_restaurant.pin.dto.PinSimpleResponse;
import com.dt.find_restaurant.pin.dto.PinUpdateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(
        name = "핀",
        description = "핀 관련 API"
)
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/pin")
public class PinController {

    private final PinService pinService;

    //CREATE
    @Operation(
            summary = "핀 생성",
            description = "새로운 핀을 생성합니다. 요청 본문에 핀 정보를 포함해야 합니다."
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "핀 생성 요청 본문",
            required = true,
            content = @io.swagger.v3.oas.annotations.media.Content(
                    mediaType = "application/json",
                    schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = PinRequest.class)
            )
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses(
            value = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "핀 생성 성공"
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "400",
                            description = "잘못된 요청"
                    )
            }
    )
    @PostMapping
    public APIResponse<UUID> createPin(@RequestBody @Validated PinRequest pinRequest,
                                       @AuthenticationPrincipal String userEmail) {
        UUID pinId = pinService.createPin(userEmail, pinRequest);
        return APIResponse.success(pinId);
    }

    //READ
    @Operation(
            summary = "모든 핀 조회",
            description = "저장된 모든 핀을 조회합니다."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses(
            value = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "모든 핀 조회 성공"
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "400",
                            description = "잘못된 요청"
                    )
            }
    )
    @GetMapping
    public APIResponse<List<PinSimpleResponse>> getAllPins() {
        return APIResponse.success(pinService.getAllPins());
    }

    @Operation(
            summary = "핀 ID로 핀 조회",
            description = "특정 핀 ID에 해당하는 핀을 조회합니다."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses(
            value = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "핀 조회 성공"
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "400",
                            description = "잘못된 요청"
                    )
            }
    )
    @GetMapping("{pinId}")
    public APIResponse<PinDetailResponse> getPinById(@PathVariable UUID pinId) {
        return APIResponse.success(pinService.getPinById(pinId));
    }

    //UPDATE
    @Operation(
            summary = "핀 수정",
            description = "기존 핀 정보를 수정합니다. 요청 파라미터에 핀 ID를 포함하고, 요청 본문에 수정할 핀 정보를 포함해야 합니다."
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "핀 수정 요청 본문",
            required = true,
            content = @io.swagger.v3.oas.annotations.media.Content(
                    mediaType = "application/json",
                    schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = PinUpdateRequest.class)
            )
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses(
            value = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "핀 수정 성공"
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "400",
                            description = "잘못된 요청"
                    )
            }
    )
    @PatchMapping("{pinId}")
    public APIResponse<Void> updatePin(
            @AuthenticationPrincipal String userEmail,
            @PathVariable UUID pinId,
            @RequestBody PinUpdateRequest req) {
        pinService.updatePin(userEmail, pinId, req);
        return APIResponse.success();
    }

    //DELETE
    @Operation(
            summary = "핀 삭제",
            description = "핀을 삭제합니다. 요청 파라미터에 핀 ID를 포함해야 합니다."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses(
            value = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "핀 삭제 성공"
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "400",
                            description = "잘못된 요청"
                    )
            }
    )
    @PostMapping("/delete")
    public void deletePin(@RequestBody UUID pinId, @AuthenticationPrincipal String userEmail) {
        pinService.deletePin(userEmail, pinId);
    }


}
