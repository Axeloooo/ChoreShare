package com.choresync.event.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
import org.springframework.web.client.RestTemplate;

import com.choresync.event.entity.Event;
import com.choresync.event.exception.EventInvalidBodyException;
import com.choresync.event.exception.EventNotFoundException;
import com.choresync.event.external.response.HouseholdResponse;
import com.choresync.event.external.response.UserResponse;
import com.choresync.event.model.EventRequest;
import com.choresync.event.model.EventResponse;
import com.choresync.event.repository.EventRepository;

public class EventServiceImplTest {

  @Mock
  private EventRepository eventRepository;

  @InjectMocks
  private EventServiceImpl eventService;

  private Event event;
  private EventRequest eventRequest;
  private EventRequest invalidEventRequest;
  private EventResponse eventResponse;

  @Mock
  private RestTemplate restTemplate;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);

    when(restTemplate.getForObject(
        anyString(),
        eq(HouseholdResponse.class),
        any(Object[].class))).thenReturn(new HouseholdResponse());

    when(restTemplate.getForObject(
        anyString(),
        eq(UserResponse.class),
        any(Object[].class))).thenReturn(new UserResponse());

    event = Event.builder()
        .id("1")
        .username("username1")
        .userId("user1")
        .title("Test Event")
        .householdId("house1")
        .startTime(new Date())
        .endTime(new Date())
        .createdAt(new Date())
        .updatedAt(new Date())
        .build();

    invalidEventRequest = EventRequest.builder()
        .title("Test Event")
        .username("username1")
        .userId(null)
        .householdId("house1")
        .startTime(new Date())
        .endTime(new Date())
        .build();

    eventRequest = EventRequest.builder()
        .title("Test Event")
        .username("username1")
        .userId("user1")
        .householdId("house1")
        .startTime(new Date())
        .endTime(new Date())
        .build();

    eventResponse = EventResponse.builder()
        .id("1")
        .title("Test Event")
        .householdId("house1")
        .username("username1")
        .userId("user1")
        .startTime(event.getStartTime())
        .endTime(event.getEndTime())
        .createdAt(event.getCreatedAt())
        .updatedAt(event.getUpdatedAt())
        .build();
  }

  @Description("POST /api/v1/event - Test create event")
  @Test
  public void testCreateEvent() {
    when(eventRepository.save(any(Event.class))).thenReturn(event);

    EventResponse event = eventService.createEvent(eventRequest);

    assertNotNull(event);
    assertEquals(eventResponse, event);

    verify(eventRepository, times(1)).save(any(Event.class));
  }

  @Description("POST /api/v1/event - Test EventCreationException when creating event")
  @Test
  public void testCreateEventWithNullRequest() {
    assertThrows(EventInvalidBodyException.class, () -> eventService.createEvent(invalidEventRequest));

    verify(eventRepository, times(0)).save(any(Event.class));
  }

  @Description("GET /api/v1/event/{id} - Test get event by id")
  @Test
  public void testGetEventById() {
    when(eventRepository.findById("1")).thenReturn(Optional.ofNullable(event));

    EventResponse result = eventService.getEventById("1");

    assertNotNull(result);
    assertEquals(eventResponse, result);

    verify(eventRepository, times(1)).findById("1");
  }

  @Description("GET /api/v1/event/{id} - Test EventNotFoundException when event not found")
  @Test
  public void testGetEventByIdNotFound() {
    assertThrows(EventNotFoundException.class, () -> eventService.getEventById("1"));

    verify(eventRepository, times(1)).findById("1");
  }

  @Description("GET /api/v1/event - Test get all events")
  @Test
  public void testGetAllEvents() {
    when(eventRepository.findByHouseholdId("house1")).thenReturn(Arrays.asList(event));

    List<EventResponse> result = eventService.getAllEventsByHouseholdId("house1");

    assertNotNull(result, "The response should not be null");
    assertEquals(1, result.size(), "The response size should be 1");
    assertEquals(eventResponse, result.get(0),
        "The event response should match the expected response");

    verify(eventRepository, times(1)).findByHouseholdId("house1");
  }

  @Description("GET /api/v1/event - Test get all empty events")
  @Test
  public void testGetAllEventsWhenNoEventsFound() {
    when(eventRepository.findByHouseholdId("house1")).thenReturn(Collections.emptyList());

    List<EventResponse> result = eventService.getAllEventsByHouseholdId("house1");

    assertNotNull(result, "The response list should not be null");
    assertTrue(result.isEmpty(), "The response list should be empty");

    verify(eventRepository, times(1)).findByHouseholdId("house1");
  }
}
