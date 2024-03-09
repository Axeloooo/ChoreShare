package com.choresync.announcement.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Arrays;
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

import com.choresync.announcement.entity.Announcement;
import com.choresync.announcement.exception.AnnouncementCreationException;
import com.choresync.announcement.exception.AnnouncementNotFoundException;
import com.choresync.announcement.model.AnnouncementRequest;
import com.choresync.announcement.model.AnnouncementResponse;
import com.choresync.announcement.repository.AnnouncementRepository;

public class AnnouncementServiceImplTest {

  @Mock
  private AnnouncementRepository announcementRepository;

  @InjectMocks
  private AnnouncementServiceImpl announcementService;

  private Announcement announcement;
  private AnnouncementRequest announcementRequest;
  private AnnouncementResponse announcementResponse;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);

    announcement = Announcement.builder()
        .id("1")
        .message("Test Announcement")
        .userId("user1")
        .createdAt(new Date())
        .updatedAt(new Date())
        .build();

    announcementRequest = AnnouncementRequest
        .builder()
        .message("Test Announcement")
        .userId("user1")
        .build();

    announcementResponse = AnnouncementResponse.builder()
        .id("1")
        .message("Test Announcement")
        .userId("user1")
        .createdAt(new Date())
        .updatedAt(new Date())
        .build();
  }

  @Description("POST /api/v1/announcement - Test create announcement")
  @Test
  public void testCreateAnnouncement() {
    when(announcementRepository.save(any(Announcement.class)))
        .thenReturn(announcement);

    AnnouncementResponse response = announcementService.createAnnouncement(announcementRequest);

    assertNotNull(response);
    assertEquals(announcementResponse, response);

    verify(announcementRepository, times(1)).save(any(Announcement.class));
  }

  @Description("POST /api/v1/announcement - Test AnnouncementCreationException when request is null")
  @Test
  public void testCreateAnnouncementNullRequest() {
    assertThrows(AnnouncementCreationException.class, () -> {
      announcementService.createAnnouncement(null);
    });

    verify(announcementRepository, times(0)).save(any(Announcement.class));
  }

  @Description("GET /api/v1/announcement/{id} - Test get announcement by id")
  @Test
  public void testGetAnnouncementById() {
    when(announcementRepository.findById("1"))
        .thenReturn(Optional.of(announcement));

    AnnouncementResponse response = announcementService.getAnnouncementById("1");

    assertNotNull(response);
    assertEquals(announcementResponse, response);

    verify(announcementRepository, times(1)).findById("1");
  }

  @Description("GET /api/v1/announcement/{id} - Test AnnouncementNotFoundException when announcement is not found")
  @Test
  public void testGetAnnouncementByIdNotFound() {
    assertThrows(AnnouncementNotFoundException.class, () -> {
      announcementService.getAnnouncementById("1");
    });

    verify(announcementRepository, times(1)).findById("1");
  }

  @Description("GET /api/v1/announcement - Test get all announcements")
  @Test
  public void testGetAllAnnouncements() {
    when(announcementRepository.findAll()).thenReturn(Arrays.asList(announcement));

    List<AnnouncementResponse> responses = announcementService.getAllAnnouncements();

    assertNotNull(responses);
    assertEquals(1, responses.size());
    assertEquals(announcementResponse, responses.get(0));

    verify(announcementRepository, times(1)).findAll();
  }

  @Description("GET /api/v1/announcement - Test get all empty announcements")
  @Test
  public void testGetAllAnnouncementsEmpty() {
    when(announcementRepository.findAll()).thenReturn(Collections.emptyList());

    List<AnnouncementResponse> responses = announcementService.getAllAnnouncements();

    assertNotNull(responses);
    assertEquals(0, responses.size());

    verify(announcementRepository, times(1)).findAll();
  }

  @Description("GET /api/v1/announcement/user/{uid} - Test get all announcements by user id")
  @Test
  public void testGetAllAnnouncementsByUserId() {
    when(announcementRepository.findByUserId("user1")).thenReturn(Arrays.asList(announcement));

    List<AnnouncementResponse> responses = announcementService.getAllAnnouncementsByUserId("user1");

    assertNotNull(responses);
    assertEquals(1, responses.size());
    assertEquals(announcementResponse, responses.get(0));

    verify(announcementRepository, times(1)).findByUserId("user1");
  }

  @Description("GET /api/v1/announcement/user/{uid} - Test get all empty announcements by user id")
  @Test
  public void testGetAllAnnouncementsByUserIdEmpty() {
    when(announcementRepository.findByUserId("user1")).thenReturn(Collections.emptyList());

    List<AnnouncementResponse> responses = announcementService.getAllAnnouncementsByUserId("user1");

    assertNotNull(responses);
    assertEquals(0, responses.size());

    verify(announcementRepository, times(1)).findByUserId("user1");
  }

  @Description("GET /api/v1/announcement/user/{uid} - Test AnnouncementNotFoundException when user id is not found")
  @Test
  public void testGetAllAnnouncementsByUserIdNotFound() {
    assertThrows(AnnouncementNotFoundException.class, () -> {
      announcementService.getAllAnnouncementsByUserId(null);
    });

    verify(announcementRepository, times(0)).findByUserId("user1");
  }

  @Description("PUT /api/v1/announcement/{id} - Test edit announcement")
  @Test
  public void testEditAnnouncement() {
    when(announcementRepository.findById("1")).thenReturn(Optional.of(announcement));
    when(announcementRepository.save(any(Announcement.class))).thenReturn(announcement);

    AnnouncementResponse response = announcementService.editAnnouncement("1", announcementRequest);

    assertNotNull(response);
    assertEquals(announcementResponse, response);

    verify(announcementRepository, times(1)).findById("1");
    verify(announcementRepository, times(1)).save(any(Announcement.class));
  }

  @Description("PUT /api/v1/announcement/{id} - Test AnnouncementNotFoundException when announcement is not found")
  @Test
  public void testEditAnnouncementNotFound() {
    assertThrows(AnnouncementNotFoundException.class, () -> {
      announcementService.editAnnouncement("1", announcementRequest);
    });

    verify(announcementRepository, times(1)).findById("1");
    verify(announcementRepository, times(0)).save(any(Announcement.class));
  }

  @Description("PUT /api/v1/announcement/{id} - Test AnnouncementCreationException when request is null")
  @Test
  public void testEditAnnouncementNullRequest() {
    assertThrows(AnnouncementCreationException.class, () -> {
      announcementService.editAnnouncement("1", null);
    });

    verify(announcementRepository, times(0)).save(any(Announcement.class));
  }

  @Description("DELETE /api/v1/announcement/{id} - Test delete announcement")
  @Test
  public void testDeleteAnnouncement() {
    when(announcementRepository.existsById("1")).thenReturn(true);

    announcementService.deleteAnnouncement("1");

    verify(announcementRepository, times(1)).deleteById("1");
  }

  @Description("DELETE /api/v1/announcement/{id} - Test AnnouncementNotFoundException when announcement is not found")
  @Test
  public void testDeleteAnnouncementNotFound() {
    assertThrows(AnnouncementNotFoundException.class, () -> {
      announcementService.deleteAnnouncement("1");
    });

    verify(announcementRepository, times(0)).deleteById("1");
  }
}
