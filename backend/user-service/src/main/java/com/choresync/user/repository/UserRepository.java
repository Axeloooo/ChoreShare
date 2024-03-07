package com.choresync.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.choresync.user.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

  User findByEmail(String email);

  User findByPhone(String phone);

  User findByUsername(String username);
}
