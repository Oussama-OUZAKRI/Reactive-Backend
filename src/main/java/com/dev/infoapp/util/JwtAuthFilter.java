package com.dev.infoapp.util;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.dev.infoapp.service.UserService;

import reactor.core.publisher.Mono;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

@Component
public class JwtAuthFilter implements WebFilter {

    @Autowired
    private JwtUtil jwtUtil;
    
    @Autowired
    @Lazy
    private UserService userService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String authHeader = request.getHeaders().getFirst("Authorization");
        String email = null;
        String jwt = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7);
            email = jwtUtil.extractUsername(jwt);
        }

        final String finalJwt = jwt;
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            return userService.findByUsername(email)
                .filter(userDetails -> jwtUtil.validateToken(finalJwt, userDetails))
                .map(userDetails -> {
                    UsernamePasswordAuthenticationToken authToken = 
                        new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                        );
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    return userDetails;
                })
                .then(chain.filter(exchange));
        }

        return chain.filter(exchange);
    }
}