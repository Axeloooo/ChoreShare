package com.choresync.event.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.choresync.event.entity.Event;
import com.choresync.event.exception.EventInternalCommunicationException;
import com.choresync.event.exception.EventInvalidBodyException;
import com.choresync.event.exception.EventInvalidParamException;
import com.choresync.event.exception.EventNotFoundException;
import com.choresync.event.external.exception.HouseholdNotFoundException;
import com.choresync.event.external.exception.UserNotFoundException;
import com.choresync.event.external.response.HouseholdResponse;
import com.choresync.event.external.response.UserResponse;
import com.choresync.event.model.EventRequest;
import com.choresync.event.model.EventResponse;
import com.choresync.event.repository.EventRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class EventServiceImpl implements EventService {

  @Autowired
  private EventRepository eventRepository;

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
   * Creates a new event.
   * 
   * @param eventRequest
   * 
   * @return EventResponse
   * 
   * @throws EventInvalidBodyException
   * 
   * @throws HouseholdNotFoundException
   * 
   * @throws UserNotFoundException
   * 
   * @throws EventInternalCommunicationException
   */
  @Override
  public EventResponse createEvent(EventRequest eventRequest) {
    if (eventRequest.getTitle().isBlank() || eventRequest.getUserId().isBlank()
        || eventRequest.getUserId() == null || eventRequest.getUsername().isBlank()
        || eventRequest.getUsername() == null
        || eventRequest.getStartTime() == null
        || eventRequest.getHouseholdId().isBlank()
        || eventRequest.getHouseholdId() == null) {
      throw new EventInvalidBodyException("Invalid request body");
    }

    try {
      HouseholdResponse householdResponse = restTemplate.getForObject(
          "http://household-service/api/v1/household/" + eventRequest.getHouseholdId(),
          HouseholdResponse.class);

      if (householdResponse == null) {
        throw new HouseholdNotFoundException("Could not find a household");
      }
    } catch (RestClientException e) {
      throw new EventInternalCommunicationException(extractErrorMessage(e));
    }

    try {
      UserResponse userResponse = restTemplate.getForObject(
          "http://user-service/api/v1/user/" + eventRequest.getUserId(),
          UserResponse.class);

      if (userResponse == null) {
        throw new UserNotFoundException("Could not find a user");
      }
    } catch (RestClientException e) {
      throw new EventInternalCommunicationException(extractErrorMessage(e));
    }

    Event event = Event
        .builder()
        .householdId(eventRequest.getHouseholdId())
        .title(eventRequest.getTitle())
        .userId(eventRequest.getUserId())
        .username(eventRequest.getUsername())
        .startTime(eventRequest.getStartTime())
        .endTime(eventRequest.getEndTime())
        .build();

    Event newEvent = eventRepository.save(event);

    EventResponse eventResponse = EventResponse
        .builder()
        .id(newEvent.getId())
        .householdId(newEvent.getHouseholdId())
        .title(newEvent.getTitle())
        .userId(newEvent.getUserId())
        .username(newEvent.getUsername())
        .startTime(newEvent.getStartTime())
        .endTime(newEvent.getEndTime())
        .createdAt(newEvent.getCreatedAt())
        .updatedAt(newEvent.getUpdatedAt())
        .build();

    return eventResponse;
  }

  /*
   * Retrieves an event by its ID.
   * 
   * @param id the event ID
   * 
   * @return the event response
   * 
   * @throws EventInvalidParamException if the event ID is invalid
   * 
   * @throws EventNotFoundException if the event does not exist
   */
  @Override
  public EventResponse getEventById(String id) {
    if (id.isBlank() || id == null) {
      throw new EventInvalidParamException("Invalid request parameter");
    }

    Event event = eventRepository.findById(id).orElseThrow(() -> new EventNotFoundException("Event not found"));

    EventResponse eventResponse = EventResponse
        .builder()
        .id(event.getId())
        .householdId(event.getHouseholdId())
        .title(event.getTitle())
        .userId(event.getUserId())
        .username(event.getUsername())
        .startTime(event.getStartTime())
        .endTime(event.getEndTime())
        .createdAt(event.getCreatedAt())
        .updatedAt(event.getUpdatedAt())
        .build();

    return eventResponse;
  }

  /*
   * Retrieves all events by household ID.
   * 
   * @param householdId
   * 
   * @return List<EventResponse>
   * 
   * @throws EventInvalidParamException
   * 
   * @throws HouseholdNotFoundException
   * 
   * @throws EventInternalCommunicationException
   */
  @Override
  public List<EventResponse> getAllEventsByHouseholdId(String householdId) {
    if (householdId.isBlank() || householdId == null) {
      throw new EventInvalidParamException("Invalid request parameter");
    }

    try {
      HouseholdResponse householdResponse = restTemplate.getForObject(
          "http://household-service/api/v1/household/" + householdId,
          HouseholdResponse.class);

      if (householdResponse == null) {
        throw new HouseholdNotFoundException("Could not find a household");
      }
    } catch (RestClientException e) {
      throw new EventInternalCommunicationException(extractErrorMessage(e));
    }

    List<Event> events = eventRepository.findByHouseholdId(householdId);

    List<EventResponse> eventResponses = new ArrayList<>();

    for (Event event : events) {
      EventResponse eventResponse = EventResponse
          .builder()
          .id(event.getId())
          .householdId(event.getHouseholdId())
          .title(event.getTitle())
          .userId(event.getUserId())
          .username(event.getUsername())
          .startTime(event.getStartTime())
          .endTime(event.getEndTime())
          .createdAt(event.getCreatedAt())
          .updatedAt(event.getUpdatedAt())
          .build();
      eventResponses.add(eventResponse);
    }

    return eventResponses;
  }

  /*
   * Deletes an event by its ID.
   * 
   * @param id
   * 
   * @throws EventInvalidParamException
   * 
   * @throws EventNotFoundException
   * 
   * @throws EventInternalCommunicationException
   */
  @Override
  public void deleteEvent(String id) {
    if (id.isBlank() || id == null) {
      throw new EventInvalidParamException("Invalid request parameter");
    }

    if (!eventRepository.existsById(id)) {
      throw new EventNotFoundException("Event not found");
    }

    eventRepository.deleteById(id);
  }

  /*
   * Retrieves all events by user ID.
   * 
   * @param userId
   * 
   * @return List<EventResponse>
   * 
   * @throws EventInvalidParamException
   * 
   * @throws UserNotFoundException
   * 
   * @throws EventInternalCommunicationException
   */
  @Override
  public List<EventResponse> getAllEventsByUserId(String userId) {
    if (userId.isBlank() || userId == null) {
      throw new EventInvalidParamException("Invalid request parameter");
    }

    try {
      UserResponse userResponse = restTemplate.getForObject(
          "http://user-service/api/v1/user/" + userId,
          UserResponse.class);

      if (userResponse == null) {
        throw new UserNotFoundException("Could not find a user");
      }
    } catch (RestClientException e) {
      throw new EventInternalCommunicationException(extractErrorMessage(e));
    }

    List<Event> events = eventRepository.findByUserId(userId);

    List<EventResponse> eventResponses = new ArrayList<>();

    for (Event event : events) {
      EventResponse eventResponse = EventResponse
          .builder()
          .id(event.getId())
          .householdId(event.getHouseholdId())
          .title(event.getTitle())
          .userId(event.getUserId())
          .username(event.getUsername())
          .startTime(event.getStartTime())
          .endTime(event.getEndTime())
          .createdAt(event.getCreatedAt())
          .updatedAt(event.getUpdatedAt())
          .build();
      eventResponses.add(eventResponse);
    }

    return eventResponses;
  }
}
