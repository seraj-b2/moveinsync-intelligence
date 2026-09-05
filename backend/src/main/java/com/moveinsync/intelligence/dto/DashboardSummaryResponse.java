package com.moveinsync.intelligence.dto;

public record DashboardSummaryResponse(
        long totalTrips,
        long activeVehicles,
        double onTimePercentage,
        long delayedTrips,
        long employeesTransported,
        double routeUtilization
) {
}