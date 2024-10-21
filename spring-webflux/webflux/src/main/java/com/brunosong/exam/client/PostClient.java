package com.brunosong.exam.client;

import com.brunosong.exam.dto.PostResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class PostClient {
    private final WebClient webClient;
    private final String url = "http://localhost:8111";

    // WebClient -> mvc("/posts/{id}")
    public Mono<PostResponse> getPost(Long id) {
        String uriString = UriComponentsBuilder.fromHttpUrl(url)
                .path("/posts/%d".formatted(id))
                .buildAndExpand()
                .toUriString();

        Mono<PostResponse> postResponseMono = webClient.get()
                .uri(uriString)
                .retrieve()
                .bodyToMono(PostResponse.class);

        return postResponseMono;
    }
}
