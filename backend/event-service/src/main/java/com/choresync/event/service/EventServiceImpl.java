package com.choresync.event.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.choresync.event.entity.Event;
import com.choresync.event.model.EventRequest;
import com.choresync.event.model.EventResponse;
import com.choresync.event.repository.EventRepository;

@Service
public class EventServiceImpl implements EventService {

  @Autowired
  private EventRepository eventRepository;

  @Override
  public String createEvent(EventRequest eventRequest) {
    Event event = Event
        .builder()
        .title(eventRequest.getTitle())
        .startTime(eventRequest.getStartTime())
        .endTime(eventRequest.getEndTime())
        .build();

    Event newEvent = eventRepository.save(event);

    return newEvent.getId();
  }

  @Override
  public EventResponse getEventById(String id) {
    Event event = eventRepository.findById(id).orElse(null);

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
