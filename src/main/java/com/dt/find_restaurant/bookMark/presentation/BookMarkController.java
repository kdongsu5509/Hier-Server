package com.dt.find_restaurant.bookMark.presentation;

import com.dt.find_restaurant.bookMark.application.BookMarkService;
import com.dt.find_restaurant.bookMark.dto.BookMarkResponseDto;
import com.dt.find_restaurant.global.response.APIResponse;
import com.dt.find_restaurant.security.domain.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(
        name = "북마크",
        description = "북마크 관련 API"
)
@Slf4j
@RestController
@RequestMapping("/api/bookmarks")
@RequiredArgsConstructor
public class BookMarkController {

    private final BookMarkService bookMarkService;

    //CREATE
    @Operation(
            summary = "북마크 추가",
            description = "사용자가 특정 핀을 북마크합니다. 인증된 사용자의 이메일과 북마크할 핀의 ID를 받아 북마크를 생성하고, 생성된 북마크의 UUID를 반환합니다."
    )
    @PostMapping
    public APIResponse<UUID> addBookMark(@AuthenticationPrincipal CustomUserDetails userDetails, UUID pinId) {
        UUID uuid = bookMarkService.addBookMark(userDetails.getUsername(), pinId);
        return APIResponse.success(uuid);
    }

    //READ
    @Operation(
            summary = "내 북마크 전체 조회",
            description = "인증된 사용자의 이메일을 기반으로 해당 사용자가 추가한 모든 북마크를 조회합니다. 각 북마크는 BookMarkResponseDto 형태로 반환됩니다."
    )
    @GetMapping
    public APIResponse<List<BookMarkResponseDto>> getAllMyBookMarks(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        List<BookMarkResponseDto> bookMarks = bookMarkService.getAllMyBookMarks(userDetails.getUsername());
        return APIResponse.success(bookMarks);
    }

    //DELETE
    @Operation(
            summary = "북마크 삭제",
            description = "사용자가 특정 북마크를 삭제합니다. 인증된 사용자의 이메일과 삭제할 북마크의 ID를 받아 해당 북마크를 삭제합니다."
    )
    @DeleteMapping
    public APIResponse<Void> deleteBookMark(@AuthenticationPrincipal CustomUserDetails userDetails, UUID bookMarkId) {
        bookMarkService.deleteBookMark(userDetails.getUsername(), bookMarkId);
        return APIResponse.success();
    }
}
