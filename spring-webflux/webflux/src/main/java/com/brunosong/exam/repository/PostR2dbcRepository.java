package com.brunosong.exam.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostR2dbcRepository extends ReactiveCrudRepository<Post,Long> {
}
