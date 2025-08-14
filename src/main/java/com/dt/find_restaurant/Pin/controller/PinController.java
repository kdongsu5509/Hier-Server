package com.dt.find_restaurant.Pin.controller;


import com.dt.find_restaurant.Pin.dto.PinRequest;
import com.dt.find_restaurant.Pin.repository.PinEntity;
import com.dt.find_restaurant.Pin.service.PinService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    public PinEntity createPin(@RequestBody @Validated PinRequest pinRequest) {
        return pinService.createPin(pinRequest);
    }

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
    public void deletePin(@RequestParam UUID pinId) {
        pinService.deletePin(pinId);
    }

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
    @GetMapping()
    public List<PinEntity> getAllPins() {
        return pinService.getAllPins();
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
    public PinEntity getPinById(@PathVariable UUID pinId) {
        return pinService.getPinById(pinId);
    }
}
