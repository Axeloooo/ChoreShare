package com.choresync.user.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.choresync.user.model.UserResponse;
import com.choresync.user.entity.User;
import com.choresync.user.exception.UserAlreadyExistsException;
import com.choresync.user.exception.UserInvalidBodyException;
import com.choresync.user.exception.UserInvalidParamException;
import com.choresync.user.exception.UserNotFoundException;
import com.choresync.user.model.UserAuthResponse;
import com.choresync.user.model.UserEditMetadataRequest;
import com.choresync.user.model.UserRequest;
import com.choresync.user.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

  @Autowired
  private UserRepository userRepository;

  /*
   * Create a new user
   * 
   * @param userRequest
   * 
   * @return UserAuthResponse
   * 
   * @throws UserInvalidBodyException
   * 
   * @throws UserAlreadyExistsException
   */
  @Override
  public UserAuthResponse createUser(UserRequest userRequest) {
    if (userRequest.getFirstName().isBlank() || userRequest.getFirstName() == null ||
        userRequest.getLastName().isBlank() || userRequest.getLastName() == null ||
        userRequest.getUsername().isBlank() || userRequest.getUsername() == null ||
        userRequest.getPassword().isBlank() || userRequest.getPassword() == null ||
        userRequest.getEmail().isBlank() || userRequest.getEmail() == null ||
        userRequest.getPhone().isBlank() || userRequest.getPhone() == null) {
      throw new UserInvalidBodyException("Invalid request body");
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

    existingUser = userRepository.findByPhone(userRequest.getPhone());

    if (existingUser != null) {
      throw new UserAlreadyExistsException(
          "User with phone number " + userRequest.getPhone()
              + " already exists. Please choose a different phone number.");
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

  /*
   * Get all users
   * 
   * @return List<UserResponse>
   */
  @Override
  public List<UserResponse> getAllUsers() {
    List<User> users = userRepository.findAll();

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

  /*
   * Get user by id
   * 
   * @param id
   * 
   * @return UserResponse
   * 
   * @throws UserInvalidParamException
   * 
   * @throws UserNotFoundException
   */
  @Override
  public UserResponse getUserById(String id) {
    if (id.isBlank() || id == null) {
      throw new UserInvalidParamException("Invalid request parameter");
    }

    User user = userRepository.findById(id)
        .orElseThrow(() -> new UserNotFoundException("User not found"));

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

  /*
   * Delete user by id
   * 
   * @param id
   * 
   * @throws UserInvalidParamException
   * 
   * @throws UserNotFoundException
   */
  @Override
  public void deleteUserById(String id) {
    if (id.isBlank() || id == null) {
      throw new UserInvalidParamException("Invalid request parameter");
    }

    if (!userRepository.existsById(id)) {
      throw new UserNotFoundException("User not found");
    }

    userRepository.deleteById(id);
  }

  /*
   * Edit user by id
   * 
   * @param id
   * 
   * @param userRequest
   * 
   * @return UserResponse
   * 
   * @throws UserInvalidParamException
   * 
   * @throws UserInvalidBodyException
   * 
   * @throws UserNotFoundException
   * 
   * @throws UserAlreadyExistsException
   */
  @Override
  public UserResponse editUser(String id, UserEditMetadataRequest userRequest) {
    if (id.isBlank() || id == null) {
      throw new UserInvalidParamException("Invalid request parameter");
    }

    if (userRequest.getFirstName().isBlank() || userRequest.getFirstName() == null ||
        userRequest.getLastName().isBlank() || userRequest.getLastName() == null ||
        userRequest.getEmail().isBlank() || userRequest.getEmail() == null ||
        userRequest.getPhone().isBlank() || userRequest.getPhone() == null) {
      throw new UserInvalidBodyException("Invalid request body");
    }

    User user = userRepository.findById(id)
        .orElseThrow(() -> new UserNotFoundException("User not found"));

    if (!userRequest.getEmail().equals(user.getEmail())) {
      User existingUser = userRepository.findByEmail(userRequest.getEmail());

      if (existingUser != null) {
        throw new UserAlreadyExistsException("User with email " + userRequest.getEmail() + " already exists");
      }
    }

    if (!userRequest.getPhone().equals(user.getPhone())) {
      User existingUser = userRepository.findByPhone(userRequest.getPhone());

      if (existingUser != null) {
        throw new UserAlreadyExistsException(
            "User with phone number " + userRequest.getPhone()
                + " already exists. Please choose a different phone number.");
      }
    }

    user.setFirstName(userRequest.getFirstName());
    user.setLastName(userRequest.getLastName());
    user.setEmail(userRequest.getEmail());
    user.setPhone(userRequest.getPhone());
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

  /*
   * Get user by username
   * 
   * @param username
   * 
   * @return UserAuthResponse
   * 
   * @throws UserInvalidParamException
   * 
   * @throws UserNotFoundException
   */
  @Override
  public UserAuthResponse getUserByUsername(String username) {
    if (username.isBlank() || username == null) {
      throw new UserInvalidParamException("Invalid request parameter");
    }

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
