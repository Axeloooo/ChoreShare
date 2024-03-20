package com.choresync.user.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.choresync.user.model.UserAuthResponse;
import com.choresync.user.model.UserEditMetadataRequest;
import com.choresync.user.model.UserRequest;
import com.choresync.user.model.UserResponse;
import com.choresync.user.service.UserService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

  @Autowired
  private UserService userService;

  @PostMapping
  public ResponseEntity<UserAuthResponse> createUser(@RequestBody UserRequest userRequest) {
    UserAuthResponse authResponse = userService.createUser(userRequest);
    return new ResponseEntity<>(authResponse, HttpStatus.CREATED);
  }

  @GetMapping
  public ResponseEntity<List<UserResponse>> getAllUsers() {
    return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
  }

  @GetMapping("/{id}")
  public ResponseEntity<UserResponse> getUserById(@PathVariable("id") String id) {
    return new ResponseEntity<>(userService.getUserById(id), HttpStatus.OK);
  }

  @GetMapping("/username/{username}")
  public ResponseEntity<UserAuthResponse> getUserByUsername(@PathVariable("username") String username) {
    return new ResponseEntity<>(userService.getUserByUsername(username), HttpStatus.OK);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Map<String, String>> deleteUserById(@PathVariable String id) {
    userService.deleteUserById(id);
    Map<String, String> responseBody = new HashMap<>();
    responseBody.put("message", "User deleted successfully");
    return new ResponseEntity<>(responseBody, HttpStatus.OK);
  }

  @PutMapping("/{id}")
  public ResponseEntity<UserResponse> editUser(@PathVariable String id,
      @RequestBody UserEditMetadataRequest userRequest) {
    UserResponse editedUser = userService.editUser(id, userRequest);
    return new ResponseEntity<>(editedUser, HttpStatus.OK);
  }
}
