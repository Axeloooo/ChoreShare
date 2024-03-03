package com.choresync.user.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.choresync.user.model.ClientResponse;
import com.choresync.user.entity.Client;
import com.choresync.user.exceptions.UserAlreadyExistsException;
import com.choresync.user.exceptions.UserNotFoundException;
import com.choresync.user.model.ClientRequest;
import com.choresync.user.repository.ClientRepository;

@Service
public class ClientServiceImpl implements ClientService {

  @Autowired
  private ClientRepository clientRepository;

  @Override
  public ClientResponse createUser(ClientRequest userRequest) {
    // if (userRequest == null) {
    // throw new RuntimeException("Client request must not be null");
    // }

    Client existingUser = clientRepository.findByEmail(userRequest.getEmail());

    if (existingUser != null) {
      throw new UserAlreadyExistsException("User with email " + userRequest.getEmail() + " already exists");
    }

    Client user = Client.builder()
        .firstName(userRequest.getFirstName())
        .lastName(userRequest.getLastName())
        .username(userRequest.getUsername())
        .password(userRequest.getPassword())
        .email(userRequest.getEmail())
        .phone(userRequest.getPhone())
        .streak(0)
        .missedChores(0)
        .build();

    Client newClient = clientRepository.save(user);

    ClientResponse userResponse = ClientResponse.builder()
        .id(newClient.getId())
        .firstName(newClient.getFirstName())
        .lastName(newClient.getLastName())
        .username(newClient.getUsername())
        .email(newClient.getEmail())
        .phone(newClient.getPhone())
        .streak(newClient.getStreak())
        .missedChores(newClient.getMissedChores())
        .createdAt(newClient.getCreatedAt())
        .updatedAt(newClient.getUpdatedAt())
        .build();

    return userResponse;
  }

  @Override
  public List<ClientResponse> getAllUsers() {
    List<Client> users = clientRepository.findAll();

    if (users.isEmpty()) {
      return Collections.emptyList();
    }

    List<ClientResponse> userListResponse = new ArrayList<>();

    for (Client user : users) {
      ClientResponse userResponse = ClientResponse.builder()
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
  public ClientResponse getUserById(String id) {
    Client user = clientRepository.findById(id).orElse(null);

    if (user == null) {
      throw new UserNotFoundException("User not found");
    }

    ClientResponse userResponse = ClientResponse.builder()
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
    if (!clientRepository.existsById(id)) {
      throw new UserNotFoundException("User not found");
    }

    clientRepository.deleteById(id);
  }

  @Override
  public ClientResponse editUser(String id, ClientRequest userRequest) {
    Client user = clientRepository.findById(id).orElse(null);

    if (user == null) {
      throw new UserNotFoundException("User not found");
    }

    // checko to ensure email is unique
    if (!userRequest.getEmail().equals(user.getEmail())) {
      Client existingUser = clientRepository.findByEmail(userRequest.getEmail());

      if (existingUser != null) {
        throw new UserAlreadyExistsException("This email address is already in use! Please choose another email.");
      }
    }

    // check to ensure phone is unique
    if (!userRequest.getPhone().equals(user.getPhone())) {
      Client existingUser = clientRepository.findByPhone(userRequest.getPhone());

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
    clientRepository.save(user);

    ClientResponse userResponse = ClientResponse.builder()
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
}
