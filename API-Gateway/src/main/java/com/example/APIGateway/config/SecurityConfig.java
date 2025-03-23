package com.example.APIGateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeExchange(authorize -> authorize
                // Public endpoints
                .pathMatchers("/api/auth/**", 
                             "/api/products/**",
                             "/swagger-ui.html",
                             "/swagger-ui/**",
                             "/v3/api-docs/**",
                             "/webjars/**",
                             "/actuator/**").permitAll()
                .anyExchange().permitAll()
            );
        return http.build();
    }
} 