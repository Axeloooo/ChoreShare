package com.choresync.user.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.choresync.user.exceptions.UserAlreadyExistsException;
import com.choresync.user.exceptions.UserNotFoundException;
import com.choresync.user.model.ClientRequest;
import com.choresync.user.model.ClientResponse;
import com.choresync.user.service.ClientService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/v1/user")
public class ClientController {

  @Autowired
  private ClientService clientService;

  // Create user
  // url: /api/v1/user, method: POST
  // return type: ResponseEntity<ClientResponse>
  @PostMapping
  public ResponseEntity<ClientResponse> createUser(@RequestBody ClientRequest userRequest) {
    ClientResponse userResponse = clientService.createUser(userRequest);
    return new ResponseEntity<>(userResponse, HttpStatus.CREATED);
  }

  // Get all users
  // url: /api/v1/user/all, method: GET
  // return type: ResponseEntity<List<ClientResponse>>
  @GetMapping("/all")
  public ResponseEntity<List<ClientResponse>> getAllUsers() {
    return new ResponseEntity<>(clientService.getAllUsers(), HttpStatus.OK);
  }

  // Get user by id
  // url: /api/v1/user/{id}, method: GET
  // return type: ResponseEntity<ClientResponse>
  @GetMapping("/{id}")
  public ResponseEntity<ClientResponse> getUserById(@PathVariable String id) {
    return new ResponseEntity<>(clientService.getUserById(id), HttpStatus.OK);
  }

  // Delete user
  // url: /api/v1/user/{id}, method: DELETE
  @DeleteMapping("/{id}")
  public ResponseEntity<Object> deleteUserById(@PathVariable String id) {
    clientService.deleteUserById(id);
    Map<String, String> responseBody = new HashMap<>();
    responseBody.put("message", "User deleted successfully");
    return new ResponseEntity<>(responseBody, HttpStatus.OK);
  }

  // Update user
  // url: /api/v1/user/{id}
  // method: PUT
  // return type: ResponseEntity<ClientResponse>
  @PutMapping("/{id}")
  public ResponseEntity<ClientResponse> editUser(@PathVariable String id, @RequestBody ClientRequest userRequest) {
    ClientResponse editedUser = clientService.editUser(id, userRequest);
    return new ResponseEntity<>(editedUser, HttpStatus.OK);
  }

  // Exception Handlers
  @ExceptionHandler(UserAlreadyExistsException.class)
  public ResponseEntity<Object> handleUserAlreadyExistsException(UserAlreadyExistsException ex) {
    Map<String, Object> responseBody = new HashMap<>();
    responseBody.put("error", ex.getMessage());
    return new ResponseEntity<>(responseBody, HttpStatus.CONFLICT);
  }

  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity<Object> handleUserNotFoundExceptionException(UserNotFoundException ex) {
    Map<String, Object> responseBody = new HashMap<>();
    responseBody.put("error", ex.getMessage());
    return new ResponseEntity<>(responseBody, HttpStatus.NOT_FOUND);
  }

}
