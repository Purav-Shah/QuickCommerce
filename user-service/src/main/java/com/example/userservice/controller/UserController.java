package com.example.userservice.controller;

import com.example.userservice.dto.DTO;
import com.example.userservice.dto.UserRequest;
import com.example.userservice.dto.UserResponse;
import com.example.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {
    private final UserService service;

    @GetMapping("/user/email/{email}")
    public ResponseEntity<DTO<UserResponse>> getUserByEmail(@PathVariable String email){
        UserResponse user = service.getUserByEmail(email);
        return ResponseEntity.ok(DTO.<UserResponse>builder()
                .success(true)
                .message("User found !")
                .data(user)
                .build());
    }

    @GetMapping("/users")
    public ResponseEntity<DTO<List<UserResponse>>> getAllUsers() {
        List<UserResponse> users = service.getAllUsers();
        return ResponseEntity.ok(DTO.<List<UserResponse>>builder()
                .success(true)
                .message("Users Fetched Successfully")
                .data(users)
                .build());
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<DTO<UserResponse>> getUserById(@PathVariable int id){
        UserResponse user =  service.getUserById(id);
        return  ResponseEntity.ok(DTO.<UserResponse>builder()
                .success(true)
                .message("User Fetched Successfully")
                .data(user)
                .build());
    }

    @PostMapping("/user")
    public ResponseEntity<DTO<UserResponse>> createUser(@RequestBody UserRequest user) {
        UserResponse createdUser = service.createUser(user);
        return ResponseEntity.status(201).body(DTO.<UserResponse>builder()
                .success(true)
                .message("User Created Successfully !")
                .data(createdUser)
                .build());
    }

    @PutMapping("/user")
    public ResponseEntity<DTO<UserResponse>> updateUser(@RequestBody UserRequest user){
        UserResponse updateUser = service.updateUser(user);
        return ResponseEntity.status(201).body(DTO.<UserResponse>builder()
                .success(true)
                .message("User Created Successfully !")
                .data(updateUser)
                .build());
    }

    @DeleteMapping("/user/{id}")
    private ResponseEntity<DTO<String>> deleteUserById(@PathVariable int id){
        service.deleteUserById(id);
        return ResponseEntity.ok(DTO.<String>builder()
                .success(true)
                .message("User Deleted Successfully")
                .data(null)
                .build());
    }

    @PutMapping("/user/{id}/role")
    public ResponseEntity<DTO<UserResponse>> updateUserRole(@PathVariable int id, @RequestBody UserRequest userRequest) {
        userRequest.setId(id);
        UserResponse updatedUser = service.updateUser(userRequest);
        return ResponseEntity.ok(DTO.<UserResponse>builder()
                .success(true)
                .message("User Role Updated Successfully")
                .data(updatedUser)
                .build());
    }

    @DeleteMapping("/users")
    private ResponseEntity<DTO<String>> deleteAllUsers(){
        service.deleteAllUsers();
        return ResponseEntity.ok(DTO.<String>builder()
                .success(true)
                .message("All Users Deleted Successfully")
                .data(null)
                .build());
    }

}
