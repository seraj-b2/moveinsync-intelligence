package com.moveinsync.intelligence.dto;

import java.util.List;

public record ManagerDashboardResponse(
        String managerId,
        String managerName,
        String title,
        String department,
        String companyName,
        String office,
        int teamSize,
        double teamReadinessIndex,
        double averageCsat,
        List<ManagerShiftSummary> shifts
) {
    public record ManagerShiftSummary(
            String shiftId,
            String shiftName,
            String timeWindow,
            long totalDirectReports,
            long boardedCount,
            long noShowCount,
            double noShowRatePercentage,
            double onTimeBoardingPercentage,
            double shiftReadinessIndex,
            List<TeamMemberRosterDetail> roster,
            List<TeamSafetyAlertDetail> safetyAlerts,
            TeamCsatDetail csatBreakdown
    ) {}

    public record TeamMemberRosterDetail(
            String stwid,
            String employeeName,
            String role,
            String managerId,
            String office,
            String shiftTime,
            String routeId,
            String cabReg,
            String pickupStatus,
            long delayMinutes,
            double plannedKm,
            double traveledKm,
            boolean isNoShow,
            String noShowReason,
            String gender
    ) {}

    public record TeamSafetyAlertDetail(
            String alertId,
            String eventType,
            String severity,
            String stwid,
            String employeeName,
            String cabReg,
            boolean escortAssigned,
            String stateText,
            String timestamp,
            String actionRequired
    ) {}

    public record TeamCsatDetail(
            double overallCsat,
            double driverRating,
            double cabRating,
            double routeRating,
            double safetyRating,
            double marshalRating,
            long totalFeedbackCount
    ) {}

    public record ManagerProfile(
            String managerId,
            String managerName,
            String title,
            String department,
            String companyName,
            int teamSize,
            String office
    ) {}
}
