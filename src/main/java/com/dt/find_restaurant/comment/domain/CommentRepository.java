package com.dt.find_restaurant.comment.domain;

import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository {

    UUID saveAndReturnId(Comment comment);

    List<Comment> findByPinId(UUID pinId);
}
