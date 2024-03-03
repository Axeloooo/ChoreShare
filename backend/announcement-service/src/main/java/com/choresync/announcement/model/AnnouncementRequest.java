package com.choresync.announcement.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnnouncementRequest {

  private String message;

  private String userId;
}
