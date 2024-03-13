package com.choresync.event.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.choresync.event.entity.Event;
import com.choresync.event.exception.EventCreationException;
import com.choresync.event.exception.EventNotFoundException;
import com.choresync.event.exception.HouseholdNotFoundException;
import com.choresync.event.external.response.HouseholdResponse;
import com.choresync.event.model.EventRequest;
import com.choresync.event.model.EventResponse;
import com.choresync.event.repository.EventRepository;

@Service
public class EventServiceImpl implements EventService {

  @Autowired
  private EventRepository eventRepository;

  @Autowired
  private RestTemplate restTemplate;

  @Override
  public EventResponse createEvent(EventRequest eventRequest) {
    if (eventRequest == null) {
      throw new EventCreationException("Missing fields in request body");
    }

    HouseholdResponse householdResponse;

    try {
      householdResponse = restTemplate.getForObject(
          "http://household-service/api/v1/household/" + eventRequest.getHouseholdId(),
          HouseholdResponse.class);

    } catch (RestClientException e) {
      throw new HouseholdNotFoundException(
          "Could not find a household. " + e.getMessage());
    }

    if (householdResponse == null) {
      throw new HouseholdNotFoundException("Could not find a household");
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

  @Override
  public EventResponse getEventById(String id) {
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

  @Override
  public List<EventResponse> getAllEventsByHouseholdId(String householdId) {
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

  @Override
  public void deleteEvent(String id) {
    if (!eventRepository.existsById(id)) {
      throw new EventNotFoundException("Event not found");
    }
    eventRepository.deleteById(id);
  }
}
