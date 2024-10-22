package com.brunosong.exam.controller;

import com.brunosong.exam.dto.UserCreateRequest;
import com.brunosong.exam.dto.UserResponse;
import com.brunosong.exam.dto.UserUpdateRequest;
import com.brunosong.exam.repository.User;
import com.brunosong.exam.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

import static org.mockito.Mockito.when;


@WebFluxTest(UserController.class)
@AutoConfigureWebTestClient
class UserControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private UserService userService;

    @Test
    void createUser() {

        when(userService.create("brunosong", "bruno@gmail.com"))
                .thenReturn(Mono.just(new User(1L,"brunosong", "bruno@gmail.com",
                LocalDateTime.now(), LocalDateTime.now())));

        webTestClient.post().uri("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserCreateRequest("brunosong", "bruno@gmail.com"))
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(UserResponse.class)
                .value(res -> {
                    Assertions.assertEquals("brunosong", res.getName());
                    Assertions.assertEquals("bruno@gmail.com", res.getEmail());
                });
    }

    @Test
    void findAllUsers() {

        when(userService.findAll())
                .thenReturn(Flux.just(
                        new User(1L,"brunosong", "bruno@gmail.com", LocalDateTime.now(), LocalDateTime.now()),
                        new User(2L,"brunosong", "bruno@gmail.com", LocalDateTime.now(), LocalDateTime.now()),
                        new User(3L,"brunosong", "bruno@gmail.com", LocalDateTime.now(), LocalDateTime.now())
                ));

        webTestClient.get().uri("/users")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBodyList(UserResponse.class)
                .hasSize(3);
    }

    @Test
    void findUser200() {

        when(userService.findById(1L))
                .thenReturn(Mono.just(new User(1L,"brunosong", "bruno@gmail.com", LocalDateTime.now(), LocalDateTime.now())));

        webTestClient.get().uri("/users/1")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(UserResponse.class)
                .value(res -> {
                    Assertions.assertEquals("brunosong", res.getName());
                    Assertions.assertEquals("bruno@gmail.com", res.getEmail());
                });

    }

    @Test
    void findUser404() {

        when(userService.findById(1L))
                .thenReturn(Mono.empty());

        webTestClient.get().uri("/users/1")
                .exchange()
                .expectStatus().is4xxClientError();

    }

    @Test
    void deleteUser() {

        when(userService.deleteById(1L))
                .thenReturn(Mono.empty());

        webTestClient.delete().uri("/users/1")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(Integer.class);

    }

    @Test
    void updateUser() {

        User brunosong = new User(1L, "brunosong", "bruno@gmail.com", LocalDateTime.now(), LocalDateTime.now());
        when(userService.update(1L, "brunosong", "bruno@gmail.com")).thenReturn(Mono.just(brunosong));

        webTestClient.put().uri("/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserUpdateRequest("brunosong", "bruno@gmail.com"))
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(UserResponse.class)
                .value(res -> {
                    Assertions.assertEquals("brunosong", res.getName());
                    Assertions.assertEquals("bruno@gmail.com", res.getEmail());
                });
    }

}