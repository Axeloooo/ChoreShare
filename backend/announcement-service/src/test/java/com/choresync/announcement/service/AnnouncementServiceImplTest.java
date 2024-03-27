package com.choresync.announcement.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.annotation.Description;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.choresync.announcement.entity.Announcement;
import com.choresync.announcement.exception.AnnouncementInternalCommunicationException;
import com.choresync.announcement.exception.AnnouncementInvalidBodyException;
import com.choresync.announcement.exception.AnnouncementInvalidParamException;
import com.choresync.announcement.exception.AnnouncementNotFoundException;
import com.choresync.announcement.external.exception.HouseholdNotFoundException;
import com.choresync.announcement.external.exception.UserNotFoundException;
import com.choresync.announcement.external.response.HouseholdResponse;
import com.choresync.announcement.external.response.UserResponse;
import com.choresync.announcement.model.AnnouncementRequest;
import com.choresync.announcement.model.AnnouncementResponse;
import com.choresync.announcement.repository.AnnouncementRepository;

public class AnnouncementServiceImplTest {

  @Mock
  private AnnouncementRepository announcementRepository;

  @InjectMocks
  private AnnouncementServiceImpl announcementService;

  private Announcement announcement;
  private AnnouncementRequest invalidAnnouncementRequest;
  private AnnouncementRequest announcementRequest;
  private AnnouncementResponse announcementResponse;
  private UserResponse userResponse;
  private HouseholdResponse householdResponse;

  @Mock
  private RestTemplate restTemplate;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);

    announcement = Announcement.builder()
        .id("1")
        .message("Test Announcement")
        .userId("user1")
        .householdId("house 1")
        .author(null)
        .createdAt(new Date())
        .updatedAt(new Date())
        .build();

    invalidAnnouncementRequest = AnnouncementRequest
        .builder()
        .build();

    announcementRequest = AnnouncementRequest
        .builder()
        .message("Test Announcement")
        .author(null)
        .userId("user1")
        .householdId("house 1")
        .build();

    announcementResponse = AnnouncementResponse.builder()
        .id("1")
        .message("Test Announcement")
        .userId("user1")
        .householdId("house 1")
        .author(null)
        .createdAt(new Date())
        .updatedAt(new Date())
        .build();

    userResponse = UserResponse
        .builder()
        .id("user1")
        .firstName("John")
        .lastName("Doe")
        .username("johndoe")
        .email("axel@gmail.com")
        .phone("1234567890")
        .missedChores(0)
        .streak(0)
        .createdAt(new Date())
        .updatedAt(new Date())
        .build();

    householdResponse = HouseholdResponse
        .builder()
        .id("household1")
        .name("Household 1")
        .createdAt(new Date())
        .updatedAt(new Date())
        .build();
  }

  @Description("POST /api/v1/announcement - Test create announcement with invalid body")
  @Test
  public void testCreateAnnouncementWithInvalidBody() {
    assertThrows(AnnouncementInvalidBodyException.class,
        () -> announcementService.createAnnouncement(invalidAnnouncementRequest));
  }

  @Description("POST /api/v1/announcement - Test create announcement with household not found")
  @Test
  public void testCreateAnnouncementWithHouseholdNotFound() {
    when(restTemplate.getForObject("http://household-service/api/v1/household/" + announcementRequest.getHouseholdId(),
        HouseholdResponse.class)).thenReturn(null);

    assertThrows(HouseholdNotFoundException.class, () -> announcementService.createAnnouncement(announcementRequest));
  }

  @Description("POST /api/v1/announcement - Test create announcement with user not found")
  @Test
  public void testCreateAnnouncementWithUserNotFound() {
    when(restTemplate.getForObject("http://household-service/api/v1/household/" + announcementRequest.getHouseholdId(),
        HouseholdResponse.class)).thenReturn(new HouseholdResponse());
    when(restTemplate.getForObject("http://user-service/api/v1/user/" + announcementRequest.getUserId(),
        UserResponse.class)).thenReturn(null);

    assertThrows(UserNotFoundException.class, () -> announcementService.createAnnouncement(announcementRequest));
  }

  @Description("POST /api/v1/announcement - Test create announcement with internal communication exception")
  @Test
  public void testCreateAnnouncementWithInternalCommunicationException() {
    when(restTemplate.getForObject("http://household-service/api/v1/household/" + announcementRequest.getHouseholdId(),
        HouseholdResponse.class)).thenThrow(new RestClientException("Internal error"));

    assertThrows(AnnouncementInternalCommunicationException.class,
        () -> announcementService.createAnnouncement(announcementRequest));
  }

  @Description("POST /api/v1/announcement - Test create announcement success")
  @Test
  public void testCreateAnnouncementSuccess() {
    when(restTemplate.getForObject("http://household-service/api/v1/household/" + announcementRequest.getHouseholdId(),
        HouseholdResponse.class)).thenReturn(new HouseholdResponse());
    when(restTemplate.getForObject("http://user-service/api/v1/user/" + announcementRequest.getUserId(),
        UserResponse.class)).thenReturn(new UserResponse());
    when(announcementRepository.save(any(Announcement.class))).thenReturn(announcement);

    AnnouncementResponse response = announcementService.createAnnouncement(announcementRequest);

    assertNotNull(response);
    assertEquals(announcementResponse.getId(), response.getId());

    verify(restTemplate, times(1)).getForObject(
        "http://household-service/api/v1/household/" + announcementRequest.getHouseholdId(),
        HouseholdResponse.class);
    verify(restTemplate, times(1)).getForObject("http://user-service/api/v1/user/" + announcementRequest.getUserId(),
        UserResponse.class);
    verify(announcementRepository, times(1)).save(any(Announcement.class));
  }

  @Description("GET /api/v1/announcement/{id} - Test get announcement by id with invalid param")
  @Test
  public void testGetAnnouncementByIdWithInvalidParam() {
    assertThrows(AnnouncementInvalidParamException.class, () -> announcementService.getAnnouncementById(null));
  }

  @Description("GET /api/v1/announcement/{id} - Test get announcement by id with announcement not found")
  @Test
  public void testGetAnnouncementByIdWithAnnouncementNotFound() {
    when(announcementRepository.findById(anyString())).thenReturn(Optional.empty());

    assertThrows(AnnouncementNotFoundException.class, () -> announcementService.getAnnouncementById("1"));
  }

  @Description("GET /api/v1/announcement/{id} - Test get announcement by id success")
  @Test
  public void testGetAnnouncementByIdSuccess() {
    when(announcementRepository.findById(anyString())).thenReturn(Optional.of(announcement));
    AnnouncementResponse response = announcementService.getAnnouncementById("1");
    assertNotNull(response);
    assertEquals(announcementResponse.getId(), response.getId());

    verify(announcementRepository, times(1)).findById("1");
  }

  @Description("GET /api/v1/announcement/household/{householdId} - Test get all announcements by household id with invalid param")
  @Test
  public void testGetAllAnnouncementsByHouseholdIdWithInvalidParam() {
    assertThrows(AnnouncementInvalidParamException.class,
        () -> announcementService.getAllAnnouncementsByHouseholdId(null));
  }

  @Description("GET /api/v1/announcement/household/{householdId} - Test get all announcements by household id with household not found")
  @Test
  public void testGetAllAnnouncementsByHouseholdIdWithHouseholdNotFound() {
    when(restTemplate.getForObject("http://household-service/api/v1/household/" + householdResponse.getId(),
        HouseholdResponse.class)).thenReturn(null);

    assertThrows(HouseholdNotFoundException.class,
        () -> announcementService.getAllAnnouncementsByHouseholdId(householdResponse.getId()));
  }

  @Description("GET /api/v1/announcement/household/{householdId} - Test get all announcements by household id with internal communication exception")
  @Test
  public void testGetAllAnnouncementsByHouseholdIdWithInternalCommunicationException() {
    when(restTemplate.getForObject("http://household-service/api/v1/household/" + householdResponse.getId(),
        HouseholdResponse.class)).thenThrow(new RestClientException("Internal error"));
    assertThrows(AnnouncementInternalCommunicationException.class,
        () -> announcementService.getAllAnnouncementsByHouseholdId(householdResponse.getId()));
  }

  @Description("GET /api/v1/announcement/household/{householdId} - Test get all announcements by household id success")
  @Test
  public void testGetAllAnnouncementsByHouseholdIdSuccess() {
    List<Announcement> announcements = Collections.singletonList(announcement);

    when(restTemplate.getForObject("http://household-service/api/v1/household/" + householdResponse.getId(),
        HouseholdResponse.class)).thenReturn(householdResponse);
    when(announcementRepository.findByHouseholdId(householdResponse.getId())).thenReturn(announcements);

    List<AnnouncementResponse> responses = announcementService
        .getAllAnnouncementsByHouseholdId(householdResponse.getId());

    assertNotNull(responses);
    assertEquals(1, responses.size());
    assertEquals(announcementResponse, responses.get(0));

    verify(restTemplate, times(1)).getForObject(
        "http://household-service/api/v1/household/" + householdResponse.getId(),
        HouseholdResponse.class);
    verify(announcementRepository, times(1)).findByHouseholdId(householdResponse.getId());
  }

  @Description("PUT /api/v1/announcement/{id} - Test edit announcement with invalid param")
  @Test
  public void testEditAnnouncementWithInvalidParam() {
    assertThrows(AnnouncementInvalidParamException.class,
        () -> announcementService.editAnnouncement(null, announcementRequest));
  }

  @Description("PUT /api/v1/announcement/{id} - Test edit announcement with invalid body")
  @Test
  public void testEditAnnouncementWithInvalidBody() {
    assertThrows(AnnouncementInvalidBodyException.class,
        () -> announcementService.editAnnouncement("1", invalidAnnouncementRequest));
  }

  @Description("PUT /api/v1/announcement/{id} - Test edit announcement with announcement not found")
  @Test
  public void testEditAnnouncementWithAnnouncementNotFound() {
    when(announcementRepository.findById(anyString())).thenReturn(Optional.empty());

    assertThrows(AnnouncementNotFoundException.class,
        () -> announcementService.editAnnouncement("1", announcementRequest));
  }

  @Description("PUT /api/v1/announcement/{id} - Test edit announcement success")
  @Test
  public void testEditAnnouncementSuccess() {
    when(announcementRepository.findById(anyString())).thenReturn(Optional.of(announcement));
    when(announcementRepository.save(any(Announcement.class))).thenReturn(announcement);

    AnnouncementResponse response = announcementService.editAnnouncement("1", announcementRequest);

    assertNotNull(response);
    assertEquals(announcementResponse.getId(), response.getId());

    verify(announcementRepository, times(1)).findById("1");
    verify(announcementRepository, times(1)).save(any(Announcement.class));
  }

  @Description("DELETE /api/v1/announcement/{id} - Test delete announcement with invalid param")
  @Test
  public void testDeleteAnnouncementWithInvalidParam() {
    assertThrows(AnnouncementInvalidParamException.class, () -> announcementService.deleteAnnouncement(null));
  }

  @Description("DELETE /api/v1/announcement/{id} - Test delete announcement with announcement not found")
  @Test
  public void testDeleteAnnouncementWithAnnouncementNotFound() {
    when(announcementRepository.existsById(anyString())).thenReturn(false);

    assertThrows(AnnouncementNotFoundException.class, () -> announcementService.deleteAnnouncement("1"));
  }

  @Description("DELETE /api/v1/announcement/{id} - Test delete announcement success")
  @Test
  public void testDeleteAnnouncementSuccess() {
    when(announcementRepository.existsById(anyString())).thenReturn(true);

    doNothing().when(announcementRepository).deleteById(anyString());
    announcementService.deleteAnnouncement("1");

    verify(announcementRepository, times(1)).deleteById("1");
  }

  @Description("GET /api/v1/announcement/user/{userId} - Test get all announcements by user id with invalid param")
  @Test
  public void testGetAllAnnouncementsByUserIdWithInvalidParam() {
    assertThrows(AnnouncementInvalidParamException.class, () -> announcementService.getAllAnnouncementsByUserId(null));
  }

  @Description("GET /api/v1/announcement/user/{userId} - Test get all announcements by user id with user not found")
  @Test
  public void testGetAllAnnouncementsByUserIdWithUserNotFound() {
    when(restTemplate.getForObject("http://user-service/api/v1/user/" + userResponse.getId(), UserResponse.class))
        .thenReturn(null);

    assertThrows(UserNotFoundException.class,
        () -> announcementService.getAllAnnouncementsByUserId(userResponse.getId()));
  }

  @Description("GET /api/v1/announcement/user/{userId} - Test get all announcements by user id with internal communication exception")
  @Test
  public void testGetAllAnnouncementsByUserIdWithInternalCommunicationException() {
    when(restTemplate.getForObject("http://user-service/api/v1/user/" + userResponse.getId(), UserResponse.class))
        .thenThrow(new RestClientException("Internal error"));

    assertThrows(AnnouncementInternalCommunicationException.class,
        () -> announcementService.getAllAnnouncementsByUserId(userResponse.getId()));
  }

  @Description("GET /api/v1/announcement/user/{userId} - Test get all announcements by user id success")
  @Test
  public void testGetAllAnnouncementsByUserIdSuccess() {
    List<Announcement> announcements = Collections.singletonList(announcement);

    when(restTemplate.getForObject("http://user-service/api/v1/user/" + userResponse.getId(), UserResponse.class))
        .thenReturn(userResponse);
    when(announcementRepository.findByUserId(userResponse.getId())).thenReturn(announcements);

    List<AnnouncementResponse> responses = announcementService.getAllAnnouncementsByUserId(userResponse.getId());

    assertNotNull(responses);
    assertEquals(1, responses.size());
    assertEquals(announcementResponse, responses.get(0));

    verify(restTemplate, times(1)).getForObject("http://user-service/api/v1/user/" + userResponse.getId(),
        UserResponse.class);
    verify(announcementRepository, times(1)).findByUserId(userResponse.getId());
  }
}
