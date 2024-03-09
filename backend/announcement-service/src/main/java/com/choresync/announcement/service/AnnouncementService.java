package com.choresync.announcement.service;

import java.util.List;

import com.choresync.announcement.model.AnnouncementRequest;
import com.choresync.announcement.model.AnnouncementResponse;

public interface AnnouncementService {

  AnnouncementResponse createAnnouncement(AnnouncementRequest announcementRequest);

  AnnouncementResponse getAnnouncementById(String id);

  List<AnnouncementResponse> getAllAnnouncements();

  AnnouncementResponse editAnnouncement(String id, AnnouncementRequest announcementRequest);

  void deleteAnnouncement(String id);

  List<AnnouncementResponse> getAllAnnouncementsByUserId(String userId);
}
