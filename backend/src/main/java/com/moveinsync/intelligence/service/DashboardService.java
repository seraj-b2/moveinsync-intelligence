package com.moveinsync.intelligence.service;

import com.moveinsync.intelligence.dto.DashboardSummaryResponse;
import com.moveinsync.intelligence.entity.Trip;
import com.moveinsync.intelligence.entity.TripStatus;
import com.moveinsync.intelligence.repository.TripRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class DashboardService {

    private final TripRepository tripRepository;

    public DashboardService(TripRepository tripRepository) {
        this.tripRepository = tripRepository;
    }

    public DashboardSummaryResponse getSummary() {
        List<Trip> trips = tripRepository.findAll();

        long totalTrips = trips.size();

        Set<String> activeVehicleIds = new HashSet<>();

        for (Trip trip : trips) {
            if (isActiveTrip(trip.getStatus())) {
                activeVehicleIds.add(trip.getVehicleId());
            }
        }

        long activeVehicles = activeVehicleIds.size();

        long delayedTrips = trips.stream()
            .filter(trip -> trip.getStatus() == TripStatus.DELAYED)
            .count();

        long completedTrips = trips.stream()
            .filter(trip -> trip.getStatus() == TripStatus.COMPLETED)
            .count();

        long employeesTransported = trips.stream()
            .filter(trip ->
                trip.getStatus() == TripStatus.COMPLETED
                    || trip.getStatus() == TripStatus.IN_PROGRESS
                    || trip.getStatus() == TripStatus.RESUMED
            )
            .count();

        double onTimePercentage = totalTrips == 0
            ? 0
            : ((double) completedTrips / totalTrips) * 100;

        double routeUtilization = totalTrips == 0
            ? 0
            : ((double) activeVehicles / totalTrips) * 100;

        return new DashboardSummaryResponse(
            totalTrips,
            activeVehicles,
            round(onTimePercentage),
            delayedTrips,
            employeesTransported,
            round(routeUtilization)
        );
    }

    private boolean isActiveTrip(TripStatus status) {
        return status == TripStatus.IN_PROGRESS
            || status == TripStatus.DELAYED
            || status == TripStatus.REROUTED
            || status == TripStatus.CAR_BREAKDOWN
            || status == TripStatus.MEDICAL_ASSISTANCE
            || status == TripStatus.RESUMED;
    }

    private double round(double value) {
        return Math.round(value * 10.0) / 10.0;
    }
}