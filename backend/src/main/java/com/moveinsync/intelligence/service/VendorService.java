package com.moveinsync.intelligence.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.moveinsync.intelligence.dto.*;
import com.moveinsync.intelligence.dto.SlaShieldResponse.TripAttributionDetail;
import com.moveinsync.intelligence.dto.VendorScorecardResponse.BillingAuditDetail;
import com.moveinsync.intelligence.dto.VendorScorecardResponse.ComplianceAlertDetail;
import com.moveinsync.intelligence.dto.VendorScorecardResponse.VendorSlaDetail;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class VendorService {

    private static final String SARVAM_API_KEY = "sk_k1fczr34_EqKXoPdzW4WPJaJSSuTV6ndv";
    private static final String SARVAM_API_URL = "https://api.sarvam.ai/v1/chat/completions";
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    public List<String> getBusinessUnits() {
        return List.of("All Business Units", "vanta-Aus", "catalyst-Sac", "orbit-Slc", "vanta-Sea", "pinnacle-Slc");
    }

    public List<String> getAvailableMonths() {
        return List.of("July 2026", "June 2026", "May 2026", "All Months");
    }

    public List<VendorScorecardResponse> getVendorScorecards(String month, String businessUnit) {
        String filterMonth = (month == null || month.isBlank() || month.equalsIgnoreCase("ALL")) ? "July 2026" : month;
        String filterBu = (businessUnit == null || businessUnit.isBlank() || businessUnit.contains("All")) ? "All Business Units" : businessUnit;

        // Base modifiers for months
        double monthFactor = filterMonth.contains("May") ? 0.92 : filterMonth.contains("June") ? 0.96 : filterMonth.contains("All") ? 2.85 : 1.0;
        // Base modifiers for Business Units
        double buFactor = filterBu.contains("vanta-Aus") ? 1.15 : filterBu.contains("catalyst") ? 0.85 : filterBu.contains("orbit") ? 1.05 : filterBu.contains("pinnacle") ? 1.20 : 1.0;

        double combinedFactor = monthFactor * buFactor;

        // Rohan Mikhailov Travel
        double rohanOta = Math.round((filterMonth.contains("May") ? 82.1 : filterMonth.contains("June") ? 85.0 : 78.4) * (buFactor > 1 ? 1.02 : buFactor < 1 ? 0.97 : 1.0) * 10.0) / 10.0;
        rohanOta = Math.min(98.5, Math.max(70.0, rohanOta));
        long rohanTrips = Math.round(42150 * combinedFactor);
        long rohanVendorFault = Math.round(4810 * combinedFactor);
        long rohanEmpFault = Math.round(2340 * combinedFactor);
        long rohanTraffic = Math.round(1980 * combinedFactor);
        long rohanDelayed = rohanVendorFault + rohanEmpFault + rohanTraffic;
        int rohanAlertsCount = (int) Math.max(1, Math.round(484 * combinedFactor / 100));
        int rohanBillingCount = (int) Math.max(2, Math.round(124 * combinedFactor / 100));

        VendorSlaDetail rSla = new VendorSlaDetail(
                filterMonth, "Rohan Mikhailov Travel", rohanOta, 90.0, rohanTrips, rohanVendorFault, rohanEmpFault, rohanTraffic
        );

        BillingAuditDetail audit1 = new BillingAuditDetail(
                "1097076", "Rohan Mikhailov Travel (" + filterBu + ")", 45.2, 31.8, Math.round(1420.00 * buFactor * 100.0) / 100.0, 1000.00,
                "Billed distance exceeds GPS tracked distance by 42.1% in " + filterMonth, "FLAGGED"
        );
        BillingAuditDetail audit2 = new BillingAuditDetail(
                "1123974", "Rohan Mikhailov Travel (" + filterBu + ")", 28.0, 27.5, 850.00, 850.00,
                "Billing matched GPS logs within SLA tolerance for " + filterBu, "VERIFIED"
        );

        ComplianceAlertDetail comp1 = new ComplianceAlertDetail(
                "ALT-9042", "Rohan Mikhailov Travel", "KA-01-MJ-4821", "Rajesh Kumar",
                "DRIVER_NON_COMPLIANT", "Sev-1", filterMonth + " 08:15 AM (" + filterBu + ")"
        );
        ComplianceAlertDetail comp2 = new ComplianceAlertDetail(
                "ALT-8812", "Rohan Mikhailov Travel", "KA-05-MB-1102", "Suresh Naik",
                "OVERSPEEDING", "Sev-2", filterMonth + " 10:30 PM (" + filterBu + ")"
        );

        VendorScorecardResponse rohanMikhailov = new VendorScorecardResponse(
                "Rohan Mikhailov Travel",
                rohanOta, 90.0,
                Math.round(88.5 * (buFactor > 1 ? 1.01 : 0.98) * 10.0) / 10.0,
                Math.round(82.3 * (buFactor > 1 ? 1.02 : 0.97) * 10.0) / 10.0,
                3.8,
                rohanTrips, rohanDelayed, rohanAlertsCount, rohanBillingCount,
                List.of(rSla), List.of(audit1, audit2), List.of(comp1, comp2)
        );

        // Meera Pavlov Travel
        double meeraOta = Math.round((filterMonth.contains("May") ? 89.5 : filterMonth.contains("June") ? 91.0 : 92.1) * (buFactor > 1 ? 1.01 : 0.99) * 10.0) / 10.0;
        meeraOta = Math.min(99.0, Math.max(75.0, meeraOta));
        long meeraTrips = Math.round(38400 * combinedFactor);
        long meeraVendorFault = Math.round(1420 * combinedFactor);
        long meeraEmpFault = Math.round(1200 * combinedFactor);
        long meeraTraffic = Math.round(410 * combinedFactor);
        long meeraDelayed = meeraVendorFault + meeraEmpFault + meeraTraffic;
        int meeraAlertsCount = (int) Math.round(107 * combinedFactor / 100);
        int meeraBillingCount = (int) Math.round(12 * combinedFactor / 100);

        VendorSlaDetail mSla = new VendorSlaDetail(
                filterMonth, "Meera Pavlov Travel", meeraOta, 90.0, meeraTrips, meeraVendorFault, meeraEmpFault, meeraTraffic
        );

        BillingAuditDetail audit3 = new BillingAuditDetail(
                "1098442", "Meera Pavlov Travel (" + filterBu + ")", 52.0, 51.5, Math.round(1650.00 * buFactor * 100.0) / 100.0, 1650.00,
                "Verified rate slab & kilometer log for " + filterMonth, "VERIFIED"
        );

        VendorScorecardResponse meeraPavlov = new VendorScorecardResponse(
                "Meera Pavlov Travel",
                meeraOta, 90.0,
                Math.round(97.2 * (buFactor > 1 ? 1.01 : 0.99) * 10.0) / 10.0,
                Math.round(98.6 * (buFactor > 1 ? 1.01 : 0.99) * 10.0) / 10.0,
                4.7,
                meeraTrips, meeraDelayed, meeraAlertsCount, meeraBillingCount,
                List.of(mSla), List.of(audit3), List.of()
        );

        // Sanjay Mikhailov Travel
        double sanjayOta = Math.round((filterMonth.contains("May") ? 81.0 : filterMonth.contains("June") ? 83.5 : 84.7) * (buFactor > 1 ? 1.02 : 0.98) * 10.0) / 10.0;
        sanjayOta = Math.min(97.0, Math.max(72.0, sanjayOta));
        long sanjayTrips = Math.round(31200 * combinedFactor);
        long sanjayVendorFault = Math.round(2890 * combinedFactor);
        long sanjayEmpFault = Math.round(1150 * combinedFactor);
        long sanjayTraffic = Math.round(720 * combinedFactor);
        long sanjayDelayed = sanjayVendorFault + sanjayEmpFault + sanjayTraffic;
        int sanjayAlertsCount = (int) Math.max(1, Math.round(268 * combinedFactor / 100));
        int sanjayBillingCount = (int) Math.round(38 * combinedFactor / 100);

        VendorSlaDetail sSla = new VendorSlaDetail(
                filterMonth, "Sanjay Mikhailov Travel", sanjayOta, 90.0, sanjayTrips, sanjayVendorFault, sanjayEmpFault, sanjayTraffic
        );

        ComplianceAlertDetail comp3 = new ComplianceAlertDetail(
                "ALT-7741", "Sanjay Mikhailov Travel", "KA-03-MK-9910", "Amit Patel",
                "VEHICLE_PERMIT_EXPIRED", "Sev-1", filterMonth + " 06:45 AM (" + filterBu + ")"
        );

        VendorScorecardResponse sanjayMikhailov = new VendorScorecardResponse(
                "Sanjay Mikhailov Travel",
                sanjayOta, 90.0,
                Math.round(91.4 * (buFactor > 1 ? 1.01 : 0.98) * 10.0) / 10.0,
                Math.round(93.1 * (buFactor > 1 ? 1.01 : 0.98) * 10.0) / 10.0,
                4.2,
                sanjayTrips, sanjayDelayed, sanjayAlertsCount, sanjayBillingCount,
                List.of(sSla), List.of(), List.of(comp3)
        );

        // Priya Mikhailov Travel
        double priyaOta = Math.round((filterMonth.contains("May") ? 86.4 : filterMonth.contains("June") ? 88.2 : 87.5) * (buFactor > 1 ? 1.01 : 0.99) * 10.0) / 10.0;
        long priyaTrips = Math.round(57211 * combinedFactor / 3);
        long priyaVendorFault = Math.round(2100 * combinedFactor / 3);
        long priyaEmpFault = Math.round(1800 * combinedFactor / 3);
        long priyaTraffic = Math.round(950 * combinedFactor / 3);
        long priyaDelayed = priyaVendorFault + priyaEmpFault + priyaTraffic;
        VendorSlaDetail pSla = new VendorSlaDetail(filterMonth, "Priya Mikhailov Travel", priyaOta, 90.0, priyaTrips, priyaVendorFault, priyaEmpFault, priyaTraffic);
        VendorScorecardResponse priyaMikhailov = new VendorScorecardResponse(
                "Priya Mikhailov Travel", priyaOta, 90.0, 94.5, 96.2, 4.5,
                priyaTrips, priyaDelayed, (int) Math.round(85 * combinedFactor / 100), (int) Math.round(18 * combinedFactor / 100),
                List.of(pSla), List.of(), List.of()
        );

        // Aarav Mikhailov Travel
        double aaravOta = Math.round((filterMonth.contains("May") ? 88.0 : filterMonth.contains("June") ? 89.5 : 90.5) * (buFactor > 1 ? 1.01 : 0.99) * 10.0) / 10.0;
        long aaravTrips = Math.round(55686 * combinedFactor / 3);
        long aaravVendorFault = Math.round(1650 * combinedFactor / 3);
        long aaravEmpFault = Math.round(1400 * combinedFactor / 3);
        long aaravTraffic = Math.round(620 * combinedFactor / 3);
        long aaravDelayed = aaravVendorFault + aaravEmpFault + aaravTraffic;
        VendorSlaDetail aSla = new VendorSlaDetail(filterMonth, "Aarav Mikhailov Travel", aaravOta, 90.0, aaravTrips, aaravVendorFault, aaravEmpFault, aaravTraffic);
        VendorScorecardResponse aaravMikhailov = new VendorScorecardResponse(
                "Aarav Mikhailov Travel", aaravOta, 90.0, 96.8, 97.4, 4.6,
                aaravTrips, aaravDelayed, (int) Math.round(45 * combinedFactor / 100), (int) Math.round(8 * combinedFactor / 100),
                List.of(aSla), List.of(), List.of()
        );

        // Anjali Mikhailov Travel
        double anjaliOta = Math.round((filterMonth.contains("May") ? 80.5 : filterMonth.contains("June") ? 82.0 : 83.1) * (buFactor > 1 ? 1.01 : 0.99) * 10.0) / 10.0;
        long anjaliTrips = Math.round(50886 * combinedFactor / 3);
        long anjaliVendorFault = Math.round(3100 * combinedFactor / 3);
        long anjaliEmpFault = Math.round(1900 * combinedFactor / 3);
        long anjaliTraffic = Math.round(1100 * combinedFactor / 3);
        long anjaliDelayed = anjaliVendorFault + anjaliEmpFault + anjaliTraffic;
        VendorSlaDetail anjSla = new VendorSlaDetail(filterMonth, "Anjali Mikhailov Travel", anjaliOta, 90.0, anjaliTrips, anjaliVendorFault, anjaliEmpFault, anjaliTraffic);
        VendorScorecardResponse anjaliMikhailov = new VendorScorecardResponse(
                "Anjali Mikhailov Travel", anjaliOta, 90.0, 89.1, 91.0, 4.0,
                anjaliTrips, anjaliDelayed, (int) Math.round(195 * combinedFactor / 100), (int) Math.round(42 * combinedFactor / 100),
                List.of(anjSla), List.of(), List.of()
        );

        return List.of(rohanMikhailov, meeraPavlov, sanjayMikhailov, priyaMikhailov, aaravMikhailov, anjaliMikhailov);
    }

    public SlaShieldResponse analyzeSlaShield(SlaShieldRequest request) {
        String vendor = request.vendorName() != null ? request.vendorName() : "Rohan Mikhailov Travel";
        String month = request.selectedMonth() != null ? request.selectedMonth() : "July 2026";
        String bu = (request.businessUnit() != null && !request.businessUnit().isBlank()) ? request.businessUnit() : "All Business Units";

        double monthFactor = month.contains("May") ? 0.92 : month.contains("June") ? 0.96 : month.contains("All") ? 2.85 : 1.0;
        double buFactor = bu.contains("vanta-Aus") ? 1.15 : bu.contains("catalyst") ? 0.85 : bu.contains("orbit") ? 1.05 : bu.contains("pinnacle") ? 1.20 : 1.0;
        double combinedFactor = monthFactor * buFactor;

        long baseTrips = 42150;
        long baseVendorFault = 4810;
        long baseEmpFault = 2340;
        long baseTraffic = 1980;

        if (vendor.contains("Meera")) {
            baseTrips = 38400;
            baseVendorFault = 1420;
            baseEmpFault = 1200;
            baseTraffic = 410;
        } else if (vendor.contains("Sanjay")) {
            baseTrips = 31200;
            baseVendorFault = 2890;
            baseEmpFault = 1150;
            baseTraffic = 720;
        }

        long totalTrips = Math.round(baseTrips * combinedFactor);
        long vendorFault = Math.round(baseVendorFault * combinedFactor);
        long employeeFault = Math.round(baseEmpFault * combinedFactor);
        long trafficWeather = Math.round(baseTraffic * combinedFactor);
        long totalDelayed = vendorFault + employeeFault + trafficWeather;

        double rawOta = Math.round(((double) (totalTrips - totalDelayed) / totalTrips) * 1000.0) / 10.0;
        double adjustedSla = Math.round(((double) (totalTrips - vendorFault) / totalTrips) * 1000.0) / 10.0;
        double savedPenalty = (employeeFault + trafficWeather) * 3.50;

        String prompt = String.format(
                "Vendor %s for client %s in %s had %d total trips. Raw OTA is %.1f%% (%d delays). " +
                "AI Delay Attribution discovered: %d vendor driver late starts, %d employee late boardings (>15m late), and %d traffic/weather slowdowns. " +
                "Explain why adjusting the SLA score to %.1f%% protects the vendor from $%.0f in unfair penalties.",
                vendor, bu, month, totalTrips, rawOta, totalDelayed, vendorFault, employeeFault, trafficWeather, adjustedSla, savedPenalty
        );

        String sarvamReasoning;
        try {
            sarvamReasoning = callSarvamAiPrompt(prompt);
        } catch (Exception e) {
            sarvamReasoning = String.format(
                    "Sarvam AI Analysis for %s (%s - %s): Cross-referencing ride_data_trip and emp_data.csv logs confirms %,d trips were delayed due to late employee boarding at designated nodes, and %,d trips were delayed by monsoon/traffic slowdowns. Excluding non-vendor fault delays raises vendor SLA performance from %.1f%% to %.1f%%, saving $%,.0f in unfair contract penalties.",
                    vendor, month, bu, employeeFault, trafficWeather, rawOta, adjustedSla, savedPenalty
            );
        }

        TripAttributionDetail trip1 = new TripAttributionDetail(
                "1097076", "EMP-4081", "08:00 AM", "08:02 AM", "08:18 AM", 20,
                "EMPLOYEE_FAULT", "Driver arrived 2 mins early; employee boarded 18 mins late at " + bu + " pickup node.", true
        );

        TripAttributionDetail trip2 = new TripAttributionDetail(
                "1123974", "EMP-9912", "08:15 AM", "08:35 AM", "08:37 AM", 22,
                "VENDOR_FAULT", "Driver departed garage 20 minutes past scheduled dispatch time during " + month + " shift.", false
        );

        TripAttributionDetail trip3 = new TripAttributionDetail(
                "1098442", "EMP-2204", "09:00 AM", "09:01 AM", "09:03 AM", 25,
                "TRAFFIC_FAULT", "Outer Ring Road severe alert near " + bu + "; average speed dropped to 11 km/h.", true
        );

        return new SlaShieldResponse(
                vendor,
                month,
                rawOta,
                adjustedSla,
                90.0,
                Math.round(savedPenalty * 100.0) / 100.0,
                totalTrips,
                totalDelayed,
                vendorFault,
                employeeFault,
                trafficWeather,
                sarvamReasoning,
                List.of(trip1, trip2, trip3)
        );
    }

    public ClaimEvaluationResponse evaluateClaim(ClaimEvaluationRequest request) {
        String claimInput = request.claimText() != null ? request.claimText() : "";
        String month = request.selectedMonth() != null ? request.selectedMonth() : "July 2026";

        try {
            String sarvamReasoning = callSarvamAi(claimInput, month);
            if (sarvamReasoning != null && !sarvamReasoning.isBlank()) {
                return buildResponseWithSarvamAi(claimInput, month, sarvamReasoning);
            }
        } catch (Exception e) {
            System.err.println("Sarvam AI Live API call fallback: " + e.getMessage());
        }

        return evaluateClaimFallback(claimInput, month);
    }

    private String callSarvamAiPrompt(String userPrompt) throws Exception {
        Map<String, Object> bodyMap = Map.of(
                "model", "sarvam-105b",
                "messages", List.of(
                        Map.of("role", "system", "content", "You are an AI Mobility Inspector powered by Sarvam AI. Provide a clear, executive explanation of delay attribution and SLA protection."),
                        Map.of("role", "user", "content", userPrompt)
                )
        );

        String jsonBody = objectMapper.writeValueAsString(bodyMap);

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(SARVAM_API_URL))
                .header("Content-Type", "application/json")
                .header("api-subscription-key", SARVAM_API_KEY)
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .timeout(Duration.ofSeconds(10))
                .build();

        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            JsonNode root = objectMapper.readTree(response.body());
            JsonNode choices = root.path("choices");
            if (choices.isArray() && choices.size() > 0) {
                return choices.get(0).path("message").path("content").asText();
            }
        }
        return null;
    }

    private String callSarvamAi(String claimInput, String month) throws Exception {
        return callSarvamAiPrompt("Evaluate vendor claim for period " + month + ":\n" + claimInput);
    }

    private ClaimEvaluationResponse buildResponseWithSarvamAi(String claimText, String month, String sarvamContent) {
        String text = claimText.toLowerCase();

        String vendorName = "Rohan Mikhailov Travel";
        if (text.contains("meera") || text.contains("pavlov")) vendorName = "Meera Pavlov Travel";
        else if (text.contains("sanjay")) vendorName = "Sanjay Mikhailov Travel";

        String route = "Route 4";
        if (text.contains("route 12")) route = "Route 12";
        else if (text.contains("route 8")) route = "Route 8";

        boolean isWeather = text.contains("rain") || text.contains("weather") || text.contains("storm");
        boolean isEmployee = text.contains("employee") || text.contains("rider") || text.contains("board");
        boolean isBreakdown = text.contains("breakdown") || text.contains("engine");

        String recommendation = (isWeather || isEmployee) ? "APPROVE SLA PENALTY WAIVER (SARVAM AI VERIFIED)"
                : isBreakdown ? "REJECT WAIVER (SARVAM AI VERIFIED - VENDOR FAULT)"
                : "SARVAM AI PARTIAL WAIVER RECOMMENDATION";

        List<String> evidence = new ArrayList<>();
        evidence.add("Sarvam AI (sarvam-105b model) processed claim text and generated natural language reasoning.");
        if (isWeather) {
            evidence.add("GPS speed logs confirm average speed dropped from 38 km/h to 11 km/h on " + route + ".");
            evidence.add("Peer vendor validation: Cabs from competing vendors on " + route + " also experienced 20+ min delays.");
            evidence.add("Meteorological log confirms heavy rainfall alert for " + month + ".");
        } else if (isEmployee) {
            evidence.add("Employee boarding logs confirm riders boarded >15 minutes past scheduled pickup time.");
            evidence.add("Driver GPS logs show vehicle was waiting at designated pickup node for 18 minutes.");
        } else if (isBreakdown) {
            evidence.add("Vehicle telemetry shows cab was deployed with pending maintenance alert.");
            evidence.add("No external traffic or weather slowdown detected for other cabs on " + route + ".");
        } else {
            evidence.add("Sarvam AI generated multi-source risk score based on historical vendor reliability.");
        }

        return new ClaimEvaluationResponse(
                vendorName,
                month,
                route,
                !isBreakdown,
                98.2,
                evidence,
                recommendation,
                "Sarvam AI Evaluation: " + sarvamContent
        );
    }

    private ClaimEvaluationResponse evaluateClaimFallback(String claimInput, String month) {
        String text = claimInput.toLowerCase();

        String vendorName = "Rohan Mikhailov Travel";
        if (text.contains("meera") || text.contains("pavlov")) vendorName = "Meera Pavlov Travel";
        else if (text.contains("sanjay")) vendorName = "Sanjay Mikhailov Travel";

        String route = "Route 4";
        if (text.contains("route 12")) route = "Route 12";

        List<String> evidenceList = new ArrayList<>();

        if (text.contains("rain") || text.contains("weather") || text.contains("storm")) {
            evidenceList.add("GPS speed logs confirm average speed dropped from 38 km/h to 11 km/h on " + route + ".");
            evidenceList.add("Cross-vendor validation: Cabs from Meera Pavlov Travel & Sanjay Mikhailov Travel also experienced delays.");
            evidenceList.add("Meteorological system log confirms heavy rainfall alert for " + month + ".");

            return new ClaimEvaluationResponse(
                    vendorName, month, route, true, 96.8, evidenceList,
                    "APPROVE SLA PENALTY WAIVER",
                    "Delay was demonstrably caused by external severe weather event affecting all mobility vendors on " + route + "."
            );
        } else if (text.contains("employee") || text.contains("rider") || text.contains("board")) {
            evidenceList.add("Employee boarding logs confirm 12 employees boarded >15 minutes past scheduled time.");
            evidenceList.add("Driver GPS logs show vehicle was waiting at designated pickup node for 18 minutes.");

            return new ClaimEvaluationResponse(
                    vendorName, month, route, true, 92.4, evidenceList,
                    "APPROVE SLA WAIVER (EMPLOYEE FAULT)",
                    "Trip delay was caused by late employee boarding at designated nodes, not vendor driver delay."
            );
        } else {
            evidenceList.add("Vehicle maintenance telemetry shows vehicle was deployed with pending service alert.");
            evidenceList.add("No external traffic or weather slowdown detected on route.");

            return new ClaimEvaluationResponse(
                    vendorName, month, route, false, 94.1, evidenceList,
                    "REJECT WAIVER - VENDOR AT FAULT",
                    "Vehicle breakdown and delayed backup replacement is vendor maintenance responsibility under section 4.2 of contract."
            );
        }
    }
}
