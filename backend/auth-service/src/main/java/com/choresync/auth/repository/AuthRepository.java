package com.choresync.auth.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.choresync.auth.entity.Auth;

public interface AuthRepository extends JpaRepository<Auth, String> {

  Optional<Auth> findByUsername(String username);

}
