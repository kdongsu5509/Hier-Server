package com.dt.find_restaurant.Pin.service;

import static com.dt.find_restaurant.global.exception.CustomExcpMsgs.PIN_NOT_FOUND;

import com.dt.find_restaurant.Pin.dto.PinRequest;
import com.dt.find_restaurant.Pin.repository.PinEntity;
import com.dt.find_restaurant.Pin.repository.PinRepository;
import com.dt.find_restaurant.comment.repository.CommentEntity;
import com.dt.find_restaurant.comment.repository.CommentRepository;
import com.dt.find_restaurant.global.exception.CustomExcpMsgs;
import com.dt.find_restaurant.global.exception.CustomeExceptions.PinException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PinService {

    private final PinRepository pinRepository;
    private final CommentRepository commentRepository;

    public PinEntity createPin(PinRequest req) {
        notAllowAlreadyExistInfomation(req);
        PinEntity pinEntity = PinEntity.toPinEntity(req);
        return pinRepository.save(pinEntity);
    }

    public void deletePin(UUID pinId) {
        if (!pinRepository.existsById(pinId)) {
            throw new PinException(PIN_NOT_FOUND.getMessage());
        }
        // 핀에 연결된 댓글이 있는지 확인 -> 댓글이 있다면 삭제
        deleteCommentIfExist(haveComment(pinId));
        pinRepository.deleteById(pinId);
    }

    public List<PinEntity> getAllPins() {
        return pinRepository.findAll();
    }

    public PinEntity getPinById(UUID pinId) {
        return pinRepository.findById(pinId)
                .orElseThrow(() -> new PinException(PIN_NOT_FOUND.getMessage()));
    }

    private void notAllowAlreadyExistInfomation(PinRequest req) {
        Optional<PinEntity> byLat = pinRepository.findByLat(req.lat());
        if (byLat.isPresent() && byLat.get().getLat().equals(req.lat())) {
            throw new PinException(CustomExcpMsgs.ALEADY_EXISTS.getMessage());
        }
    }

    private void deleteCommentIfExist(List<UUID> commentIds) {
        if (!commentIds.isEmpty()) { // 댓글이 있는 경우 -> 삭제
            // 댓글 삭제
            for (UUID commentId : commentIds) {
                commentRepository.deleteById(commentId);
            }
        }
    }

    private List<UUID> haveComment(UUID pinId) {
        return commentRepository.findAllByPinId(pinId).stream()
                .map(CommentEntity::getId)
                .toList();
    }
}
