package com.choresync.event.service;

import java.util.List;

import com.choresync.event.model.EventRequest;
import com.choresync.event.model.EventResponse;

public interface EventService {

  EventResponse createEvent(EventRequest eventRequest);

  EventResponse getEventById(String id);

  List<EventResponse> getAllEvents();

}
