package com.dt.find_restaurant.comment.application;

import static com.dt.find_restaurant.global.exception.CustomExcpMsgs.COMMENT_NOT_FOUND;
import static com.dt.find_restaurant.global.exception.CustomExcpMsgs.COMMENT_UNAUTHORIZED;
import static com.dt.find_restaurant.global.exception.CustomExcpMsgs.PIN_NOT_FOUND;

import com.dt.find_restaurant.comment.domain.Comment;
import com.dt.find_restaurant.comment.domain.CommentImageEntity;
import com.dt.find_restaurant.comment.domain.CommentRepository;
import com.dt.find_restaurant.comment.dto.CommentRequest;
import com.dt.find_restaurant.comment.dto.CommentResponse;
import com.dt.find_restaurant.comment.dto.CommentUpdateRequest;
import com.dt.find_restaurant.global.exception.CustomExceptions.CommentException;
import com.dt.find_restaurant.pin.domain.Pin;
import com.dt.find_restaurant.pin.domain.PinRepository;
import com.dt.find_restaurant.security.domain.User;
import com.dt.find_restaurant.security.domain.UserRepository;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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
    private final UserRepository userRepository;

    public UUID createComment(String userEmail, UUID pinId, CommentRequest req) {
        //게시글 조회
        Comment commentEntity = makeDefaultComment(userEmail, pinId, req);
        //사진이 비어있지 않으면 이미지 엔티티 생성
        addImageIfExist(req, commentEntity);
        log.info("댓글 생성: {}", commentEntity);
        // 댓글 저장
        return commentRepository.saveAndReturnId(commentEntity);
    }

    public List<CommentResponse> getAllCommentOfPin(UUID pinId) {
        List<Comment> byPinId = commentRepository.findByPinId(pinId);
        return byPinId.stream()
                .map(this::toCommentResponse)
                .toList();
    }

    public void updateComment(String userEmail, @NotNull UUID pinId, @NotNull UUID commentId,
                              CommentUpdateRequest updateRequest) {
        //1. 내가 이 댓글을 쓴게 맞는지 확인
        Comment commentEntity = validateCommentUpdateRequest(userEmail, pinId, commentId);
        //4. 댓글 수정
        commentEntity.updateComment(updateRequest.comment(), updateRequest.grade(), updateRequest.commentType());
    }

    public void updateCommentImages(String userEmail, @NotNull UUID pinId, @NotNull UUID commentId,
                                    List<String> imageUrls) {
        Comment commentEntity = validateCommentUpdateRequest(userEmail, pinId, commentId);
        //4. 이미지 수정
        imageUrls.stream()
                .map(CommentImageEntity::create)
                .forEach(commentEntity::updateImage);
    }

    private Comment validateCommentUpdateRequest(String userEmail, UUID pinId, UUID commentId) {
        //1. 내가 이 댓글을 쓴게 맞는지 확인
        isMyComment(userEmail, commentId);

        //2. commentId로 댓글 조회
        Comment commentEntity = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentException(COMMENT_NOT_FOUND.getMessage()));

        //3. 댓글이 pinId와 연관된게 맞는지 확인
        haveProperRelationWithPin(pinId, commentEntity);
        return commentEntity;
    }

    public void deleteComment(String userEmail, UUID pinId, UUID commentId) {
        //1. 내 댓글이 맞는지 확인
        Comment commentEntity = validateCommentUpdateRequest(userEmail, pinId, commentId);

        // 댓글 삭제 (자식 이미지들은 CommentEntity에 cascade=ALL, orphanRemoval=true면 함께 삭제)
        commentRepository.delete(commentEntity);
    }

    private static void haveProperRelationWithPin(UUID pinId, Comment commentEntity) {
        Pin pin = commentEntity.getPin();
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
    }

    private void isMyComment(String userEmail, UUID commentId) {
        List<Comment> byUserEmail = commentRepository.findByUserEmail(userEmail);
        if (byUserEmail.isEmpty()) {
            log.info("해당 사용자가 작성한 댓글이 없습니다. userEmail: {}", userEmail);
            throw new CommentException(COMMENT_UNAUTHORIZED.getMessage());
        }
        boolean isMyComment = byUserEmail.stream()
                .anyMatch(comment -> comment.getId().toString().equals(commentId.toString()));
        if (!isMyComment) {
            log.info("해당 사용자가 작성한 댓글이 아닙니다. userEmail: {}, commentId: {}", userEmail, commentId);
            throw new CommentException(COMMENT_UNAUTHORIZED.getMessage());
        }
    }

    private CommentResponse toCommentResponse(Comment comment) {
        return new CommentResponse(
                comment.getId(),
                comment.getComment(),
                comment.getGrade(),
                comment.getType(),
                comment.getUser().getUserName(),
                comment.getCreatedAt(),
                comment.getUpdatedAt()
        );
    }

    private void addImageIfExist(CommentRequest req, Comment commentEntity) {
        assert req.imageUrl() != null;
        if (!req.imageUrl().isEmpty()) {
            for (String imageUrl : req.imageUrl()) {
                CommentImageEntity commentImageEntity = CommentImageEntity.create(imageUrl);
                commentEntity.updateImage(commentImageEntity);
            }
        }
    }

    private Comment makeDefaultComment(String userEmail, UUID pinId, CommentRequest req) {
        Pin relatedPin = pinRepository.findById(pinId).orElseThrow(
                () -> new CommentException(PIN_NOT_FOUND.getMessage())
        );

        //사용자 조회
        Optional<User> byEmail = userRepository.findByEmail(userEmail);
        User creator = byEmail.orElseThrow(
                () -> new CommentException(COMMENT_UNAUTHORIZED.getMessage())
        );

        Comment commentEntity = Comment.create(
                req.comment(),
                req.grade(),
                relatedPin,
                req.type()
        );

        //댓글 작성자 설정
        commentEntity.updateUser(creator);
        return commentEntity;
    }

    public List<CommentResponse> getAllMyComments(String userEmail) {
        List<Comment> byUserEmail = commentRepository.findByUserEmail(userEmail);
        return byUserEmail.stream()
                .map(this::toCommentResponse)
                .toList();
    }
}
