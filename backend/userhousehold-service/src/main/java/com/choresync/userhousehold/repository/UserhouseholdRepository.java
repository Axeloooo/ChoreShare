package com.choresync.userhousehold.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.choresync.userhousehold.entity.Userhousehold;

@Repository
public interface UserhouseholdRepository extends JpaRepository<Userhousehold, String> {

}
