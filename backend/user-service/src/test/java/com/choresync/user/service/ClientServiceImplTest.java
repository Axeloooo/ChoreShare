package com.choresync.user.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Description;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import com.choresync.user.entity.Client;
import com.choresync.user.model.ClientRequest;
import com.choresync.user.model.ClientResponse;
import com.choresync.user.repository.ClientRepository;

@SpringBootTest
@AutoConfigureMockMvc
class ClientServiceImplTest {

  @Mock
  private ClientRepository clientRepository;

  @InjectMocks
  private ClientServiceImpl clientService;

  private ClientRequest clientRequest;
  private Client client;
  private ClientResponse clientResponse;

  @BeforeEach
  void setUp() {
    clientRequest = ClientRequest
        .builder()
        .firstName("Ben")
        .lastName("Dover")
        .username("ben.dover")
        .email("mariia@gmail.com")
        .password("password")
        .phone("1234567890")
        .build();

    client = Client
        .builder()
        .id("1")
        .firstName("Ben")
        .lastName("Dover")
        .username("ben.dover")
        .email("mariia@gmail.com")
        .password("password")
        .phone("1234567890")
        .streak(0)
        .missedChores(0)
        .createdAt(new Date())
        .updatedAt(new Date())
        .build();

    clientResponse = ClientResponse
        .builder()
        .id("1")
        .firstName("Ben")
        .lastName("Dover")
        .username("ben.dover")
        .email("mariia@gmail.com")
        .phone("1234567890")
        .streak(0)
        .missedChores(0)
        .createdAt(new Date())
        .updatedAt(new Date())
        .build();
  }

  @Description("Post successful user creation")
  @Test
  void testCreateUser_Success() {
    when(clientRepository.save(any(Client.class)))
        .thenReturn(client);

    ClientResponse response = clientService.createUser(clientRequest);

    assertNotNull(response);

    assertEquals(clientResponse, response);

    verify(clientRepository, times(1)).save(any(Client.class));
  }

  @Description("Post unsuccesful user creation")
  @Test
  void testCreateUser_NullRequest() {
    Exception exception = assertThrows(RuntimeException.class, () -> {
      clientService.createUser(null);
    });

    String expectedMessage = "Client request must not be null"; // This message should match what your service method
                                                                // throws
    String actualMessage = exception.getMessage();

    assertTrue(actualMessage.contains(expectedMessage));
  }

}
