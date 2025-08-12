package com.dt.find_restaurant.comment.service;

import com.dt.find_restaurant.comment.dto.CommentRequest;
import com.dt.find_restaurant.comment.dto.CommentUpdateRequest;
import com.dt.find_restaurant.comment.repository.Comment;
import com.dt.find_restaurant.comment.repository.CommentRepository;
import com.dt.find_restaurant.comment.repository.CommentType;
import com.dt.find_restaurant.post.repository.PostEntity;
import com.dt.find_restaurant.post.repository.PostRepository;
import com.dt.find_restaurant.user.domain.User;
import com.dt.find_restaurant.user.repository.UserRepository;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public Comment createComment(Long postId, CommentRequest comment, String userEmail) {
        // 게시글 조회
        PostEntity postEntity = postRepository.findById(postId).orElseThrow(
                () -> new IllegalArgumentException("게시글이 존재하지 않습니다.")
        );
        // 사용자 조회
        User user = userRepository.findByEmail(userEmail).orElseThrow(
                () -> new IllegalArgumentException("사용자가 존재하지 않습니다.")
        );
        Comment commentEntity = Comment.create(
                postEntity,
                user,
                comment.imageUrl(),
                comment.text(),
                comment.grade(),
                comment.commentType().equals("NORMAL") ? CommentType.NORMAL : CommentType.REVIEW
        );

        // 댓글 저장
        return commentRepository.save(commentEntity);
    }

    public Comment updatePost(UUID id, @Valid CommentUpdateRequest reuqest) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("댓글이 존재하지 않습니다."));

        // 댓글 업데이트
        comment.updateFrom(reuqest);

        return commentRepository.save(comment);
    }

    public List<Comment> getAllCommentOfPost(Long postId) {
        // 게시글 조회
        PostEntity postEntity = postRepository.findById(postId).orElseThrow(
                () -> new IllegalArgumentException("게시글이 존재하지 않습니다.")
        );

        // 해당 게시글의 모든 댓글 조회
        return commentRepository.findAllByPost(postEntity);
    }
}
