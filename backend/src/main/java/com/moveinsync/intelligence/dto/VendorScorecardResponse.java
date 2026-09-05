package com.moveinsync.intelligence.dto;

import java.util.List;

public record VendorScorecardResponse(
        String vendorName,
        double onTimeArrivalPercentage,
        double slaTargetPercentage,
        double complianceRatePercentage,
        double billingAccuracyPercentage,
        double averageDriverRating,
        long totalTrips,
        long delayedTrips,
        long nonCompliantTrips,
        long billingDiscrepanciesCount,
        List<VendorSlaDetail> slaBreakdown,
        List<BillingAuditDetail> billingDiscrepancies,
        List<ComplianceAlertDetail> complianceAlerts
) {
    public record VendorSlaDetail(
            String month,
            String vendorName,
            double actualOta,
            double targetSla,
            long totalTrips,
            long vendorFaultDelays,
            long employeeFaultDelays,
            long trafficWeatherDelays
    ) {}

    public record BillingAuditDetail(
            String tripId,
            String vendorName,
            double billedKm,
            double gpsActualKm,
            double billedCost,
            double auditExpectedCost,
            String discrepancyReason,
            String status
    ) {}

    public record ComplianceAlertDetail(
            String alertId,
            String vendorName,
            String vehicleReg,
            String driverName,
            String alertType,
            String severity,
            String timestamp
    ) {}
}
