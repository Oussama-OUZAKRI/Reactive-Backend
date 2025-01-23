package com.dev.infoapp.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.dev.infoapp.model.UserPreference;
import com.dev.infoapp.repository.UserPreferenceRepository;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserPreferenceService {
    final private UserPreferenceRepository repository;
    
    public Mono<ResponseEntity<UserPreference>> savePreference(UserPreference preference) {
        return repository.save(preference)
                         .map(savedPreference -> ResponseEntity.ok(savedPreference));
    }

    public Mono<UserPreference> getPreferences(Long userId) {
        return repository.findByUserId(userId);
    }
}
