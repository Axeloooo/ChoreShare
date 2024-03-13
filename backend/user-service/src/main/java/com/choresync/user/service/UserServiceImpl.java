package com.choresync.user.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.choresync.user.model.UserResponse;
import com.choresync.user.entity.User;
import com.choresync.user.exception.UserAlreadyExistsException;
import com.choresync.user.exception.UserCreationException;
import com.choresync.user.exception.UserNotFoundException;
import com.choresync.user.model.UserAuthResponse;
import com.choresync.user.model.UserRequest;
import com.choresync.user.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

  @Autowired
  private UserRepository userRepository;

  @Override
  public UserAuthResponse createUser(UserRequest userRequest) {
    if (userRequest == null) {
      throw new UserCreationException("Invalid request body");
    }

    User existingUser = userRepository.findByEmail(userRequest.getEmail());

    if (existingUser != null) {
      throw new UserAlreadyExistsException("User with email " + userRequest.getEmail() + " already exists");
    }

    existingUser = userRepository.findByUsername(userRequest.getUsername());

    if (existingUser != null) {
      throw new UserAlreadyExistsException(
          "User with username " + userRequest.getUsername() + " already exists. Please choose a different username.");
    }

    User user = User.builder()
        .firstName(userRequest.getFirstName())
        .lastName(userRequest.getLastName())
        .username(userRequest.getUsername())
        .password(userRequest.getPassword())
        .email(userRequest.getEmail())
        .phone(userRequest.getPhone())
        .streak(0)
        .missedChores(0)
        .build();

    User newClient = userRepository.save(user);

    UserAuthResponse authResponse = UserAuthResponse.builder()
        .id(newClient.getId())
        .username(newClient.getUsername())
        .password(newClient.getPassword())
        .createdAt(newClient.getCreatedAt())
        .updatedAt(newClient.getUpdatedAt())
        .build();

    return authResponse;
  }

  @Override
  public List<UserResponse> getAllUsers() {
    List<User> users = userRepository.findAll();

    if (users.isEmpty()) {
      return Collections.emptyList();
    }

    List<UserResponse> userListResponse = new ArrayList<>();

    for (User user : users) {
      UserResponse userResponse = UserResponse.builder()
          .id(user.getId())
          .firstName(user.getFirstName())
          .lastName(user.getLastName())
          .username(user.getUsername())
          .email(user.getEmail())
          .phone(user.getPhone())
          .streak(user.getStreak())
          .missedChores(user.getMissedChores())
          .createdAt(user.getCreatedAt())
          .updatedAt(user.getUpdatedAt())
          .build();

      userListResponse.add(userResponse);
    }

    return userListResponse;
  }

  @Override
  public UserResponse getUserById(String id) {
    User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found"));

    UserResponse userResponse = UserResponse.builder()
        .id(user.getId())
        .firstName(user.getFirstName())
        .lastName(user.getLastName())
        .username(user.getUsername())
        .email(user.getEmail())
        .phone(user.getPhone())
        .streak(user.getStreak())
        .missedChores(user.getMissedChores())
        .createdAt(user.getCreatedAt())
        .updatedAt(user.getUpdatedAt())
        .build();

    return userResponse;
  }

  @Override
  public void deleteUserById(String id) {
    if (!userRepository.existsById(id)) {
      throw new UserNotFoundException("User not found");
    }

    userRepository.deleteById(id);
  }

  @Override
  public UserResponse editUser(String id, UserRequest userRequest) {
    User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found"));

    if (!userRequest.getEmail().equals(user.getEmail())) {
      User existingUser = userRepository.findByEmail(userRequest.getEmail());

      if (existingUser != null) {
        throw new UserAlreadyExistsException("This email address is already in use! Please choose another email.");
      }
    }

    if (!userRequest.getPhone().equals(user.getPhone())) {
      User existingUser = userRepository.findByPhone(userRequest.getPhone());

      if (existingUser != null) {
        throw new UserAlreadyExistsException("This phone number is already in use! Please choose another number.");
      }
    }

    user.setFirstName(userRequest.getFirstName());
    user.setLastName(userRequest.getLastName());
    user.setUsername(userRequest.getUsername());
    user.setEmail(userRequest.getEmail());
    user.setPhone(userRequest.getPhone());
    user.setStreak(user.getStreak());
    user.setMissedChores(user.getMissedChores());
    userRepository.save(user);

    UserResponse userResponse = UserResponse.builder()
        .id(user.getId())
        .firstName(user.getFirstName())
        .lastName(user.getLastName())
        .username(user.getUsername())
        .email(user.getEmail())
        .phone(user.getPhone())
        .streak(user.getStreak())
        .missedChores(user.getMissedChores())
        .createdAt(user.getCreatedAt())
        .updatedAt(user.getUpdatedAt())
        .build();

    return userResponse;
  }

  @Override
  public UserAuthResponse getUserByUsername(String username) {
    User user = userRepository.findByUsername(username);

    if (user == null) {
      throw new UserNotFoundException("User not found");
    }

    UserAuthResponse authResponse = UserAuthResponse.builder()
        .id(user.getId())
        .username(user.getUsername())
        .password(user.getPassword())
        .createdAt(user.getCreatedAt())
        .updatedAt(user.getUpdatedAt())
        .build();

    return authResponse;
  }
}
