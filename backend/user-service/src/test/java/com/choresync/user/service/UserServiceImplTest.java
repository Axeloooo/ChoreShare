package com.choresync.user.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.annotation.Description;

import com.choresync.user.entity.User;
import com.choresync.user.exception.UserAlreadyExistsException;
import com.choresync.user.exception.UserInvalidBodyException;
import com.choresync.user.exception.UserNotFoundException;
import com.choresync.user.model.UserAuthResponse;
import com.choresync.user.model.UserRequest;
import com.choresync.user.model.UserResponse;
import com.choresync.user.repository.UserRepository;

class UserServiceImplTest {

  @Mock
  private UserRepository userRepository;

  @InjectMocks
  private UserServiceImpl userService;

  private UserRequest userRequest;
  private User user;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);

    userRequest = UserRequest.builder()
        .firstName("John")
        .lastName("Doe")
        .username("johndoe")
        .email("john.doe@example.com")
        .password("password")
        .phone("1234567890")
        .build();

    user = User.builder()
        .id("1")
        .firstName("John")
        .lastName("Doe")
        .username("johndoe")
        .email("john.doe@example.com")
        .password("password")
        .phone("1234567890")
        .streak(0)
        .missedChores(0)
        .createdAt(new Date())
        .updatedAt(new Date())
        .build();
  }

  @Description("POST /api/v1/user - Test create user success")
  @Test
  public void testCreateUserSuccess() {
    UserAuthResponse authResponse = UserAuthResponse
        .builder()
        .id(user.getId())
        .username(user.getUsername())
        .password(user.getPassword())
        .createdAt(user.getCreatedAt())
        .updatedAt(user.getUpdatedAt())
        .build();

    when(userRepository.findByEmail(anyString())).thenReturn(null);
    when(userRepository.save(any(User.class))).thenReturn(user);

    UserAuthResponse result = userService.createUser(userRequest);

    assertNotNull(result);
    assertEquals(authResponse, result);
    assertEquals(user.getUsername(), authResponse.getUsername());

    verify(userRepository, times(1)).findByEmail(anyString());
    verify(userRepository, times(1)).save(any(User.class));
  }

  @Description("POST /api/v1/user - Test UserAlreadyExistsException")
  @Test
  public void testUserAlreadyExistsException() {
    when(userRepository.findByEmail(anyString())).thenReturn(user);

    assertThrows(UserAlreadyExistsException.class, () -> userService.createUser(userRequest));

    verify(userRepository, times(1)).findByEmail(anyString());
    verify(userRepository, times(0)).save(any(User.class));
  }

  @Description("POST /api/v1/user - Test UserCreationException")
  @Test
  public void testUserCreationException() {
    assertThrows(UserInvalidBodyException.class, () -> userService.createUser(null));

    verify(userRepository, times(0)).findByEmail(anyString());
    verify(userRepository, times(0)).save(any(User.class));
  }

  @Description("GET /api/v1/user - Test get all users success")
  @Test
  public void testGetAllUsersNonEmpty() {
    List<User> userList = Arrays.asList(user);
    when(userRepository.findAll()).thenReturn(userList);

    List<UserResponse> userResponses = userService.getAllUsers();

    assertNotNull(userResponses);
    assertFalse(userResponses.isEmpty());
    assertEquals(userList.size(), userResponses.size());

    verify(userRepository, times(1)).findAll();
  }

  @Description("GET /api/v1/user - Test get all empty users")
  @Test
  public void testGetAllUsersEmpty() {
    when(userRepository.findAll()).thenReturn(Arrays.asList());

    List<UserResponse> userResponses = userService.getAllUsers();

    assertNotNull(userResponses);
    assertTrue(userResponses.isEmpty());

    verify(userRepository, times(1)).findAll();
  }

  @Description("GET /api/v1/user/{id} - Test get user by id success")
  @Test
  public void testGetUserByIdSuccess() {
    when(userRepository.findById(anyString())).thenReturn(Optional.of(user));

    UserResponse userResponse = userService.getUserById("1");

    assertNotNull(userResponse);
    assertEquals(user.getId(), userResponse.getId());

    verify(userRepository, times(1)).findById(anyString());
  }

  @Description("GET /api/v1/user/{id} - Test UserNotFoundException")
  @Test
  public void testGetUserByIdNotFound() {
    when(userRepository.findById(anyString())).thenReturn(Optional.empty());

    assertThrows(UserNotFoundException.class, () -> userService.getUserById("1"));

    verify(userRepository, times(1)).findById(anyString());
  }

  @Description("DELETE /api/v1/user/{id} - Test delete user by id success")
  @Test
  public void testDeleteUserByIdSuccess() {
    when(userRepository.existsById(anyString())).thenReturn(true);

    assertDoesNotThrow(() -> userService.deleteUserById("1"));

    verify(userRepository, times(1)).existsById(anyString());
    verify(userRepository, times(1)).deleteById(anyString());
  }

  @Description("DELETE /api/v1/user/{id} - Test UserNotFoundException")
  @Test
  public void testDeleteUserByIdNotFound() {
    when(userRepository.existsById(anyString())).thenReturn(false);

    assertThrows(UserNotFoundException.class, () -> userService.deleteUserById("1"));

    verify(userRepository, times(1)).existsById(anyString());
    verify(userRepository, times(0)).deleteById(anyString());
  }

  // @Description("PUT /api/v1/user/{id} - Test edit user success")
  // @Test
  // public void testEditUserSuccess() {
  // when(userRepository.findById(anyString())).thenReturn(Optional.of(user));
  // when(userRepository.save(any(User.class))).thenReturn(user);

  // UserResponse userResponse = userService.editUser("1", userRequest);

  // assertNotNull(userResponse);
  // assertEquals(user.getId(), userResponse.getId());

  // verify(userRepository, times(1)).findById(anyString());
  // verify(userRepository, times(1)).save(any(User.class));
  // }

  // @Description("PUT /api/v1/user/{id} - Test UserNotFoundException")
  // @Test
  // public void testEditUserNotFound() {
  // when(userRepository.findById(anyString())).thenReturn(Optional.empty());

  // assertThrows(UserNotFoundException.class, () -> userService.editUser("1",
  // userRequest));

  // verify(userRepository, times(1)).findById(anyString());
  // verify(userRepository, times(0)).save(any(User.class));
  // }

  @Description("GET /api/v1/user/username/{username} - Test get user by username success")
  @Test
  public void testGetUserByUsernameSuccess() {
    when(userRepository.findByUsername(anyString())).thenReturn(user);

    UserAuthResponse authResponse = userService.getUserByUsername("johndoe");

    assertNotNull(authResponse);
    assertEquals(user.getUsername(), authResponse.getUsername());

    verify(userRepository, times(1)).findByUsername(anyString());
  }

  @Description("GET /api/v1/username/{username} - Test UserNotFoundException")
  @Test
  public void testGetUserByUsernameNotFound() {
    when(userRepository.findByUsername(anyString())).thenReturn(null);

    assertThrows(UserNotFoundException.class, () -> userService.getUserByUsername("johndoe"));

    verify(userRepository, times(1)).findByUsername(anyString());
  }

}
