package com.brunosong.exam.repository;

import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface UserR2dbcRepository extends R2dbcRepository<User,Long> {

    Flux<User> findByName(String name);
    Flux<User> findByNameOrderByIdDesc(String name);

    @Modifying
    @Query("delete from users where name = :name")
    Mono<Void> deleteByName(String name);

}
