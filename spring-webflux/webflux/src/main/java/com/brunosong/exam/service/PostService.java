package com.brunosong.exam.service;

import com.brunosong.exam.client.PostClient;
import com.brunosong.exam.dto.PostResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    // webclient mvc server request
    private final PostClient postClient;

    public Mono<PostResponse> getPostContent(Long id) {
        return postClient.getPost(id);
    }

    public Flux<PostResponse> getMultiplePostContent(List<Long> idList) {
        return Flux.fromIterable(idList)
                .flatMap(this::getPostContent);
    }

}
