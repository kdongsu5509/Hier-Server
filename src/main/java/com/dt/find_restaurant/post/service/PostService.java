package com.dt.find_restaurant.post.service;

import com.dt.find_restaurant.post.dto.request.Post;
import com.dt.find_restaurant.post.dto.request.PostUpdateRequest;
import com.dt.find_restaurant.post.repository.PostEntity;
import com.dt.find_restaurant.post.repository.PostRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PostService {
    private final PostRepository postRepository;
    public PostEntity savePost(Post post) {
        PostEntity requestEntity = PostEntity.create(
                post.restaurantName(),
                post.latitude(),
                post.longitude(),
                post.text(),
                post.grade(), // -1.0이 들어가있으면 아직 평점이 없는 상태
                post.kakaoMapUrl()
        );

        return postRepository.save(requestEntity);
    }

    public PostEntity updatePost(Long id, PostUpdateRequest request) {
        PostEntity postEntity = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));

        postEntity.updateFrom(request);

        return postEntity;
    }

    public List<PostEntity> getAllPosts() {
        return postRepository.findAll();
    }

    public PostEntity getPostById(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));
    }
}
