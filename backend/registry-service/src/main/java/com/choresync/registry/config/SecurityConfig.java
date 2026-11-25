package com.choresync.registry.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

/*
 * Every registry endpoint (dashboard and /eureka/** REST API) requires HTTP basic
 * auth. Without it anyone who can reach the registry can register a fake instance
 * of a service and receive its traffic.
 */
@Configuration
public class SecurityConfig {

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .csrf(csrf -> csrf.ignoringRequestMatchers("/eureka/**"))
        .authorizeHttpRequests(requests -> requests
            .requestMatchers("/actuator/health").permitAll()
            .anyRequest().authenticated())
        .httpBasic(Customizer.withDefaults());
    return http.build();
  }
}
