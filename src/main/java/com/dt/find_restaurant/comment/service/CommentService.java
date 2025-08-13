package com.dt.find_restaurant.comment.service;

import com.dt.find_restaurant.Pin.repository.PinEntity;
import com.dt.find_restaurant.Pin.repository.PinRepository;
import com.dt.find_restaurant.comment.dto.CommentRequest;
import com.dt.find_restaurant.comment.repository.CommentEntity;
import com.dt.find_restaurant.comment.repository.CommentImageEntity;
import com.dt.find_restaurant.comment.repository.CommentRepository;
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
    private final PinRepository pinRepository;

    public CommentEntity createComment(UUID pinId, CommentRequest req) {
        // 게시글 조회
        PinEntity pinEntity = pinRepository.findById(pinId).orElseThrow(
                () -> new IllegalArgumentException("게시글이 존재하지 않습니다.")
        );

        CommentEntity commentEntity = CommentEntity.create(
                req.creatorName(),
                req.text(),
                req.grade(),
                pinEntity
        );

        //사진이 비어있지 않으면 이미지 엔티티 생성
        if (!req.imageUrl().isEmpty()) {
            for (String imageUrl : req.imageUrl()) {
                CommentImageEntity commentImageEntity = CommentImageEntity.create(imageUrl);
                commentEntity.addCommentImage(commentImageEntity);
            }
        }
        // 댓글 저장
        return commentRepository.save(commentEntity);
    }

    public List<CommentEntity> getAllCommentOfPost(UUID pinId) {
        // 게시글 조회
        PinEntity pinEntity = pinRepository.findById(pinId).orElseThrow(
                () -> new IllegalArgumentException("게시글이 존재하지 않습니다.")
        );

        // 해당 게시글의 모든 댓글 조회
        return commentRepository.findAllByPin(pinEntity);
    }

    public void deleteComment(UUID id, UUID pinId) {
        CommentEntity commentEntity = commentRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("댓글이 존재하지 않습니다.")
        );

        if (commentEntity.getPin().getId() != pinId) {
            throw new IllegalArgumentException("해당 댓글은 이 게시글에 속하지 않습니다.");
        }
        // 댓글 삭제
        commentRepository.delete(commentEntity);
    }
}
