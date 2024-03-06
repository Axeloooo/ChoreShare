package com.choresync.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.choresync.user.entity.User;

public interface UserRepository extends JpaRepository<User, String> {

  User findByEmail(String email);

  User findByPhone(String phone);

  User findByUsername(String username);
}
