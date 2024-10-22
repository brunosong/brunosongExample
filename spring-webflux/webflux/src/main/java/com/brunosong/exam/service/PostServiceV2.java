package com.brunosong.exam.service;

import com.brunosong.exam.client.PostClient;
import com.brunosong.exam.dto.PostResponse;
import com.brunosong.exam.repository.Post;
import com.brunosong.exam.repository.PostR2dbcRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostServiceV2 {

    private final PostR2dbcRepository postR2dbcRepository;

    // create
    public Mono<Post> create(Long userId, String title, String content) {
        return postR2dbcRepository.save(Post.builder()
                .userId(userId)
                .title(title)
                .content(content)
                .build());
    }

    // read
    public Flux<Post> findAll() {
        return postR2dbcRepository.findAll();
    }

    // delete
    public Mono<Void> deleteById(Long id) {
        return postR2dbcRepository.deleteById(id);
    }

    public Mono<Post> findById(Long id) {
        return postR2dbcRepository.findById(id);
    }

}
