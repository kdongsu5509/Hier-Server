package com.dt.find_restaurant.comment.repository;

import com.dt.find_restaurant.Pin.repository.PinEntity;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, UUID> {
    List<CommentEntity> findAllByPin(PinEntity postEntity);
}
