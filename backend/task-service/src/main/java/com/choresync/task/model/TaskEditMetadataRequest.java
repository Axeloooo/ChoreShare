package com.choresync.task.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskEditMetadataRequest {
  private String title;

  private String description;

  private String frequency;

  private String tag;
}
