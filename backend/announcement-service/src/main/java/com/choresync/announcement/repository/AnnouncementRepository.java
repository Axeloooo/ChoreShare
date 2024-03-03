package com.choresync.announcement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.choresync.announcement.entity.Announcement;

@Repository
public interface AnnouncementRepository extends JpaRepository<Announcement, String> {

}
