package com.choresync.announcement.service;

import com.choresync.announcement.model.AnnouncementRequest;
import com.choresync.announcement.model.AnnouncementResponse;

public interface AnnouncementService {

  AnnouncementResponse createAnnouncement(AnnouncementRequest announcementRequest);

  AnnouncementResponse getAnnouncementById(String id);

  AnnouncementResponse editAnnouncement(String id, AnnouncementRequest announcementRequest);

  void deleteAnnouncement(String id);
}
