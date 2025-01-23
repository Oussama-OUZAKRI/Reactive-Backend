package com.dev.infoapp.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import com.dev.infoapp.model.UserPreference;

import reactor.core.publisher.Mono;

@Repository
public interface UserPreferenceRepository extends ReactiveCrudRepository<UserPreference, Long> {
    Mono<UserPreference> findByUserId(Long userId);
}
