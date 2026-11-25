package com.choresync.gateway.util;

import java.security.Key;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

  private final Key signKey;

  /*
   * The signing key is a base64-encoded HMAC-SHA key (at least 256 bits) supplied
   * via the JWT_SECRET environment variable. It must match the auth-service's key.
   */
  public JwtUtil(@Value("${jwt.secret}") String secret) {
    this.signKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
  }

  public void validateToken(final String token) {
    Jwts.parserBuilder().setSigningKey(signKey).build().parseClaimsJws(token);
  }
}
