package com.brunosong.exam.controller;

import com.brunosong.exam.dto.UserCreateRequest;
import com.brunosong.exam.dto.UserResponse;
import com.brunosong.exam.dto.UserUpdateRequest;
import com.brunosong.exam.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public Mono<UserResponse> createUser(@RequestBody UserCreateRequest request) {
        return userService.create(request.getName(),request.getEmail())
                .map(UserResponse::of);
    }

    @GetMapping
    public Flux<UserResponse> findAllUsers() {
        return userService.findAll()
                .map(UserResponse::of);
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<UserResponse>> findUser(@PathVariable Long id) {
        return userService.findById(id)
                .map(u -> ResponseEntity.ok(UserResponse.of(u)))
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<?>> deleteUser(@PathVariable Long id) {
        // no content (204)
        return userService.deleteById(id).then(
                Mono.just(ResponseEntity.noContent().build())
        );
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<UserResponse>> updateUser(@PathVariable Long id, @RequestBody UserUpdateRequest request) {
        // user x : 404 not found
        // user o : 200 ok
        return userService.update(id,request.getName(),request.getEmail())
                .map(u -> ResponseEntity.ok(UserResponse.of(u)))
                // 전달된 값이 없으면 switchIfEmpty 가 실행
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }
}
