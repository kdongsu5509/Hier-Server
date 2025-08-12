package com.dt.find_restaurant.post.repository;

import com.dt.find_restaurant.post.dto.request.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<PostEntity, Long> {
}
