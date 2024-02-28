package com.choresync.gateway.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class OktaSecurity {

  public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) {
    http
        .authorizeExchange(exchange -> exchange
            .anyExchange().authenticated())
        .oauth2Login(Customizer.withDefaults())
        .oauth2ResourceServer(server -> server
            .jwt(Customizer.withDefaults()));
    return http.build();
  }
}
