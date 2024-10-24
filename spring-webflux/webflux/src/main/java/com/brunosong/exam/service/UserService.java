package com.brunosong.exam.service;

import com.brunosong.exam.repository.User;
import com.brunosong.exam.repository.UserR2dbcRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserService {

    //private final UserRepository userRepository;
    private final UserR2dbcRepository userRepository;

    public Mono<User> create(String name, String email) {
        return userRepository.save(User.builder()
                        .name(name)
                        .email(email)
                        .build());
    }

    public Flux<User> findAll() {
        return userRepository.findAll();
    }

    public Mono<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public Mono<Void> deleteById(Long id) {
        return userRepository.deleteById(id);
    }

    public Mono<Void> deleteByName(String name) {
        return userRepository.deleteByName(name);
    }

    public Mono<User> update(Long id, String name, String email) {
        return userRepository.findById(id)
                .flatMap(u -> {
                    u.setName(name);
                    u.setEmail(email);
                    return userRepository.save(u);
                });
    }
}
