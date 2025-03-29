package com.example.authservice.service;

import com.example.authservice.feign.UserServiceCommunication;
import com.example.authservice.dto.DTO;
import com.example.authservice.dto.UserResponse;
import com.example.authservice.entity.UserDetailsImpl;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserServiceCommunication communicator;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            DTO<UserResponse> user = communicator.getUserByEmail(username);
            return new UserDetailsImpl(user.getData());
        } catch (FeignException.NotFound ex) {
            throw new UsernameNotFoundException("User with Email " + username + " not found.");
        }
    }
}
