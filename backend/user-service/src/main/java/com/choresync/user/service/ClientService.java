package com.choresync.user.service;

import com.choresync.user.model.ClientResponse;

import java.util.List;

import com.choresync.user.model.ClientRequest;

public interface ClientService {

  ClientResponse createUser(ClientRequest userRequest);

  List<ClientResponse> getAllUsers();

  ClientResponse getUserById(String id);

  void deleteUserById(String id);

  ClientResponse editUser(String id, ClientRequest userRequest);

}
