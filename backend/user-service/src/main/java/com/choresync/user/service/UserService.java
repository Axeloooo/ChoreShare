package com.choresync.user.service;

import com.choresync.user.model.UserResponse;

import java.util.List;

import com.choresync.user.model.AuthResponse;
import com.choresync.user.model.UserRequest;

public interface UserService {
  AuthResponse createUser(UserRequest userRequest);

  List<UserResponse> getAllUsers();

  UserResponse getUserById(String id);

  void deleteUserById(String id);

  UserResponse editUser(String id, UserRequest userRequest);

  AuthResponse getUserByUsername(String username);
}
