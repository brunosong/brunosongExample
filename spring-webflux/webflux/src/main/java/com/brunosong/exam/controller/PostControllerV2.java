package com.brunosong.exam.controller;

import com.brunosong.exam.dto.PostCreateRequest;
import com.brunosong.exam.dto.PostResponse;
import com.brunosong.exam.dto.PostResponseV2;
import com.brunosong.exam.repository.Post;
import com.brunosong.exam.service.PostServiceV2;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v2/posts")
public class PostControllerV2 {

    private final PostServiceV2 postServiceV2;

    @PostMapping
    public Mono<PostResponseV2> createPost(@RequestBody PostCreateRequest request) {
        Mono<Post> postMono = postServiceV2.create(request.getUserId(), request.getTitle(), request.getContent());
        return postMono.map(i -> PostResponseV2.of(i));
    }

    @GetMapping
    public Flux<PostResponseV2> findAllPost() {
        return postServiceV2.findAll().map(PostResponseV2::of);
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<PostResponseV2>> findPost(@PathVariable Long id) {
        return postServiceV2.findById(id)
                .map(u -> ResponseEntity.ok().body(PostResponseV2.of(u)))
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<?>> deleteById(@PathVariable Long id) {
        return postServiceV2.deleteById(id).then(Mono.just(ResponseEntity.noContent().build()));
    }

}
