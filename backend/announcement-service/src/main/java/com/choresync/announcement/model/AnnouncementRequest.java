package com.choresync.announcement.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnnouncementRequest {
  private String message;

  private String author;

  private String householdId;

  private String userId;
}
