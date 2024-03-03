package com.choresync.event.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.choresync.event.entity.Event;
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

  @Test
  public void testCreateEvent() {
    when(eventRepository.save(any(Event.class))).thenReturn(event);

    String eventId = eventService.createEvent(eventRequest);

    assertNotNull(eventId);
    assertEquals(event.getId(), eventId);
  }

  @Test
  public void testGetEventById() {
    when(eventRepository.findById("1")).thenReturn(java.util.Optional.ofNullable(event));

    EventResponse result = eventService.getEventById("1");

    assertNotNull(result);
    assertEquals(eventResponse, result);
  }

  @Test
  public void testGetAllEvents() {
    when(eventRepository.findAll()).thenReturn(Arrays.asList(event));

    List<EventResponse> result = eventService.getAllEvents();

    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals(eventResponse, result.get(0));
  }
}
