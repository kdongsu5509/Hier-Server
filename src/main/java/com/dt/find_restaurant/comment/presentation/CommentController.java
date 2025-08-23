package com.dt.find_restaurant.comment.presentation;

import com.dt.find_restaurant.comment.application.CommentService;
import com.dt.find_restaurant.comment.dto.CommentRequest;
import com.dt.find_restaurant.comment.dto.CommentResponse;
import com.dt.find_restaurant.comment.dto.CommentUpdateRequest;
import com.dt.find_restaurant.global.response.APIResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
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
        name = "댓글",
        description = "댓글 관련 API"
)
@RestController
@RequestMapping("/api/pins")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    //CREATE
    @Operation(
            summary = "댓글 생성",
            description = "새로운 댓글을 생성합니다. 요청 본문에 댓글 정보를 포함해야 합니다."
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "댓글 생성 요청 본문",
            required = true,
            content = @io.swagger.v3.oas.annotations.media.Content(
                    mediaType = "application/json",
                    schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = CommentRequest.class)
            )
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses(
            value = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "댓글 생성 성공",
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
    @PostMapping("/{pinId}/comments")
    public APIResponse<UUID> createComment(@AuthenticationPrincipal String userEmail,
                                           @NotNull @PathVariable UUID pinId,
                                           @Validated @RequestBody CommentRequest comment) {
        UUID createdCommentId = commentService.createComment(userEmail, pinId, comment);
        return APIResponse.success(createdCommentId);
    }

    //READ
    @Operation(
            summary = "게시글의 모든 댓글 조회",
            description = "특정 게시글에 대한 모든 댓글을 조회합니다."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses(
            value = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "댓글 목록 조회 성공",
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    mediaType = "application/json",
                                    schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = List.class)
                            )
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "404",
                            description = "게시글이 존재하지 않음",
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    mediaType = "application/json",
                                    schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = APIResponse.class)
                            )
                    )
            }
    )
    @GetMapping("/{pinId}/comments")
    public APIResponse<List<CommentResponse>> getAllCommentOfPost(@NotNull @PathVariable UUID pinId) {
        List<CommentResponse> allCommentOfPost = commentService.getAllCommentOfPin(pinId);
        return APIResponse.success(allCommentOfPost);
    }

    //UPDATE
    @Operation(
            summary = "댓글 수정",
            description = "특정 댓글을 수정합니다."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses(
            value = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "댓글 수정 성공"
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "404",
                            description = "댓글이 존재하지 않음"
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "403",
                            description = "댓글 작성자가 아님"
                    )
            }
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "댓글 수정 요청 본문",
            required = true,
            content = @io.swagger.v3.oas.annotations.media.Content(
                    mediaType = "application/json",
                    schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = CommentUpdateRequest.class)
            )
    )
    @PatchMapping("/{pinId}/comments/{commentId}")
    public void updateComment(@AuthenticationPrincipal String userEmail,
                              @NotNull @PathVariable UUID pinId,
                              @NotNull @PathVariable UUID commentId,
                              @Validated @RequestBody CommentUpdateRequest updateRequest) {
        commentService.updateComment(userEmail, pinId, commentId, updateRequest);
    }

    //UPDATE IMAGE
    @Operation(
            summary = "댓글 이미지 수정",
            description = "특정 댓글의 이미지를 수정합니다."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses(
            value = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "댓글 이미지 수정 성공",
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    mediaType = "application/json",
                                    schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = APIResponse.class)
                            )
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "404",
                            description = "댓글이 존재하지 않음",
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    mediaType = "application/json",
                                    schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = APIResponse.class)
                            )
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "403",
                            description = "댓글 작성자가 아님",
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    mediaType = "application/json",
                                    schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = APIResponse.class)
                            )
                    )
            }
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "댓글 이미지 수정 요청 본문",
            required = true,
            content = @io.swagger.v3.oas.annotations.media.Content(
                    mediaType = "application/json",
                    schema = @io.swagger.v3.oas.annotations.media.Schema(
                            implementation = List.class,
                            example = "[\"https://example.com/image1.jpg\", \"https://example.com/image2.jpg\"]"
                    )
            )
    )
    @PatchMapping("/{pinId}/comments/{commentId}/images")
    public APIResponse<Void> updateCommentImages(@AuthenticationPrincipal String userEmail,
                                    @NotNull @PathVariable UUID pinId,
                                    @NotNull @PathVariable UUID commentId,
                                    @RequestBody List<String> imageUrls) {
        commentService.updateCommentImages(userEmail, pinId, commentId, imageUrls);
        return APIResponse.success();
    }


    @Operation(
            summary = "댓글 삭제",
            description = "특정 댓글을 삭제합니다."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses(
            value = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "댓글 삭제 성공",
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    mediaType = "application/json",
                                    schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = APIResponse.class)
                            )
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "404",
                            description = "댓글이 존재하지 않음",
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    mediaType = "application/json",
                                    schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = APIResponse.class)
                            )
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "403",
                            description = "댓글 작성자가 아님",
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    mediaType = "application/json",
                                    schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = APIResponse.class)
                            )
                    )
            }
    )
    @PostMapping("/{pinId}/comments/delete/{commnetId}")
    public APIResponse<Void> deleteComment(@AuthenticationPrincipal String userEmail,
                              @NotNull @PathVariable UUID pinId,
                              @NotNull @PathVariable UUID commnetId) {
        commentService.deleteComment(userEmail, pinId, commnetId);
        return APIResponse.success();
    }
}
