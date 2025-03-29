package com.example.authservice.feign;

import com.example.authservice.dto.DTO;
import com.example.authservice.dto.UserRequest;
import com.example.authservice.dto.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "USER-SERVICE")
public interface UserServiceCommunication {
    @GetMapping("/api/user/email/{email}")
    public DTO<UserResponse> getUserByEmail(@PathVariable("email") String email);

    @PostMapping("/api/user")
    public DTO<UserResponse> createUser(@RequestBody UserRequest userRequest);
}
