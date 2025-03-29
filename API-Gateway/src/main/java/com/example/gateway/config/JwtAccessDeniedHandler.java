package com.example.gateway.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.gateway.dto.DTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Component
public class JwtAccessDeniedHandler implements ServerAccessDeniedHandler {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, AccessDeniedException denied) {
        DTO<String> customResponse = new DTO<>(false, "Only Admins can Access this resource", null);


        byte[] bytes;
        try {
            bytes = objectMapper.writeValueAsBytes(customResponse);
        } catch (Exception e) {
            bytes = "{\"success\":false,\"message\":\"Only Admins can access this resource\",\"data\":null}"
                    .getBytes(StandardCharsets.UTF_8);
        }

        exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
        return exchange.getResponse().writeWith(Mono.just(exchange.getResponse().bufferFactory().wrap(bytes)));
    }
}
