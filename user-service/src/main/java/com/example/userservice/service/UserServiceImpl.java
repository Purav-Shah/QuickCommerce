package com.example.userservice.service;

import com.example.userservice.dto.UserRequest;
import com.example.userservice.dto.UserResponse;
import com.example.userservice.entity.User;
import com.example.userservice.exception.NoUsersFoundException;
import com.example.userservice.exception.UserAlreadyExistsException;
import com.example.userservice.exception.UserNotFoundException;
import com.example.userservice.mapper.UserMapper;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.websocket.UserWebSocketHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserWebSocketHandler userWebSocketHandler;

    @Override
    public UserResponse createUser(UserRequest user) {
        if(userRepository.findByEmail(user.getEmail()).isPresent()){
            throw new UserAlreadyExistsException("User with email " + user.getEmail() + " already exists");
        }
        return userMapper.mapToDTO(userRepository.save(userMapper.mapToModel(user)));
    }

    @Override
    public UserResponse getUserByEmail(String emailId) {
        User exisitingUser =  userRepository.findByEmail(emailId)
                .orElseThrow(()-> new UserNotFoundException("User with Email " + emailId + " not found."));
        return userMapper.mapToDTO(exisitingUser);
    }

    @Override
    public UserResponse getUserById(int userId) {
        return userMapper.mapToDTO(userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User with Id " + userId + " not found.")));
    }

    @Override
    public List<UserResponse> getAllUsers() {
        List<User> users =  userRepository.findAll();
        if(users.isEmpty()){
            throw new NoUsersFoundException("No Users Found");
        }
        return users.stream().map(userMapper::mapToDTO).collect(Collectors.toList());
    }

    @Override
    public UserResponse updateUser(UserRequest user) {
        User existingUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new UserNotFoundException("Cannot update. User not found with ID: " + user.getId()));

        existingUser.setName(user.getName());
        existingUser.setEmail(user.getEmail());
        existingUser.setPassword(existingUser.getPassword());
        existingUser.setRole(user.getRole());

        UserResponse userSaved = userMapper.mapToDTO(userRepository.save(existingUser));
        userWebSocketHandler.sendUserUpdate(existingUser, false);
        return userSaved;
    }


    @Override
    public void deleteUserById(int userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Cannot delete. User not found with ID: " + userId));

        userRepository.deleteById(userId);
        userWebSocketHandler.sendUserUpdate(user, true);    }
    @Override
    public void deleteAllUsers() {
        List<User> users = userRepository.findAll();
        if (users.isEmpty()) {
            throw new NoUsersFoundException("No users to delete.");
        }
        userRepository.deleteAll();
    }
}
