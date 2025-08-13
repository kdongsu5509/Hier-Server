package com.dt.find_restaurant.comment.controller;

import com.dt.find_restaurant.comment.dto.CommentRequest;
import com.dt.find_restaurant.comment.repository.CommentEntity;
import com.dt.find_restaurant.comment.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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
                            description = "댓글 생성 성공"
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "400",
                            description = "잘못된 요청"
                    )
            }
    )
    @PostMapping("/{pinId}/comments")
    public CommentEntity createComment(@NotNull @PathVariable UUID pinId,
                                       @Validated @RequestBody CommentRequest comment) {
        return commentService.createComment(pinId, comment);
    }


    @Operation(
            summary = "게시글의 모든 댓글 조회",
            description = "특정 게시글에 대한 모든 댓글을 조회합니다."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses(
            value = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "댓글 목록 조회 성공"
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "404",
                            description = "게시글이 존재하지 않음"
                    )
            }
    )
    @GetMapping("/{pinId}/comments")
    public List<CommentEntity> getAllCommentOfPost(@NotNull @PathVariable UUID pinId) {
        return commentService.getAllCommentOfPost(pinId);
    }

    @Operation(
            summary = "댓글 삭제",
            description = "특정 댓글을 삭제합니다."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses(
            value = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "댓글 삭제 성공"
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "404",
                            description = "댓글이 존재하지 않음"
                    )
            }
    )
    @DeleteMapping("/{pinId}/comments/{commnetId}")
    public void deleteComment(@NotNull @PathVariable UUID commnetId,
                              @NotNull @PathVariable UUID pinId) {
        commentService.deleteComment(commnetId, pinId);
    }
}
