package com.moveinsync.intelligence.dto;

import java.util.List;

public record SlaShieldResponse(
        String vendorName,
        String month,
        double rawOtaPercentage,
        double adjustedSlaPercentage,
        double targetSlaPercentage,
        double savedPenaltyAmount,
        long totalTrips,
        long totalDelayedTrips,
        long vendorFaultDelays,
        long employeeFaultDelays,
        long trafficWeatherDelays,
        String sarvamAiAttributionAnalysis,
        List<TripAttributionDetail> sampleAnalyzedTrips
) {
    public record TripAttributionDetail(
            String tripId,
            String employeeId,
            String pickupTimeScheduled,
            String pickupTimeActual,
            String employeeBoardedTime,
            int totalDelayMinutes,
            String delayCategory, // "VENDOR_FAULT", "EMPLOYEE_FAULT", "TRAFFIC_FAULT"
            String attributionReason,
            boolean penaltyExempt
    ) {}
}
