package com.moveinsync.intelligence.config;

import com.moveinsync.intelligence.entity.IncidentType;
import com.moveinsync.intelligence.entity.Trip;
import com.moveinsync.intelligence.entity.TripStatus;
import com.moveinsync.intelligence.repository.TripRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner loadSampleTrips(TripRepository tripRepository) {
        return args -> {
            if (tripRepository.count() > 0) {
                return;
            }

            LocalDate today = LocalDate.now();

            Trip trip1 = createTrip(
                "EMP-1001",
                "CAB-101",
                "ROUTE-A",
                today,
                TripStatus.COMPLETED,
                IncidentType.NONE
            );

            Trip trip2 = createTrip(
                "EMP-1002",
                "CAB-102",
                "ROUTE-B",
                today,
                TripStatus.IN_PROGRESS,
                IncidentType.NONE
            );

            Trip trip3 = createTrip(
                "EMP-1003",
                "CAB-103",
                "ROUTE-C",
                today,
                TripStatus.DELAYED,
                IncidentType.TRAFFIC
            );

            Trip trip4 = createTrip(
                "EMP-1004",
                "CAB-104",
                "ROUTE-D",
                today,
                TripStatus.REROUTED,
                IncidentType.ROAD_CLOSURE
            );

            Trip trip5 = createTrip(
                "EMP-1005",
                "CAB-105",
                "ROUTE-E",
                today,
                TripStatus.CAR_BREAKDOWN,
                IncidentType.VEHICLE_BREAKDOWN
            );

            Trip trip6 = createTrip(
                "EMP-1006",
                "CAB-106",
                "ROUTE-F",
                today,
                TripStatus.MEDICAL_ASSISTANCE,
                IncidentType.MEDICAL_EMERGENCY
            );

            Trip trip7 = createTrip(
                "EMP-1007",
                "CAB-107",
                "ROUTE-G",
                today,
                TripStatus.RESUMED,
                IncidentType.MEDICAL_EMERGENCY
            );

            Trip trip8 = createTrip(
                "EMP-1008",
                "CAB-108",
                "ROUTE-H",
                today,
                TripStatus.SCHEDULED,
                IncidentType.NONE
            );

            tripRepository.saveAll(List.of(
                trip1,
                trip2,
                trip3,
                trip4,
                trip5,
                trip6,
                trip7,
                trip8
            ));

            System.out.println("Sample trip data inserted successfully.");
        };
    }

    private Trip createTrip(
        String employeeId,
        String vehicleId,
        String routeId,
        LocalDate tripDate,
        TripStatus status,
        IncidentType incidentType
    ) {
        Trip trip = new Trip();

        trip.setEmployeeId(employeeId);
        trip.setVehicleId(vehicleId);
        trip.setRouteId(routeId);
        trip.setTripDate(tripDate);
        trip.setScheduledTime(LocalTime.of(8, 30));
        trip.setActualTime(LocalTime.of(8, 40));
        trip.setStatus(status);
        trip.setIncidentType(incidentType);

        if (incidentType != IncidentType.NONE) {
            trip.setIncidentDescription(
                incidentType == IncidentType.MEDICAL_EMERGENCY
                    ? "Employee required medical assistance during the trip"
                    : "Trip affected by an operational incident"
            );

            trip.setIncidentLocation("Near Hyderabad");
            trip.setIncidentStartedAt(LocalDateTime.now().minusMinutes(30));

            if (status == TripStatus.RESUMED || status == TripStatus.COMPLETED) {
                trip.setIncidentResolvedAt(LocalDateTime.now().minusMinutes(5));
            }
        }

        return trip;
    }
}