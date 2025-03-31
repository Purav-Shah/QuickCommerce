package com.example.userservice.service;

import com.example.userservice.dto.UserRequest;
import com.example.userservice.dto.UserResponse;

import java.util.List;

public interface UserService {
    UserResponse createUser(UserRequest user);
    UserResponse getUserByEmail(String emailId);
    UserResponse getUserById(int userId);
    List<UserResponse> getAllUsers();
    UserResponse updateUser(UserRequest user);
    void deleteUserById(int userId);
    void deleteAllUsers();

}
