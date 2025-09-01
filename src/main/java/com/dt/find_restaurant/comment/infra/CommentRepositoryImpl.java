package com.dt.find_restaurant.comment.infra;

import com.dt.find_restaurant.comment.domain.Comment;
import com.dt.find_restaurant.comment.domain.CommentRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepository {

    private final CommentJpaRepository jpaRepository;

    @Override
    public UUID saveAndReturnId(Comment comment) {
        Comment save = jpaRepository.save(comment);
        return save.getId();
    }

    @Override
    public List<Comment> findByPinId(UUID pinId) {
        return jpaRepository.findByPinId(pinId);
    }

    @Override
    public List<Comment> findByUserEmail(String userEmail) {
        return jpaRepository.findByUserEmail(userEmail);
    }

    @Override
    public Optional<Comment> findById(UUID commentId) {
        return jpaRepository.findById(commentId);
    }

    @Override
    public void delete(Comment commentEntity) {
        jpaRepository.delete(commentEntity);
    }

}
