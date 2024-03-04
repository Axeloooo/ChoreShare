package com.choresync.auth.config;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.choresync.auth.entity.Auth;
import com.choresync.auth.repository.AuthRepository;

@Component
public class CustomUserDetailsService implements UserDetailsService {

  @Autowired
  private AuthRepository authRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Optional<Auth> auth = authRepository.findByUsername(username);
    return auth.map(CustomUserDetails::new).orElseThrow(() -> new UsernameNotFoundException("Username not found"));
  }

}
