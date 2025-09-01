package com.dt.find_restaurant.comment.infra;

import com.dt.find_restaurant.comment.domain.Comment;
import com.dt.find_restaurant.pin.domain.Pin;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentJpaRepository extends JpaRepository<Comment, UUID> {
    List<Comment> findAllByPin(Pin postEntity);

    List<Comment> findAllByPinId(UUID pinId);

    List<Comment> findByPinId(UUID pinId);

    List<Comment> findByUserEmail(String userEmail);
}
