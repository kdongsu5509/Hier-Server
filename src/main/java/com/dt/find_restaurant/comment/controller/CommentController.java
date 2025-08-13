package com.dt.find_restaurant.comment.controller;

import com.dt.find_restaurant.comment.dto.CommentRequest;
import com.dt.find_restaurant.comment.dto.CommentUpdateRequest;
import com.dt.find_restaurant.comment.repository.Comment;
import com.dt.find_restaurant.comment.service.CommentService;
import com.dt.find_restaurant.security.jwt.component.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
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

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @Operation(
            summary = "댓글 작성",
            description = "게시글에 댓글을 작성합니다. 요청 본문에 댓글 정보를 포함해야 합니다."
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "댓글 작성 요청 본문",
            required = true,
            content = @io.swagger.v3.oas.annotations.media.Content(
                    mediaType = "application/json",
                    schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = CommentRequest.class
                    )
            )
    )
    @PostMapping
    public Comment createComment(@AuthenticationPrincipal CustomUserDetails userDetails, @NotNull Long postId,
                                 @Validated @RequestBody CommentRequest comment) {
        return commentService.createComment(postId, comment, userDetails.getUsername());
    }

    @Operation(
            summary = "댓글 수정",
            description = "댓글을 수정합니다. 요청 본문에 수정할 댓글 정보를 포함해야 합니다."
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "댓글 수정 요청 본문",
            required = true,
            content = @io.swagger.v3.oas.annotations.media.Content(
                    mediaType = "application/json",
                    schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = CommentUpdateRequest.class
                    )
            )
    )
    @PatchMapping
    public Comment updatePost(@PathVariable UUID id, @Valid @RequestBody CommentUpdateRequest reuqest) {
        return commentService.updatePost(id, reuqest);
    }

    @Operation(
            summary = "게시글의 모든 댓글 조회",
            description = "특정 게시글에 대한 모든 댓글을 조회합니다."
    )
    @GetMapping("/{postId}")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "게시글 ID",
            required = true,
            content = @io.swagger.v3.oas.annotations.media.Content(
                    mediaType = "application/json",
                    schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = Long.class)
            )
    )
    public List<Comment> getAllCommentOfPost(@PathVariable Long postId) {
        return commentService.getAllCommentOfPost(postId);
    }

    public void deleteComment(@PathVariable UUID id) {
        commentService.deleteComment(id);
    }
}
