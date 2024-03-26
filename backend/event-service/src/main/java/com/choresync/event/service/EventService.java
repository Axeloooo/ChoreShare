package com.choresync.event.service;

import java.util.List;

import org.springframework.web.client.RestClientException;

import com.choresync.event.model.EventRequest;
import com.choresync.event.model.EventResponse;

public interface EventService {
  String extractErrorMessage(RestClientException e);

  EventResponse createEvent(EventRequest eventRequest);

  EventResponse getEventById(String id);

  List<EventResponse> getAllEventsByHouseholdId(String householdId);

  List<EventResponse> getAllEventsByUserId(String userId);

  void deleteEvent(String id);
}
