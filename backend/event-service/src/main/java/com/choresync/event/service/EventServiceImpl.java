package com.choresync.event.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.choresync.event.entity.Event;
import com.choresync.event.exception.EventCreationException;
import com.choresync.event.exception.EventNotFoundException;
import com.choresync.event.model.EventRequest;
import com.choresync.event.model.EventResponse;
import com.choresync.event.repository.EventRepository;

@Service
public class EventServiceImpl implements EventService {

  @Autowired
  private EventRepository eventRepository;

  @Override
  public EventResponse createEvent(EventRequest eventRequest) {
    if (eventRequest == null) {
      throw new EventCreationException("Missing fields in request body");
    }

    Event event = Event
        .builder()
        .title(eventRequest.getTitle())
        .startTime(eventRequest.getStartTime())
        .endTime(eventRequest.getEndTime())
        .build();

    Event newEvent = eventRepository.save(event);

    EventResponse eventResponse = EventResponse
        .builder()
        .id(newEvent.getId())
        .title(newEvent.getTitle())
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
        .title(event.getTitle())
        .startTime(event.getStartTime())
        .endTime(event.getEndTime())
        .createdAt(event.getCreatedAt())
        .updatedAt(event.getUpdatedAt())
        .build();

    return eventResponse;
  }

  @Override
  public List<EventResponse> getAllEvents() {
    List<Event> events = eventRepository.findAll();

    List<EventResponse> eventResponses = new ArrayList<>();

    for (Event event : events) {
      EventResponse eventResponse = EventResponse
          .builder()
          .id(event.getId())
          .title(event.getTitle())
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
