package com.brunosong.exam.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;

class UserRepositoryImplTest {

    private final UserRepository userRepository = new UserRepositoryImpl();

    @Test
    void save() {
        User brunosong = User.builder().name("brunosong").email("bruno@gmailcom").build();
        StepVerifier.create(userRepository.save(brunosong))
                .assertNext(u -> {
                    Assertions.assertThat(1L).isEqualTo(u.getId());
                    Assertions.assertThat("brunosong").isEqualTo(u.getName());
                    Assertions.assertThat("bruno@gmailcom").isEqualTo(u.getEmail());
                }).verifyComplete();

    }

    @Test
    void findAll() {
        userRepository.save(User.builder().name("brunosong").email("bruno@gmailcom").build());
        userRepository.save(User.builder().name("brunosong2").email("bruno2@gmailcom").build());
        userRepository.save(User.builder().name("brunosong3").email("bruno3@gmailcom").build());

        StepVerifier.create(userRepository.findAll())
                .expectNextCount(3)
                .verifyComplete();
    }

    @Test
    void findById() {
        userRepository.save(User.builder().name("brunosong").email("bruno@gmailcom").build());
        userRepository.save(User.builder().name("brunosong2").email("bruno2@gmailcom").build());

        StepVerifier.create(userRepository.findById(2L))
                .assertNext(u -> {
                    Assertions.assertThat(2L).isEqualTo(u.getId());
                    Assertions.assertThat("brunosong2").isEqualTo(u.getName());
                    Assertions.assertThat("bruno2@gmailcom").isEqualTo(u.getEmail());
                }).verifyComplete();
    }

    @Test
    void deleteById() {
        userRepository.save(User.builder().name("brunosong").email("bruno@gmailcom").build());
        userRepository.save(User.builder().name("brunosong2").email("bruno2@gmailcom").build());

        StepVerifier.create(userRepository.deleteById(1L))
                .expectNext(1)
                .verifyComplete();
    }
}