package com.example.LogIn.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public AuthenticationManager authenticationManager(
            UserDetailsService userDetailsService) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(provider);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // Public endpoints
                        .requestMatchers("/register", "/login", "/", "/csrf",
                               "/swagger-ui.html", "/swagger-ui/**", 
                               "/v3/api-docs/**", "/webjars/**").permitAll()
                        
                        // Admin-only endpoints
                        .requestMatchers(
                            "/api/products/**",  // All product management
                            "/api/inventory/**", // All inventory management
                            "/api/admin/**"      // Admin specific endpoints
                        ).hasRole("ADMIN")
                        
                        // User endpoints (both USER and ADMIN can access)
                        .requestMatchers(
                            "/api/orders/**",    // Order management
                            "/api/payments/**",  // Payment processing
                            "/api/user/**"       // User specific endpoints
                        ).hasAnyRole("USER", "ADMIN")
                        
                        // Default to requiring authentication
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .httpBasic();
        return http.build();
    }
}
