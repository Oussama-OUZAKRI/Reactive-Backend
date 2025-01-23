package com.dev.infoapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.dev.infoapp.dto.UserInfoDetails;
import com.dev.infoapp.dto.UserLoginRequest;
import com.dev.infoapp.dto.UserRegisterRequest;
import com.dev.infoapp.model.User;
import com.dev.infoapp.repository.UserRepository;
import com.dev.infoapp.util.JwtUtil;

import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class UserService implements ReactiveUserDetailsService {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    
    @Autowired
    private AuthenticationManager authenticationManager;

    public Mono<User> registerUser(UserRegisterRequest user) {
    return userRepository.findByEmail(user.getEmail())
        .flatMap(existingUser -> Mono.error(new RuntimeException("Email déjà pris !")).cast(User.class))
        .switchIfEmpty(Mono.defer(() -> {
            User createdUser = User.builder()
                .fullName(user.getFullName())
                .email(user.getEmail())
                .password(passwordEncoder.encode(user.getPassword()))
                .roles(List.of("ROLE_USER"))
                .build();
            return userRepository.save(createdUser);
        }));
    }

    public String loginUser(UserLoginRequest user) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword())
        );
        if (authentication.isAuthenticated()) {
            return jwtUtil.generateToken(user.getEmail());
        } else {
            throw new UsernameNotFoundException("Invalid user request!");
        }
    }

    @Override  
    public Mono<UserDetails> findByUsername(String email) {  
        return userRepository.findByEmail(email)  
            .map(user -> (UserDetails) new UserInfoDetails(user))  
            .switchIfEmpty(Mono.error(new UsernameNotFoundException("User not found with email: " + email)));  
    }
}
