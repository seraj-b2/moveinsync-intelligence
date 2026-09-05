package com.moveinsync.intelligence.repository;

import com.moveinsync.intelligence.entity.Trip;
import com.moveinsync.intelligence.entity.TripStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TripRepository extends JpaRepository<Trip, Long> {

    long countByStatus(TripStatus status);

    List<Trip> findByStatus(TripStatus status);

    long countByTripDate(java.time.LocalDate tripDate);
}