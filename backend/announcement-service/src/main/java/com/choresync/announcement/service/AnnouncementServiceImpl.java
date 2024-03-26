package com.choresync.announcement.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.choresync.announcement.entity.Announcement;
import com.choresync.announcement.exception.AnnouncementInvalidBodyException;
import com.choresync.announcement.exception.AnnouncementInternalCommunicationException;
import com.choresync.announcement.exception.AnnouncementInvalidParamException;
import com.choresync.announcement.exception.AnnouncementNotFoundException;
import com.choresync.announcement.external.exception.HouseholdNotFoundException;
import com.choresync.announcement.external.exception.UserNotFoundException;
import com.choresync.announcement.external.response.HouseholdResponse;
import com.choresync.announcement.external.response.UserResponse;
import com.choresync.announcement.model.AnnouncementRequest;
import com.choresync.announcement.model.AnnouncementResponse;
import com.choresync.announcement.repository.AnnouncementRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class AnnouncementServiceImpl implements AnnouncementService {

  @Autowired
  private AnnouncementRepository announcementRepository;

  @Autowired
  private RestTemplate restTemplate;

  /*
   * Extracts the error message from a RestClientException
   * 
   * @param e
   * 
   * @return String
   */
  @Override
  public String extractErrorMessage(RestClientException e) {
    String rawMessage = e.getMessage();

    try {
      String jsonSubstring = rawMessage.substring(rawMessage.indexOf("{"), rawMessage.lastIndexOf("}") + 1);

      ObjectMapper objectMapper = new ObjectMapper();
      JsonNode rootNode = objectMapper.readTree(jsonSubstring);

      if (rootNode.has("message")) {
        return rootNode.get("message").asText();
      }
    } catch (JsonProcessingException ex) {
      System.out.println("Error parsing JSON from exception message: " + ex.getMessage());
    } catch (StringIndexOutOfBoundsException ex) {
      System.out.println("Error extracting JSON substring from exception message: " + ex.getMessage());
    }
    return rawMessage;
  }

  /*
   * Creates a new announcement
   * 
   * @param announcementRequest
   * 
   * @return AnnouncementResponse
   * 
   * @throws AnnouncementInvalidBodyException
   * 
   * @throws HouseholdNotFoundException
   * 
   * @throws UserNotFoundException
   * 
   * @throws AnnouncementInternalCommunicationException
   */
  @Override
  public AnnouncementResponse createAnnouncement(AnnouncementRequest announcementRequest) {
    if (announcementRequest.getMessage().isBlank() || announcementRequest.getHouseholdId().isBlank()
        || announcementRequest.getUserId().isBlank() || announcementRequest.getAuthor().isBlank()
        || announcementRequest.getMessage() == null || announcementRequest.getHouseholdId() == null
        || announcementRequest.getUserId() == null || announcementRequest.getAuthor() == null) {
      throw new AnnouncementInvalidBodyException("Invalid request body");
    }

    try {
      HouseholdResponse householdResponse = restTemplate.getForObject(
          "http://household-service/api/v1/household/" + announcementRequest.getHouseholdId(),
          HouseholdResponse.class);

      if (householdResponse == null) {
        throw new HouseholdNotFoundException("Could not find a household");
      }
    } catch (RestClientException e) {
      throw new AnnouncementInternalCommunicationException(extractErrorMessage(e));
    }

    try {
      UserResponse userResponse = restTemplate.getForObject(
          "http://user-service/api/v1/user/" + announcementRequest.getUserId(),
          UserResponse.class);

      if (userResponse == null) {
        throw new UserNotFoundException("Could not find a user");
      }
    } catch (RestClientException e) {
      throw new AnnouncementInternalCommunicationException(extractErrorMessage(e));
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

  /*
   * Retrieves an announcement by its ID
   * 
   * @param id
   * 
   * @return AnnouncementResponse
   * 
   * @throws AnnouncementInvalidParamException
   * 
   * @throws AnnouncementNotFoundException
   */
  @Override
  public AnnouncementResponse getAnnouncementById(String id) {
    if (id.isBlank() || id == null) {
      throw new AnnouncementInvalidParamException("Invalid request parameter");
    }

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

  /*
   * Retrieves all announcements by household ID
   * 
   * @param householdId
   * 
   * @return List<AnnouncementResponse>
   * 
   * @throws AnnouncementInvalidParamException
   * 
   * @throws HouseholdNotFoundException
   * 
   * @throws AnnouncementInternalCommunicationException
   */
  @Override
  public List<AnnouncementResponse> getAllAnnouncementsByHouseholdId(String householdId) {
    if (householdId.isBlank() || householdId == null) {
      throw new AnnouncementInvalidParamException("Invalid request parameter");
    }

    try {
      HouseholdResponse householdResponse = restTemplate.getForObject(
          "http://household-service/api/v1/household/" + householdId,
          HouseholdResponse.class);

      if (householdResponse == null) {
        throw new HouseholdNotFoundException("Could not find a household");
      }
    } catch (RestClientException e) {
      throw new AnnouncementInternalCommunicationException(extractErrorMessage(e));
    }

    List<Announcement> announcements = announcementRepository.findByHouseholdId(householdId);

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

  /*
   * Edits an announcement by its ID
   * 
   * @param id t
   * 
   * @param announcementRequest
   * 
   * @return AnnouncementResponse
   * 
   * @throws AnnouncementInvalidParamException
   * 
   * @throws AnnouncementInvalidBodyException
   * 
   * @throws AnnouncementNotFoundException
   */
  @Override
  public AnnouncementResponse editAnnouncement(String id, AnnouncementRequest announcementRequest) {
    if (id == null || id.isBlank()) {
      throw new AnnouncementInvalidParamException("Invalid request parameter");
    }

    if (announcementRequest.getMessage() == null || announcementRequest.getMessage().isBlank()
        || announcementRequest.getAuthor() == null || announcementRequest.getAuthor().isBlank()) {
      throw new AnnouncementInvalidBodyException("Invalid request body");
    }

    Announcement announcement = announcementRepository.findById(id)
        .orElseThrow(() -> new AnnouncementNotFoundException("Announcement not found"));

    announcement.setMessage(announcementRequest.getMessage());
    announcement.setAuthor(announcementRequest.getAuthor());
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

  /*
   * Deletes an announcement by its ID
   * 
   * @param id
   * 
   * @throws AnnouncementInvalidParamException
   * 
   * @throws AnnouncementNotFoundException
   */
  @Override
  public void deleteAnnouncement(String id) {
    if (id == null || id.isBlank()) {
      throw new AnnouncementInvalidParamException("Invalid request parameter");
    }

    if (!announcementRepository.existsById(id)) {
      throw new AnnouncementNotFoundException("Announcement not found");
    }

    announcementRepository.deleteById(id);
  }

  /*
   * Retrieves all announcements by user ID
   * 
   * @param userId
   * 
   * @return List<AnnouncementResponse>
   * 
   * @throws AnnouncementInvalidParamException
   * 
   * @throws UserNotFoundException
   * 
   * @throws AnnouncementInternalCommunicationException
   */
  @Override
  public List<AnnouncementResponse> getAllAnnouncementsByUserId(String userId) {
    if (userId.isBlank() || userId == null) {
      throw new AnnouncementInvalidParamException("Invalid request parameter");
    }

    try {
      UserResponse userResponse = restTemplate.getForObject(
          "http://user-service/api/v1/user/" + userId,
          UserResponse.class);

      if (userResponse == null) {
        throw new UserNotFoundException("Could not find a user");
      }
    } catch (RestClientException e) {
      throw new AnnouncementInternalCommunicationException(extractErrorMessage(e));
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
