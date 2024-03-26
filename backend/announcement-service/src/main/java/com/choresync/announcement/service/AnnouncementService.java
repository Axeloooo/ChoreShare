package com.choresync.announcement.service;

import java.util.List;

import org.springframework.web.client.RestClientException;

import com.choresync.announcement.model.AnnouncementRequest;
import com.choresync.announcement.model.AnnouncementResponse;

public interface AnnouncementService {

  String extractErrorMessage(RestClientException e);

  AnnouncementResponse createAnnouncement(AnnouncementRequest announcementRequest);

  AnnouncementResponse getAnnouncementById(String id);

  List<AnnouncementResponse> getAllAnnouncementsByHouseholdId(String householdId);

  AnnouncementResponse editAnnouncement(String id, AnnouncementRequest announcementRequest);

  void deleteAnnouncement(String id);

  List<AnnouncementResponse> getAllAnnouncementsByUserId(String userId);
}
