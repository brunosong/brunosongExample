package com.brunosong.exam.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface PostR2dbcRepository extends ReactiveCrudRepository<Post,Long>, PostCustomR2dbcRepository {
    Flux<Post> findByUserId(Long id);
}
