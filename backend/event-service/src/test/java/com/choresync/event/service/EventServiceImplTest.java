package com.choresync.event.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

public class EventServiceImplTest {

  @Mock
  private EventRepository eventRepository;

  @InjectMocks
  private EventServiceImpl eventService;

  private Event event;
  private EventRequest eventRequest;
  private EventRequest invalidEventRequest;
  private EventResponse eventResponse;
  private UserResponse userResponse;
  private HouseholdResponse householdResponse;

  @Mock
  private RestTemplate restTemplate;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);

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

    invalidEventRequest = EventRequest
        .builder()
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

  @Description("POST /api/v1/event - Test create event with invalid body")
  @Test
  public void testCreateEventWithInvalidBody() {
    assertThrows(EventInvalidBodyException.class, () -> eventService.createEvent(invalidEventRequest));
  }

  @Description("POST /api/v1/event - Test create event with household not found")
  @Test
  public void testCreateEventWithHouseholdNotFound() {
    when(restTemplate.getForObject("http://household-service/api/v1/household/" + eventRequest.getHouseholdId(),
        HouseholdResponse.class)).thenReturn(null);

    assertThrows(HouseholdNotFoundException.class, () -> eventService.createEvent(eventRequest));
  }

  @Description("POST /api/v1/event - Test create event with user not found")
  @Test
  public void testCreateEventWithUserNotFound() {
    when(restTemplate.getForObject("http://household-service/api/v1/household/" + eventRequest.getHouseholdId(),
        HouseholdResponse.class)).thenReturn(householdResponse);
    when(restTemplate.getForObject("http://user-service/api/v1/user/" + eventRequest.getUserId(), UserResponse.class))
        .thenReturn(null);

    assertThrows(UserNotFoundException.class, () -> eventService.createEvent(eventRequest));
  }

  @Description("POST /api/v1/event - Test create event with internal communication exception")
  @Test
  public void testCreateEventWithInternalCommunicationException() {
    when(restTemplate.getForObject("http://household-service/api/v1/household/" + eventRequest.getHouseholdId(),
        HouseholdResponse.class)).thenThrow(new RestClientException("Internal error"));
    assertThrows(EventInternalCommunicationException.class, () -> eventService.createEvent(eventRequest));
  }

  @Description("POST /api/v1/event - Test create event success")
  @Test
  public void testCreateEventSuccess() {
    when(restTemplate.getForObject("http://household-service/api/v1/household/" + eventRequest.getHouseholdId(),
        HouseholdResponse.class)).thenReturn(householdResponse);
    when(restTemplate.getForObject("http://user-service/api/v1/user/" + eventRequest.getUserId(), UserResponse.class))
        .thenReturn(userResponse);
    when(eventRepository.save(any(Event.class))).thenReturn(event);

    EventResponse eventResponse = eventService.createEvent(eventRequest);

    assertNotNull(eventResponse);
    assertEquals("1", eventResponse.getId());

    verify(restTemplate, times(1)).getForObject("http://household-service/api/v1/household/house1",
        HouseholdResponse.class);
    verify(restTemplate, times(1)).getForObject("http://user-service/api/v1/user/user1", UserResponse.class);
    verify(eventRepository, times(1)).save(any(Event.class));
  }

  @Description("GET /api/v1/event/{id} - Test get event by id with invalid param")
  @Test
  public void testGetEventByIdWithInvalidParam() {
    assertThrows(EventInvalidParamException.class, () -> eventService.getEventById(null));
  }

  @Description("GET /api/v1/event/{id} - Test get event by id with event not found")
  @Test
  public void testGetEventByIdWithEventNotFound() {
    when(eventRepository.findById(anyString())).thenReturn(Optional.empty());
    assertThrows(EventNotFoundException.class, () -> eventService.getEventById("1"));
  }

  @Description("GET /api/v1/event/{id} - Test get event by id success")
  @Test
  public void testGetEventByIdSuccess() {
    when(eventRepository.findById(anyString())).thenReturn(Optional.of(event));
    EventResponse response = eventService.getEventById("1");
    assertNotNull(response);
    assertEquals(eventResponse, response);

    verify(eventRepository, times(1)).findById(anyString());
  }

  @Description("GET /api/v1/event/household/{householdId} - Test get all events by household id with invalid param")
  @Test
  public void testGetAllEventsByHouseholdIdWithInvalidParam() {
    assertThrows(EventInvalidParamException.class, () -> eventService.getAllEventsByHouseholdId(null));
  }

  @Description("GET /api/v1/event/household/{householdId} - Test get all events by household id with household not found")
  @Test
  public void testGetAllEventsByHouseholdIdWithHouseholdNotFound() {
    when(restTemplate.getForObject("http://household-service/api/v1/household/" + householdResponse.getId(),
        HouseholdResponse.class)).thenReturn(null);

    assertThrows(HouseholdNotFoundException.class,
        () -> eventService.getAllEventsByHouseholdId(householdResponse.getId()));
  }

  @Description("GET /api/v1/event/household/{householdId} - Test get all events by household id with internal communication exception")
  @Test
  public void testGetAllEventsByHouseholdIdWithInternalCommunicationException() {
    when(restTemplate.getForObject("http://household-service/api/v1/household/" + householdResponse.getId(),
        HouseholdResponse.class)).thenThrow(new RestClientException("Internal error"));

    assertThrows(EventInternalCommunicationException.class,
        () -> eventService.getAllEventsByHouseholdId(householdResponse.getId()));
  }

  @Description("GET /api/v1/event/household/{householdId} - Test get all events by household id success")
  @Test
  public void testGetAllEventsByHouseholdIdSuccess() {
    List<Event> events = Collections.singletonList(event);

    when(restTemplate.getForObject("http://household-service/api/v1/household/" + householdResponse.getId(),
        HouseholdResponse.class)).thenReturn(householdResponse);
    when(eventRepository.findByHouseholdId(householdResponse.getId())).thenReturn(events);

    List<EventResponse> responses = eventService.getAllEventsByHouseholdId(householdResponse.getId());

    assertNotNull(responses);
    assertEquals(1, responses.size());
    assertEquals(eventResponse, responses.get(0));

    verify(restTemplate, times(1)).getForObject(
        "http://household-service/api/v1/household/" + householdResponse.getId(),
        HouseholdResponse.class);
    verify(eventRepository, times(1)).findByHouseholdId(householdResponse.getId());
  }

  @Description("DELETE /api/v1/event/{id} - Test delete event with invalid param")
  @Test
  public void testDeleteEventWithInvalidParam() {
    assertThrows(EventInvalidParamException.class, () -> eventService.deleteEvent(null));
  }

  @Description("DELETE /api/v1/event/{id} - Test delete event with event not found")
  @Test
  public void testDeleteEventWithEventNotFound() {
    when(eventRepository.existsById(anyString())).thenReturn(false);

    assertThrows(EventNotFoundException.class, () -> eventService.deleteEvent("1"));
  }

  @Description("DELETE /api/v1/event/{id} - Test delete event success")
  @Test
  public void testDeleteEventSuccess() {
    when(eventRepository.existsById(anyString())).thenReturn(true);
    doNothing().when(eventRepository).deleteById(anyString());
    eventService.deleteEvent("1");

    verify(eventRepository, times(1)).existsById("1");
    verify(eventRepository, times(1)).deleteById("1");
  }

  @Description("GET /api/v1/event/user/{userId} - Test get all events by user id with invalid param")
  @Test
  public void testGetAllEventsByUserIdWithInvalidParam() {
    assertThrows(EventInvalidParamException.class, () -> eventService.getAllEventsByUserId(null));
  }

  @Description("GET /api/v1/event/user/{userId} - Test get all events by user id with user not found")
  @Test
  public void testGetAllEventsByUserIdWithUserNotFound() {
    when(restTemplate.getForObject("http://user-service/api/v1/user/" + userResponse.getId(), UserResponse.class))
        .thenReturn(null);

    assertThrows(UserNotFoundException.class, () -> eventService.getAllEventsByUserId(userResponse.getId()));
  }

  @Description("GET /api/v1/event/user/{userId} - Test get all events by user id with internal communication exception")
  @Test
  public void testGetAllEventsByUserIdWithInternalCommunicationException() {
    when(restTemplate.getForObject("http://user-service/api/v1/user/" + userResponse.getId(), UserResponse.class))
        .thenThrow(new RestClientException("Internal error"));

    assertThrows(EventInternalCommunicationException.class,
        () -> eventService.getAllEventsByUserId(userResponse.getId()));
  }

  @Description("GET /api/v1/event/user/{userId} - Test get all events by user id success")
  @Test
  public void testGetAllEventsByUserIdSuccess() {
    List<Event> events = Collections.singletonList(event);

    when(restTemplate.getForObject("http://user-service/api/v1/user/" + userResponse.getId(), UserResponse.class))
        .thenReturn(userResponse);
    when(eventRepository.findByUserId(userResponse.getId())).thenReturn(events);

    List<EventResponse> responses = eventService.getAllEventsByUserId(userResponse.getId());

    assertNotNull(responses);
    assertEquals(1, responses.size());
    assertEquals(eventResponse, responses.get(0));

    verify(restTemplate, times(1)).getForObject("http://user-service/api/v1/user/" + userResponse.getId(),
        UserResponse.class);
    verify(eventRepository, times(1)).findByUserId(userResponse.getId());
  }
}
