package com.choresync.event.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.annotation.Description;

import com.choresync.event.entity.Event;
import com.choresync.event.exception.EventCreationException;
import com.choresync.event.exception.EventNotFoundException;
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
  private EventResponse eventResponse;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);

    event = Event.builder()
        .id("1")
        .title("Test Event")
        .startTime(new Date())
        .endTime(new Date())
        .createdAt(new Date())
        .updatedAt(new Date())
        .build();

    eventRequest = EventRequest.builder()
        .title("Test Event")
        .startTime(new Date())
        .endTime(new Date())
        .build();

    eventResponse = EventResponse.builder()
        .id("1")
        .title("Test Event")
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
    assertThrows(EventCreationException.class, () -> eventService.createEvent(null));

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

  // @Description("GET /api/v1/event - Test get all events")
  // @Test
  // public void testGetAllEvents() {
  // when(eventRepository.findAll()).thenReturn(Arrays.asList(event));

  // List<EventResponse> result = eventService.getAllEvents();

  // assertNotNull(result);
  // assertEquals(1, result.size());
  // assertEquals(eventResponse, result.get(0));

  // verify(eventRepository, times(1)).findAll();
  // }

  // @Description("GET /api/v1/event - Test get all empty events")
  // @Test
  // public void testGetAllEventsWhenNoEventsFound() {
  // when(eventRepository.findAll()).thenReturn(Arrays.asList());

  // List<EventResponse> result = eventService.getAllEvents();

  // assertNotNull(result);
  // assertEquals(0, result.size());

  // verify(eventRepository, times(1)).findAll();
  // }
}
