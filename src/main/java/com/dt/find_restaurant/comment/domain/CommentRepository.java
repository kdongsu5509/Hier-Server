package com.dt.find_restaurant.comment.domain;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository {

    UUID saveAndReturnId(Comment comment);

    List<Comment> findByPinId(UUID pinId);

    String findByUserEmail(String userEmail);

    Optional<Comment> findById(@NotNull UUID commentId);

    void delete(Comment commentEntity);
}
