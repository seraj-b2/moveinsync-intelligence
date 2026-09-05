package com.moveinsync.intelligence.service;

import com.moveinsync.intelligence.dto.VendorScorecardResponse;
import com.moveinsync.intelligence.dto.VendorScorecardResponse.BillingAuditDetail;
import com.moveinsync.intelligence.dto.VendorScorecardResponse.ComplianceAlertDetail;
import com.moveinsync.intelligence.dto.VendorScorecardResponse.VendorSlaDetail;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VendorService {

    public List<VendorScorecardResponse> getVendorScorecards() {
        VendorSlaDetail rMikhailovSla = new VendorSlaDetail(
                "July 2026", "Rohan Mikhailov Travel", 78.4, 90.0, 42150, 4810, 2340, 1980
        );

        VendorSlaDetail mPavlovSla = new VendorSlaDetail(
                "July 2026", "Meera Pavlov Travel", 92.1, 90.0, 38400, 1420, 1200, 410
        );

        VendorSlaDetail sMikhailovSla = new VendorSlaDetail(
                "July 2026", "Sanjay Mikhailov Travel", 84.7, 90.0, 31200, 2890, 1150, 720
        );

        BillingAuditDetail audit1 = new BillingAuditDetail(
                "1097076", "Rohan Mikhailov Travel", 45.2, 31.8, 1420.00, 1000.00,
                "Billed distance exceeds GPS tracked distance by 42.1%", "FLAGGED"
        );

        BillingAuditDetail audit2 = new BillingAuditDetail(
                "1123974", "Rohan Mikhailov Travel", 28.0, 27.5, 850.00, 850.00,
                "Billing matched GPS logs within SLA tolerance", "VERIFIED"
        );

        BillingAuditDetail audit3 = new BillingAuditDetail(
                "1098442", "Meera Pavlov Travel", 52.0, 51.5, 1650.00, 1650.00,
                "Verified rate slab & kilometer log", "VERIFIED"
        );

        ComplianceAlertDetail comp1 = new ComplianceAlertDetail(
                "ALT-9042", "Rohan Mikhailov Travel", "KA-01-MJ-4821", "Rajesh Kumar",
                "DRIVER_NON_COMPLIANT", "Sev-1", "July 12, 2026 08:15 AM"
        );

        ComplianceAlertDetail comp2 = new ComplianceAlertDetail(
                "ALT-8812", "Rohan Mikhailov Travel", "KA-05-MB-1102", "Suresh Naik",
                "OVERSPEEDING", "Sev-2", "July 14, 2026 10:30 PM"
        );

        ComplianceAlertDetail comp3 = new ComplianceAlertDetail(
                "ALT-7741", "Sanjay Mikhailov Travel", "KA-03-MK-9910", "Amit Patel",
                "VEHICLE_PERMIT_EXPIRED", "Sev-1", "July 15, 2026 06:45 AM"
        );

        VendorScorecardResponse rohanMikhailov = new VendorScorecardResponse(
                "Rohan Mikhailov Travel",
                78.4, 90.0, 88.5, 82.3, 3.8,
                42150, 9130, 484, 124,
                List.of(rMikhailovSla),
                List.of(audit1, audit2),
                List.of(comp1, comp2)
        );

        VendorScorecardResponse meeraPavlov = new VendorScorecardResponse(
                "Meera Pavlov Travel",
                92.1, 90.0, 97.2, 98.6, 4.7,
                38400, 3030, 107, 12,
                List.of(mPavlovSla),
                List.of(audit3),
                List.of()
        );

        VendorScorecardResponse sanjayMikhailov = new VendorScorecardResponse(
                "Sanjay Mikhailov Travel",
                84.7, 90.0, 91.4, 93.1, 4.2,
                31200, 4760, 268, 38,
                List.of(sMikhailovSla),
                List.of(),
                List.of(comp3)
        );

        return List.of(rohanMikhailov, meeraPavlov, sanjayMikhailov);
    }
}
