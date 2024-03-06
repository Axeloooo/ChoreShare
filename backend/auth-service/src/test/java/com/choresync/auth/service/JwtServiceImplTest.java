package com.choresync.auth.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.context.annotation.Description;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;

public class JwtServiceImplTest {
  private String userName;

  @InjectMocks
  private JwtServiceImpl jwtService;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);

    userName = "testUser";
  }

  @Description("Test to generate token")
  @Test
  public void testGenerateToken() {
    String token = jwtService.generateToken(userName);

    assertNotNull(token);
    assertFalse(token.isEmpty());

    Claims claims = Jwts.parserBuilder()
        .setSigningKey(jwtService.getSignKey())
        .build()
        .parseClaimsJws(token)
        .getBody();

    assertEquals(userName, claims.getSubject());
    assertNotNull(claims.getIssuedAt());
    assertNotNull(claims.getExpiration());
  }

  @Description("Test to validate token")
  @Test
  public void testValidateToken() {
    String token = jwtService.generateToken(userName);

    assertDoesNotThrow(() -> jwtService.validateToken(token));
  }

  @Description("Test to validate token with invalid token")
  @Test
  public void testValidateTokenWithInvalidToken() {
    String invalidToken = "invalidToken";

    assertThrows(MalformedJwtException.class, () -> jwtService.validateToken(invalidToken));
  }

  @Description("Test to validate token with expired token")
  @Test
  public void testValidateTokenWithExpiredToken() {
    Map<String, Object> claims = new HashMap<>();

    String expiredToken = Jwts.builder()
        .setClaims(claims)
        .setSubject(userName)
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis() - 1000))
        .signWith(jwtService.getSignKey())
        .compact();

    assertThrows(ExpiredJwtException.class, () -> jwtService.validateToken(expiredToken));
  }

  @Description("Test to validate token with null token")
  @Test
  public void testCreateToken() {
    Map<String, Object> claims = new HashMap<>();
    String token = jwtService.createToken(claims, userName);

    assertNotNull(token);
    assertFalse(token.isEmpty());

    Claims parsedClaims = Jwts.parserBuilder()
        .setSigningKey(jwtService.getSignKey())
        .build()
        .parseClaimsJws(token)
        .getBody();

    assertEquals(userName, parsedClaims.getSubject());
    assertNotNull(parsedClaims.getIssuedAt());
    assertNotNull(parsedClaims.getExpiration());
  }
}
