package com.choresync.auth.service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtServiceImpl implements JwtService {
  private final Key signKey;

  /*
   * The signing key is a base64-encoded HMAC-SHA key (at least 256 bits) supplied
   * via the JWT_SECRET environment variable. It must match the api-gateway's key.
   */
  public JwtServiceImpl(@Value("${jwt.secret}") String secret) {
    this.signKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
  }

  public void validateToken(final String token) {
    Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token);
  }

  public String generateToken(String userName) {
    Map<String, Object> claims = new HashMap<>();
    return createToken(claims, userName);
  }

  public String createToken(Map<String, Object> claims, String userName) {
    return Jwts.builder()
        .setClaims(claims)
        .setSubject(userName)
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 30))
        .signWith(getSignKey(), SignatureAlgorithm.HS256)
        .compact();
  }

  public Key getSignKey() {
    return signKey;
  }
}
