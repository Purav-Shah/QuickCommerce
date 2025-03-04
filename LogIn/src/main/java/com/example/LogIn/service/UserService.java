package com.example.LogIn.service;

import com.example.LogIn.model.Users;
import com.example.LogIn.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepo repo;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public String register(Users user) {
        // Check if username already exists
        if (repo.findByUsername(user.getUsername()) != null) {
            return "Username already exists! Please choose a different one.";
        }

        user.setPassword(encoder.encode(user.getPassword()));
        if (user.getRole() == null) {
            user.setRole("USER"); // Default role assigned
        }
        repo.save(user);
        return "User registered successfully!";
    }

    public boolean login(String username, String password) {
        Users user = repo.findByUsername(username);
        return user != null && encoder.matches(password, user.getPassword());
    }
}
