package com.choresync.household.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.choresync.household.entity.Household;

@Repository
public interface HouseholdRepository extends JpaRepository<Household, String> {
}
