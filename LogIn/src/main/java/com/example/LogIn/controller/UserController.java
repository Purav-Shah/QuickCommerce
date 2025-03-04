package com.example.LogIn.controller;

import com.example.LogIn.model.Users;
import com.example.LogIn.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    @Autowired
    private UserService service;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody Users user) {
        String result = service.register(user);
        if (result.equals("Username already exists! Please choose a different one.")) {
            return ResponseEntity.status(400).body(result);
        }
        return ResponseEntity.ok(result);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Users user) {
        boolean isAuthenticated = service.login(user.getUsername(), user.getPassword());
        if (isAuthenticated) {
            return ResponseEntity.ok("Login successful");
        } else {
            return ResponseEntity.status(401).body("Invalid username or password");
        }
    }

    @GetMapping("/csrf")
    public CsrfToken getcsrfToken(HttpServletRequest request){
        return (CsrfToken) request.getAttribute("_csrf");
    }

    @GetMapping("/")
    public String greet(HttpServletRequest request){
        return "Welcome to Log In" + request.getSession().getId();
    }
}
