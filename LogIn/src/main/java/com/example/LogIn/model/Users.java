package com.example.LogIn.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int user_id;
    private String username;
    private String password;
    private String role;  // Add this field (e.g., "USER" or "ADMIN")
}
