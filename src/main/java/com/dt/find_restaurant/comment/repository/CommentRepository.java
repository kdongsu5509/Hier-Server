package com.dt.find_restaurant.comment.repository;

import com.dt.find_restaurant.pin.domain.Pin;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, UUID> {
    List<CommentEntity> findAllByPin(Pin postEntity);

    List<CommentEntity> findAllByPinId(UUID pinId);
}
