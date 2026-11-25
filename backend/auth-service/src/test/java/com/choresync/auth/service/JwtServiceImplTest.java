package com.choresync.auth.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Description;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.WeakKeyException;

public class JwtServiceImplTest {
  private String userName;

  // Throwaway 256-bit test key; the real key comes from the JWT_SECRET env var.
  private static final String TEST_SECRET = "dGVzdC1vbmx5LWtleS10ZXN0LW9ubHkta2V5LXRlc3Qtb25seQ==";

  private JwtServiceImpl jwtService;

  @BeforeEach
  public void setUp() {
    jwtService = new JwtServiceImpl(TEST_SECRET);

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

  @Description("Test that tokens signed with a different key are rejected")
  @Test
  public void testValidateTokenSignedWithOtherKey() {
    JwtServiceImpl otherService = new JwtServiceImpl("b3RoZXIta2V5LW90aGVyLWtleS1vdGhlci1rZXktb3RoZXIta2V5");
    String foreignToken = otherService.generateToken(userName);

    assertThrows(JwtException.class, () -> jwtService.validateToken(foreignToken));
  }

  @Description("Test that a key shorter than 256 bits is refused at startup")
  @Test
  public void testRejectsWeakKey() {
    assertThrows(WeakKeyException.class, () -> new JwtServiceImpl("c2hvcnQ="));
  }
}
