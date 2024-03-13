package com.choresync.announcement.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.choresync.announcement.entity.Announcement;
import com.choresync.announcement.exception.AnnouncementCreationException;
import com.choresync.announcement.exception.AnnouncementNotFoundException;
import com.choresync.announcement.exception.HouseholdNotFoundException;
import com.choresync.announcement.external.response.HouseholdResponse;
import com.choresync.announcement.model.AnnouncementRequest;
import com.choresync.announcement.model.AnnouncementResponse;
import com.choresync.announcement.repository.AnnouncementRepository;

@Service
public class AnnouncementServiceImpl implements AnnouncementService {

  @Autowired
  private AnnouncementRepository announcementRepository;

  @Autowired
  private RestTemplate restTemplate;

  @Override
  public AnnouncementResponse createAnnouncement(AnnouncementRequest announcementRequest) {
    if (announcementRequest == null) {
      throw new AnnouncementCreationException("Missing announcement request body");
    }

    HouseholdResponse householdResponse;

    try {
      householdResponse = restTemplate.getForObject(
          "http://household-service/api/v1/household/" + announcementRequest.getHouseholdId(),
          HouseholdResponse.class);

    } catch (RestClientException e) {
      throw new HouseholdNotFoundException(
          "Could not find a household. " + e.getMessage());
    }

    if (householdResponse == null) {
      throw new HouseholdNotFoundException("Could not find a household");
    }

    Announcement announcement = Announcement.builder()
        .message(announcementRequest.getMessage())
        .householdId(announcementRequest.getHouseholdId())
        .userId(announcementRequest.getUserId())
        .author(announcementRequest.getAuthor())
        .build();

    Announcement newAnnouncement = announcementRepository.save(announcement);

    AnnouncementResponse announcementResponse = AnnouncementResponse.builder()
        .id(newAnnouncement.getId())
        .message(newAnnouncement.getMessage())
        .householdId(newAnnouncement.getHouseholdId())
        .userId(newAnnouncement.getUserId())
        .author(newAnnouncement.getAuthor())
        .createdAt(newAnnouncement.getCreatedAt())
        .updatedAt(newAnnouncement.getUpdatedAt())
        .build();

    return announcementResponse;
  }

  @Override
  public AnnouncementResponse getAnnouncementById(String id) {
    Announcement announcement = announcementRepository.findById(id)
        .orElseThrow(() -> new AnnouncementNotFoundException("Announcement not found"));

    AnnouncementResponse announcementResponse = AnnouncementResponse.builder()
        .id(announcement.getId())
        .message(announcement.getMessage())
        .householdId(announcement.getHouseholdId())
        .userId(announcement.getUserId())
        .author(announcement.getAuthor())
        .createdAt(announcement.getCreatedAt())
        .updatedAt(announcement.getUpdatedAt())
        .build();

    return announcementResponse;
  }

  @Override
  public List<AnnouncementResponse> getAllAnnouncementsByHousehold(String householdId) {
    List<Announcement> announcements = announcementRepository.findByHouseholdId(householdId);

    if (announcements.isEmpty()) {
      return Collections.emptyList();
    }

    List<AnnouncementResponse> announcementResponses = new ArrayList<>();

    for (Announcement announcement : announcements) {
      AnnouncementResponse announcementResponse = AnnouncementResponse.builder()
          .id(announcement.getId())
          .message(announcement.getMessage())
          .householdId(announcement.getHouseholdId())
          .userId(announcement.getUserId())
          .author(announcement.getAuthor())
          .createdAt(announcement.getCreatedAt())
          .updatedAt(announcement.getUpdatedAt())
          .build();
      announcementResponses.add(announcementResponse);
    }

    return announcementResponses;
  }

  @Override
  public AnnouncementResponse editAnnouncement(String id, AnnouncementRequest announcementRequest) {
    if (announcementRequest == null) {
      throw new AnnouncementCreationException("Missing announcement request body");
    }

    Announcement announcement = announcementRepository.findById(id)
        .orElseThrow(() -> new AnnouncementNotFoundException("Announcement not found"));

    announcement.setMessage(announcementRequest.getMessage());
    announcement.setUserId(announcementRequest.getUserId());
    announcementRepository.save(announcement);

    AnnouncementResponse announcementResponse = AnnouncementResponse.builder()
        .id(announcement.getId())
        .message(announcement.getMessage())
        .householdId(announcement.getHouseholdId())
        .userId(announcement.getUserId())
        .author(announcement.getAuthor())
        .createdAt(announcement.getCreatedAt())
        .updatedAt(announcement.getUpdatedAt())
        .build();

    return announcementResponse;
  }

  @Override
  public void deleteAnnouncement(String id) {
    if (!announcementRepository.existsById(id)) {
      throw new AnnouncementNotFoundException("Announcement not found");
    }
    announcementRepository.deleteById(id);
  }

  @Override
  public List<AnnouncementResponse> getAllAnnouncementsByUserId(String userId) {
    if (userId == null) {
      throw new AnnouncementNotFoundException("Announcement not found");
    }

    List<Announcement> announcements = announcementRepository.findByUserId(userId);

    List<AnnouncementResponse> announcementResponses = new ArrayList<>();

    for (Announcement announcement : announcements) {
      AnnouncementResponse announcementResponse = AnnouncementResponse.builder()
          .id(announcement.getId())
          .message(announcement.getMessage())
          .householdId(announcement.getHouseholdId())
          .userId(announcement.getUserId())
          .author(announcement.getAuthor())
          .createdAt(announcement.getCreatedAt())
          .updatedAt(announcement.getUpdatedAt())
          .build();

      announcementResponses.add(announcementResponse);
    }

    return announcementResponses;
  }
}
