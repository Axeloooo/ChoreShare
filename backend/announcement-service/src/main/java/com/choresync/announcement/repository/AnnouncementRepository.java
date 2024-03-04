package com.choresync.announcement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.choresync.announcement.entity.Announcement;

@Repository
public interface AnnouncementRepository extends JpaRepository<Announcement, String> {
  List<Announcement> findByUserId(String userId);
}
