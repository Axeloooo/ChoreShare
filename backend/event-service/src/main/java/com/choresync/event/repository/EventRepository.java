package com.choresync.event.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.choresync.event.entity.Event;

@Repository
public interface EventRepository extends JpaRepository<Event, String> {

}
