package com.choresync.user.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.util.Collections;
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
import com.choresync.user.exception.UserInvalidParamException;
import com.choresync.user.exception.UserNotFoundException;
import com.choresync.user.model.UserAuthResponse;
import com.choresync.user.model.UserEditMetadataRequest;
import com.choresync.user.model.UserRequest;
import com.choresync.user.model.UserResponse;
import com.choresync.user.repository.UserRepository;

class UserServiceImplTest {

  @Mock
  private UserRepository userRepository;

  @InjectMocks
  private UserServiceImpl userService;

  private UserRequest userRequest;
  private UserRequest invalidUserRequest;
  private UserEditMetadataRequest userMetadata;
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

    userMetadata = UserEditMetadataRequest.builder()
        .firstName("John")
        .lastName("Doe")
        .email("axel.doe@example.com")
        .phone("1234567899")
        .build();

    invalidUserRequest = UserRequest.builder()
        .firstName("Jhon")
        .lastName(null)
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

  @Description("POST /api/v1/user - Test create user with invalid body")
  @Test
  public void testCreateUserWithInvalidBody() {
    assertThrows(UserInvalidBodyException.class, () -> userService.createUser(invalidUserRequest));
  }

  @Description("POST /api/v1/user - Test create user with user already exists by email")
  @Test
  public void testCreateUserWithUserAlreadyExistsByEmail() {
    when(userRepository.findByEmail(userRequest.getEmail())).thenReturn(user);

    assertThrows(UserAlreadyExistsException.class, () -> userService.createUser(userRequest));
  }

  @Description("POST /api/v1/user - Test create user with user already exists by username")
  @Test
  public void testCreateUserWithUserAlreadyExistsByUsername() {
    when(userRepository.findByUsername(userRequest.getUsername())).thenReturn(user);

    assertThrows(UserAlreadyExistsException.class, () -> userService.createUser(userRequest));
  }

  @Description("POST /api/v1/user - Test create user with user already exists by phone")
  @Test
  public void testCreateUserWithUserAlreadyExistsByPhone() {
    when(userRepository.findByPhone(userRequest.getPhone())).thenReturn(user);
    assertThrows(UserAlreadyExistsException.class, () -> userService.createUser(userRequest));
  }

  @Description("POST /api/v1/user - Test create user success")
  @Test
  public void testCreateUserSuccess() {
    when(userRepository.findByEmail(userRequest.getEmail())).thenReturn(null);
    when(userRepository.findByUsername(userRequest.getUsername())).thenReturn(null);
    when(userRepository.findByPhone(userRequest.getPhone())).thenReturn(null);
    when(userRepository.save(any(User.class))).thenReturn(user);

    UserAuthResponse response = userService.createUser(userRequest);

    assertNotNull(response);
    assertEquals(user.getId(), response.getId());
    assertEquals(user.getUsername(), response.getUsername());
    assertEquals(user.getPassword(), response.getPassword());

    verify(userRepository, times(1)).findByEmail(anyString());
    verify(userRepository, times(1)).findByUsername(anyString());
    verify(userRepository, times(1)).findByPhone(anyString());
    verify(userRepository, times(1)).save(any(User.class));
  }

  @Description("GET /api/v1/user - Test get all users when no users exist")
  @Test
  public void testGetAllUsersWhenNoUsersExist() {
    when(userRepository.findAll()).thenReturn(Collections.emptyList());

    List<UserResponse> response = userService.getAllUsers();

    assertTrue(response.isEmpty());
  }

  @Description("GET /api/v1/user - Test get all users when users exist")
  @Test
  public void testGetAllUsersWhenUsersExist() {
    List<User> users = Collections.singletonList(user);

    when(userRepository.findAll()).thenReturn(users);

    List<UserResponse> response = userService.getAllUsers();

    assertNotNull(response);
    assertFalse(response.isEmpty());
    assertEquals(1, response.size());
    assertEquals(user.getId(), response.get(0).getId());
    assertEquals(user.getFirstName(), response.get(0).getFirstName());
    assertEquals(user.getLastName(), response.get(0).getLastName());
    assertEquals(user.getUsername(), response.get(0).getUsername());
    assertEquals(user.getEmail(), response.get(0).getEmail());
    assertEquals(user.getPhone(), response.get(0).getPhone());
    assertEquals(user.getStreak(), response.get(0).getStreak());
    assertEquals(user.getMissedChores(), response.get(0).getMissedChores());
    assertEquals(user.getCreatedAt(), response.get(0).getCreatedAt());
    assertEquals(user.getUpdatedAt(), response.get(0).getUpdatedAt());

    verify(userRepository, times(1)).findAll();
  }

  @Description("GET /api/v1/user/{id} - Test get user by id with invalid param")
  @Test
  public void testGetUserByIdWithInvalidParam() {
    assertThrows(UserInvalidParamException.class, () -> userService.getUserById(null));
  }

  @Description("GET /api/v1/user/{id} - Test get user by id with user not found")
  @Test
  public void testGetUserByIdWithUserNotFound() {
    when(userRepository.findById(anyString())).thenReturn(Optional.empty());

    assertThrows(UserNotFoundException.class, () -> userService.getUserById("1"));
  }

  @Description("GET /api/v1/user/{id} - Test get user by id success")
  @Test
  public void testGetUserByIdSuccess() {
    when(userRepository.findById(anyString())).thenReturn(Optional.of(user));

    UserResponse response = userService.getUserById("1");

    assertNotNull(response);
    assertEquals(user.getId(), response.getId());
    assertEquals(user.getFirstName(), response.getFirstName());
    assertEquals(user.getLastName(), response.getLastName());
    assertEquals(user.getUsername(), response.getUsername());
    assertEquals(user.getEmail(), response.getEmail());
    assertEquals(user.getPhone(), response.getPhone());
    assertEquals(user.getStreak(), response.getStreak());
    assertEquals(user.getMissedChores(), response.getMissedChores());
    assertEquals(user.getCreatedAt(), response.getCreatedAt());
    assertEquals(user.getUpdatedAt(), response.getUpdatedAt());

    verify(userRepository, times(1)).findById(anyString());
  }

  @Description("DELETE /api/v1/user/{id} - Test delete user by id with invalid param")
  @Test
  public void testDeleteUserByIdWithInvalidParam() {
    assertThrows(UserInvalidParamException.class, () -> userService.deleteUserById(null));
  }

  @Description("DELETE /api/v1/user/{id} - Test delete user by id with user not found")
  @Test
  public void testDeleteUserByIdWithUserNotFound() {
    when(userRepository.existsById(anyString())).thenReturn(false);

    assertThrows(UserNotFoundException.class, () -> userService.deleteUserById("1"));
  }

  @Description("DELETE /api/v1/user/{id} - Test delete user by id success")
  @Test
  public void testDeleteUserByIdSuccess() {
    when(userRepository.existsById(anyString())).thenReturn(true);
    doNothing().when(userRepository).deleteById(anyString());

    userService.deleteUserById("1");

    verify(userRepository, times(1)).deleteById("1");
  }

  @Description("PUT /api/v1/user/{id} - Test edit user by id with invalid param")
  @Test
  public void testEditUserByIdWithInvalidParam() {
    assertThrows(UserInvalidParamException.class, () -> userService.editUser(null, userMetadata));
  }

  @Description("PUT /api/v1/user/{id} - Test edit user by id with invalid body")
  @Test
  public void testEditUserByIdWithInvalidBody() {
    assertThrows(UserInvalidBodyException.class, () -> userService.editUser("1", new UserEditMetadataRequest()));
  }

  @Description("PUT /api/v1/user/{id} - Test edit user by id with user not found")
  @Test
  public void testEditUserByIdWithUserNotFound() {
    when(userRepository.findById(anyString())).thenReturn(Optional.empty());

    assertThrows(UserNotFoundException.class, () -> userService.editUser("1", userMetadata));
  }

  @Description("PUT /api/v1/user/{id} - Test edit user by id with email already exists")
  @Test
  public void testEditUserByIdWithEmailAlreadyExists() {
    when(userRepository.findById(anyString())).thenReturn(Optional.of(user));
    when(userRepository.findByEmail(anyString())).thenReturn(new User());

    assertThrows(UserAlreadyExistsException.class, () -> userService.editUser("1", userMetadata));
  }

  @Description("PUT /api/v1/user/{id} - Test edit user by id with phone already exists")
  @Test
  public void testEditUserByIdWithPhoneAlreadyExists() {
    when(userRepository.findById(anyString())).thenReturn(Optional.of(user));
    when(userRepository.findByPhone(anyString())).thenReturn(new User());

    assertThrows(UserAlreadyExistsException.class, () -> userService.editUser("1", userMetadata));
  }

  @Description("PUT /api/v1/user/{id} - Test edit user by id success")
  @Test
  public void testEditUserByIdSuccess() {
    when(userRepository.findById(anyString())).thenReturn(Optional.of(user));
    when(userRepository.findByEmail(anyString())).thenReturn(null);
    when(userRepository.findByPhone(anyString())).thenReturn(null);
    when(userRepository.save(any(User.class))).thenReturn(user);

    UserResponse response = userService.editUser("1", userMetadata);

    assertNotNull(response);
    assertEquals(user.getId(), response.getId());
    assertEquals(user.getFirstName(), response.getFirstName());
    assertEquals(user.getLastName(), response.getLastName());
    assertEquals(user.getUsername(), response.getUsername());
    assertEquals(user.getEmail(), response.getEmail());
    assertEquals(user.getPhone(), response.getPhone());
    assertEquals(user.getStreak(), response.getStreak());
    assertEquals(user.getMissedChores(), response.getMissedChores());
    assertEquals(user.getCreatedAt(), response.getCreatedAt());
    assertEquals(user.getUpdatedAt(), response.getUpdatedAt());

    verify(userRepository, times(1)).findById(anyString());
    verify(userRepository, times(1)).findByEmail(anyString());
    verify(userRepository, times(1)).findByPhone(anyString());
    verify(userRepository, times(1)).save(any(User.class));
  }

  @Description("GET /api/v1/user/username/{username} - Test get user by username with invalid param")
  @Test
  public void testGetUserByUsernameWithInvalidParam() {
    assertThrows(UserInvalidParamException.class, () -> userService.getUserByUsername(null));
  }

  @Description("GET /api/v1/user/username/{username} - Test get user by username with user not found")
  @Test
  public void testGetUserByUsernameWithUserNotFound() {
    when(userRepository.findByUsername(anyString())).thenReturn(null);

    assertThrows(UserNotFoundException.class, () -> userService.getUserByUsername("johndoe"));
  }

  @Description("GET /api/v1/user/username/{username} - Test get user by username success")
  @Test
  public void testGetUserByUsernameSuccess() {
    when(userRepository.findByUsername(anyString())).thenReturn(user);

    UserAuthResponse response = userService.getUserByUsername("johndoe");

    assertNotNull(response);
    assertEquals(user.getId(), response.getId());
    assertEquals(user.getUsername(), response.getUsername());
    assertEquals(user.getPassword(), response.getPassword());
    assertEquals(user.getCreatedAt(), response.getCreatedAt());
    assertEquals(user.getUpdatedAt(), response.getUpdatedAt());

    verify(userRepository, times(1)).findByUsername(anyString());
  }
}
