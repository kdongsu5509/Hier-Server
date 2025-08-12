package com.dt.find_restaurant.post.controller;

import com.dt.find_restaurant.post.dto.request.Post;
import com.dt.find_restaurant.post.dto.request.PostUpdateRequest;
import com.dt.find_restaurant.post.repository.PostEntity;
import com.dt.find_restaurant.post.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
@Tag(
        name = "추천 글",
        description = "게시글 관련 API"
)
public class PostController {

    private final PostService postService;

    @Operation(
            summary = "게시글 작성",
            description = "새로운 게시글을 작성합니다. 요청 본문에 게시글 정보를 포함해야 합니다."
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "게시글 작성 요청 본문",
            required = true,
            content = @io.swagger.v3.oas.annotations.media.Content(
                    mediaType = "application/json",
                    schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = Post.class)
            )
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "게시글 작성 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청")
            }
    )
    @PostMapping
    public PostEntity createPost(@Validated @RequestBody Post post) {
        return postService.savePost(post);
    }

    @Operation(
            summary = "게시글 수정",
            description = "기존 게시글을 수정합니다. 요청 본문에 수정된 게시글 정보를 포함해야 합니다."
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "게시글 수정 요청 본문",
            required = true,
            content = @io.swagger.v3.oas.annotations.media.Content(
                    mediaType = "application/json",
                    schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = PostUpdateRequest.class)
            )
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "게시글 수정 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청"),
                    @ApiResponse(responseCode = "404", description = "게시글을 찾을 수 없음")
            }
    )
    @PatchMapping
    public PostEntity updatePost(@PathVariable Long id, @Valid @RequestBody PostUpdateRequest post) {
        return postService.updatePost(id, post);
    }

    @Operation(
            summary = "모든 게시글 조회",
            description = "저장된 모든 게시글을 조회합니다."
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "게시글 조회 요청 본문",
            required = false,
            content = @io.swagger.v3.oas.annotations.media.Content(
                    mediaType = "application/json",
                    schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = PostEntity.class
                    )
            )
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "게시글 조회 성공"),
                    @ApiResponse(responseCode = "404", description = "게시글을 찾을 수 없음")
            }
    )
    @GetMapping
    public List<PostEntity> findAll() {
        return postService.getAllPosts();
    }

    @Operation
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "게시글 조회 성공"),
                    @ApiResponse(responseCode = "404", description = "게시글을 찾을 수 없음")
            }
    )
    @GetMapping("/{id}")
    public PostEntity findById(@PathVariable Long id) {
        return postService.getPostById(id);
    }


}
