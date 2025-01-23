package com.dev.infoapp.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.dev.infoapp.model.UserPreference;
import com.dev.infoapp.service.UserPreferenceService;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {
    final private UserPreferenceService service;

    @PostMapping
    public Mono<ResponseEntity<UserPreference>> savePreference(@RequestBody UserPreference preference) {
        return service.savePreference(preference);
    }

    @GetMapping("/{userId}")
    public Mono<UserPreference> getPreferences(@PathVariable Long userId) {
        return service.getPreferences(userId);
    }
}
