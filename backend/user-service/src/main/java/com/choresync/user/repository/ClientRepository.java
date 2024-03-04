package com.choresync.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.choresync.user.entity.Client;

public interface ClientRepository extends JpaRepository<Client, String> {

  Client findByEmail(String email);

  Client findByPhone(String phone);
}
