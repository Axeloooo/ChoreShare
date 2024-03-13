package com.choresync.user.service;

import com.choresync.user.model.UserResponse;

import java.util.List;

import com.choresync.user.model.UserAuthResponse;
import com.choresync.user.model.UserRequest;

public interface UserService {
  UserAuthResponse createUser(UserRequest userRequest);

  List<UserResponse> getAllUsers();

  UserResponse getUserById(String id);

  void deleteUserById(String id);

  UserResponse editUser(String id, UserRequest userRequest);

  UserAuthResponse getUserByUsername(String username);
}
