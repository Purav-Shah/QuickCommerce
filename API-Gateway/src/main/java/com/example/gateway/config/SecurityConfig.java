package com.example.gateway.config;

import com.example.gateway.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Bean
    SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        CorsConfiguration corsConfig = getCorsConfiguration();
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);

        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .cors(cors -> cors.configurationSource(source))
                .exceptionHandling(e -> e
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                        .accessDeniedHandler(jwtAccessDeniedHandler))
                .authorizeExchange(authorizeExchangeSpec -> authorizeExchangeSpec
                        // WebSocket endpoints
                        .pathMatchers("/ws/**").permitAll()

                        // CORS preflight requests
                        .pathMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // Auth Service - Public endpoints
                        .pathMatchers(HttpMethod.POST, "/auth-service/api/login").permitAll()
                        .pathMatchers(HttpMethod.POST, "/auth-service/api/register").permitAll()

                        // User Service - Admin only endpoints
                        .pathMatchers(HttpMethod.GET, "/api/users/users").hasRole("Admin")
                        .pathMatchers(HttpMethod.DELETE, "/api/users/user/**").hasRole("Admin")
                        .pathMatchers(HttpMethod.DELETE, "/api/users/users").hasRole("Admin")

                        // User Service - User/Admin endpoints
                        .pathMatchers(HttpMethod.GET, "/api/users/user/**").authenticated()
                        .pathMatchers(HttpMethod.PUT, "/api/users/user").authenticated()

                        // Product Service - Public endpoints
                        .pathMatchers(HttpMethod.GET, "/api/products").permitAll()
                        .pathMatchers(HttpMethod.GET, "/api/products/{id}").permitAll()
                        .pathMatchers(HttpMethod.GET, "/api/products/category/{category}").permitAll()
                        .pathMatchers(HttpMethod.GET, "/api/products/search").permitAll()

                        // Product Service - Admin only endpoints
                        .pathMatchers(HttpMethod.POST, "/api/products").hasRole("Admin")
                        .pathMatchers(HttpMethod.PUT, "/api/products/{id}").hasRole("Admin")
                        .pathMatchers(HttpMethod.DELETE, "/api/products/{id}").hasRole("Admin")

                        // Inventory Service - Admin only endpoints
                        .pathMatchers(HttpMethod.POST, "/api/inventory").hasRole("Admin")
                        .pathMatchers(HttpMethod.PUT, "/api/inventory/{productId}").hasRole("Admin")

                        // Inventory Service - User/Admin endpoints
                        .pathMatchers(HttpMethod.GET, "/api/inventory/check").authenticated()
                        .pathMatchers(HttpMethod.GET, "/api/inventory/{productId}").authenticated()

                        // Order Service - User/Admin endpoints
                        .pathMatchers(HttpMethod.POST, "/api/orders").authenticated()
                        .pathMatchers(HttpMethod.GET, "/api/orders/{id}").authenticated()
                        .pathMatchers(HttpMethod.GET, "/api/orders/user/{userId}").authenticated()
                        .pathMatchers(HttpMethod.PUT, "/api/orders/{id}/status").hasRole("Admin")

                        // Payment Service - User/Admin endpoints
                        .pathMatchers(HttpMethod.POST, "/api/payments/process").authenticated()
                        .pathMatchers(HttpMethod.GET, "/api/payments/{paymentId}").authenticated()
                        .pathMatchers(HttpMethod.GET, "/api/payments/order/{orderId}").authenticated()
                        .pathMatchers(HttpMethod.GET, "/api/payments/user/{userId}").authenticated()

                        // Default policy - require authentication for any other endpoints
                        .anyExchange().authenticated())
                .addFilterBefore(jwtAuthenticationFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .build();
    }

    private static CorsConfiguration getCorsConfiguration() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.addAllowedOriginPattern("*");
        corsConfig.addAllowedMethod("*");
        corsConfig.addAllowedHeader("*");
        corsConfig.setAllowCredentials(true);
        corsConfig.setMaxAge(3600L);

        // Add required exposed headers
        corsConfig.addExposedHeader("Authorization");
        corsConfig.addExposedHeader("Access-Control-Allow-Origin");
        corsConfig.addExposedHeader("Access-Control-Allow-Credentials");
        return corsConfig;
    }
}