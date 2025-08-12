package com.dt.find_restaurant.comment.repository;

import com.dt.find_restaurant.post.repository.PostEntity;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, UUID> {
    List<Comment> findAllByPost(PostEntity postEntity);
}
