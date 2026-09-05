package com.moveinsync.intelligence.dto;

import java.util.List;

public record EmployeePortalResponse(
        EmployeeProfile profile,
        LiveTripDetail liveTrip,
        List<TripActivityRecord> tripHistory,
        List<EmployeeSafetyAlert> safetyAlerts,
        List<EmployeeFeedbackRecord> feedbackHistory,
        EmployeeCommuteStats commuteStats
) {
    public record EmployeeProfile(
            String stwid,
            String employeeName,
            String role,
            String companyName,
            String office,
            String gender,
            String managerId,
            String managerName,
            Double onTimeBoardingRate,
            Integer totalTripsTaken,
            Double safetyScore
    ) {}

    public record LiveTripDetail(
            String tripId,
            String tripDate,
            String shiftType,
            String tripType,
            String routeId,
            String cabReg,
            String driverName,
            String driverPhone,
            String pickupLocation,
            String scheduledPickupTime,
            Integer etaMinutes,
            String boardingOtp,
            String status,
            Boolean escortAssigned,
            String escortName,
            Boolean hasRideToday
    ) {}

    public record TripActivityRecord(
            String tripId,
            String tripDate,
            String dayOfWeek,
            String shiftType,
            String tripType,
            String routeId,
            String cabReg,
            String plannedPickup,
            String actualPickup,
            Double plannedKm,
            Double traveledKm,
            String boardingStatus,
            Integer delayMinutes,
            String noShowReason,
            Boolean isNoShow
    ) {}

    public record EmployeeSafetyAlert(
            String alertId,
            String eventType,
            String severity,
            String timestamp,
            String cabReg,
            String stateText,
            String resolutionDetails
    ) {}

    public record EmployeeFeedbackRecord(
            String tripId,
            String tripDate,
            Double overallRating,
            Integer driverRating,
            Integer cabRating,
            Integer routeRating,
            Integer safetyRating,
            Integer marshalRating,
            String comments
    ) {}

    public record EmployeeCommuteStats(
            Integer totalTrips,
            Double onTimePercentage,
            Double totalKmTraveled,
            Integer avgPickupDelayMins,
            Double avgCsatGiven
    ) {}

    public record FeedbackSubmitRequest(
            String stwid,
            String tripId,
            Integer driverRating,
            Integer cabRating,
            Integer routeRating,
            Integer safetyRating,
            Integer marshalRating,
            String comments
    ) {}

    public record SosTriggerRequest(
            String stwid,
            String tripId,
            String eventType,
            String userNote,
            String location
    ) {}

    public record ActionResponse(
            Boolean success,
            String message,
            String incidentId,
            String timestamp
    ) {}
}
