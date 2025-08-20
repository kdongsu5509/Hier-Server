package com.dt.find_restaurant.comment.service;

import static com.dt.find_restaurant.global.exception.CustomExcpMsgs.COMMENT_NOT_FOUND;
import static com.dt.find_restaurant.global.exception.CustomExcpMsgs.PIN_NOT_FOUND;

import com.dt.find_restaurant.comment.dto.CommentRequest;
import com.dt.find_restaurant.comment.repository.CommentEntity;
import com.dt.find_restaurant.comment.repository.CommentImageEntity;
import com.dt.find_restaurant.comment.repository.CommentRepository;
import com.dt.find_restaurant.global.exception.CustomExceptions.CommentException;
import com.dt.find_restaurant.pin.repository.PinEntity;
import com.dt.find_restaurant.pin.repository.PinRepository;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final PinRepository pinRepository;

    public CommentEntity createComment(UUID pinId, CommentRequest req) {
        // 게시글 조회
        PinEntity pinEntity = pinRepository.findById(pinId).orElseThrow(
                () -> new CommentException(PIN_NOT_FOUND.getMessage())
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
        log.info("댓글 생성: {}", commentEntity);
        // 댓글 저장
        return commentRepository.save(commentEntity);
    }

    public List<CommentEntity> getAllCommentOfPost(UUID pinId) {
        // 게시글 조회
        PinEntity pinEntity = pinRepository.findById(pinId).orElseThrow(
                () -> new CommentException(PIN_NOT_FOUND.getMessage())
        );

        // 해당 게시글의 모든 댓글 조회
        return commentRepository.findAllByPin(pinEntity);
    }

    public void deleteComment(UUID pinId, UUID commentId) {
        CommentEntity commentEntity = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentException(COMMENT_NOT_FOUND.getMessage()));

        PinEntity pin = commentEntity.getPin();
        if (pin == null || pin.getId() == null) {
            log.info("핀 조회에 실패했습니다 (pin 또는 pin.id == null)");
            throw new CommentException(PIN_NOT_FOUND.getMessage());
        }

        // 값 비교로 변경
        if (!Objects.equals(pin.getId(), pinId)) {
            log.info("찾은 pinId: {}, 요청한 pinId: {}", pin.getId(), pinId);
            log.info("여기서 핀 조회에 실패했습니다");
            throw new CommentException(PIN_NOT_FOUND.getMessage());
        }

        // 댓글 삭제 (자식 이미지들은 CommentEntity에 cascade=ALL, orphanRemoval=true면 함께 삭제)
        commentRepository.delete(commentEntity);
    }
}
