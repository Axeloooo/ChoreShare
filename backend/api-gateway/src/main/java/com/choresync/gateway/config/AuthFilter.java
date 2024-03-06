package com.choresync.gateway.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import com.choresync.gateway.exception.AuthMissingTokenException;
import com.choresync.gateway.exception.AuthUnauthorizedAccessException;
import com.choresync.gateway.util.JwtUtil;

@Component
public class AuthFilter extends AbstractGatewayFilterFactory<AuthFilter.Config> {

  @Autowired
  private RouteValidator validator;

  @Autowired
  private JwtUtil jwtUtil;

  public AuthFilter() {
    super(Config.class);
  }

  @Override
  public GatewayFilter apply(Config config) {
    return ((exchange, chain) -> {
      if (validator.isSecured.test(exchange.getRequest())) {

        if (!exchange
            .getRequest()
            .getHeaders()
            .containsKey(HttpHeaders.AUTHORIZATION)) {
          throw new AuthMissingTokenException("Missing Authorization Token");
        }

        String authHeader = exchange
            .getRequest()
            .getHeaders()
            .get(HttpHeaders.AUTHORIZATION)
            .get(0);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
          authHeader = authHeader.substring(7);
        }
        try {
          jwtUtil.validateToken(authHeader);
        } catch (Exception e) {
          throw new AuthUnauthorizedAccessException("Unauthorized Access");
        }
      }
      return chain.filter(exchange);
    });
  }

  public static class Config {

  }
}
