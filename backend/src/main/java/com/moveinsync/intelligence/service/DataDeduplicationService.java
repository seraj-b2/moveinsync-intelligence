package com.moveinsync.intelligence.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.moveinsync.intelligence.dto.DeduplicationReportResponse;
import com.moveinsync.intelligence.entity.BillingDiscrepancyEntity;
import com.moveinsync.intelligence.entity.ComplianceAlertEntity;
import com.moveinsync.intelligence.entity.VendorDisputeEntity;
import com.moveinsync.intelligence.repository.BillingDiscrepancyRepository;
import com.moveinsync.intelligence.repository.ComplianceAlertRepository;
import com.moveinsync.intelligence.repository.VendorDisputeRepository;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.*;

@Service
public class DataDeduplicationService {

    private final VendorDisputeRepository disputeRepository;
    private final BillingDiscrepancyRepository billingRepository;
    private final ComplianceAlertRepository alertRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public DataDeduplicationService(VendorDisputeRepository disputeRepository,
                                    BillingDiscrepancyRepository billingRepository,
                                    ComplianceAlertRepository alertRepository) {
        this.disputeRepository = disputeRepository;
        this.billingRepository = billingRepository;
        this.alertRepository = alertRepository;
    }

    public DeduplicationReportResponse processAndDeduplicate(String content, String datasetType) {
        if (content == null || content.isBlank()) {
            return new DeduplicationReportResponse(0, 0, 0, 0.0, datasetType, "ERROR", "Empty file content received.");
        }

        String type = (datasetType != null) ? datasetType.toUpperCase() : "BILLING";

        if (type.contains("DISPUTE")) {
            return processDisputesDeduplication(content);
        } else if (type.contains("ALERT")) {
            return processAlertsDeduplication(content);
        } else {
            return processBillingDeduplication(content);
        }
    }

    private DeduplicationReportResponse processBillingDeduplication(String content) {
        int totalRows = 0;
        int newSaved = 0;
        int duplicates = 0;
        Set<String> seenKeysInBatch = new HashSet<>();

        try {
            if (content.trim().startsWith("[") || content.trim().startsWith("{")) {
                JsonNode array = objectMapper.readTree(content);
                if (array.isArray()) {
                    for (JsonNode node : array) {
                        totalRows++;
                        String tripId = node.has("tripId") ? node.get("tripId").asText() : "TRP-" + (1000 + totalRows);
                        String vendor = node.has("vendorName") ? node.get("vendorName").asText() : "Rohan Mikhailov Travel";
                        String compositeKey = tripId + "::" + vendor;

                        if (seenKeysInBatch.contains(compositeKey) || billingRepository.existsByTripIdAndVendorName(tripId, vendor)) {
                            duplicates++;
                        } else {
                            seenKeysInBatch.add(compositeKey);
                            double billedKm = node.has("billedKm") ? node.get("billedKm").asDouble() : 45.0;
                            double gpsKm = node.has("gpsActualKm") ? node.get("gpsActualKm").asDouble() : 38.2;
                            double billedCost = node.has("billedCost") ? node.get("billedCost").asDouble() : 120.0;
                            double expectedCost = node.has("auditExpectedCost") ? node.get("auditExpectedCost").asDouble() : 95.0;
                            String reason = node.has("discrepancyReason") ? node.get("discrepancyReason").asText() : "GPS distance mismatch";
                            String status = node.has("status") ? node.get("status").asText() : "FLAGGED";

                            billingRepository.save(new BillingDiscrepancyEntity(
                                    tripId, vendor, "July 2026", "All Business Units",
                                    billedKm, gpsKm, billedCost, expectedCost, reason, status
                            ));
                            newSaved++;
                        }
                    }
                }
            } else {
                BufferedReader reader = new BufferedReader(new StringReader(content));
                String line;
                boolean isHeader = true;

                while ((line = reader.readLine()) != null) {
                    if (line.isBlank()) continue;
                    if (isHeader) { isHeader = false; continue; }
                    totalRows++;

                    String[] parts = line.split(",");
                    String tripId = parts.length > 0 ? parts[0].trim().replace("\"", "") : "TRP-" + totalRows;
                    String vendor = parts.length > 1 ? parts[1].trim().replace("\"", "") : "Rohan Mikhailov Travel";
                    String compositeKey = tripId + "::" + vendor;

                    if (seenKeysInBatch.contains(compositeKey) || billingRepository.existsByTripIdAndVendorName(tripId, vendor)) {
                        duplicates++;
                    } else {
                        seenKeysInBatch.add(compositeKey);
                        double billedKm = parts.length > 2 ? parseDouble(parts[2], 45.0) : 45.0;
                        double gpsKm = parts.length > 3 ? parseDouble(parts[3], 38.2) : 38.2;
                        double billedCost = parts.length > 4 ? parseDouble(parts[4], 120.0) : 120.0;
                        double expectedCost = parts.length > 5 ? parseDouble(parts[5], 95.0) : 95.0;
                        String reason = parts.length > 6 ? parts[6].trim().replace("\"", "") : "CSV Ingested GPS Discrepancy";

                        billingRepository.save(new BillingDiscrepancyEntity(
                                tripId, vendor, "July 2026", "All Business Units",
                                billedKm, gpsKm, billedCost, expectedCost, reason, "FLAGGED"
                        ));
                        newSaved++;
                    }
                }
            }
        } catch (Exception e) {
            return new DeduplicationReportResponse(totalRows, newSaved, duplicates, 0.0, "BILLING", "PARTIAL_ERROR", "Error parsing billing file: " + e.getMessage());
        }

        double efficiency = totalRows > 0 ? Math.round(((double) duplicates / totalRows) * 1000.0) / 10.0 : 0.0;
        return new DeduplicationReportResponse(totalRows, newSaved, duplicates, efficiency, "BILLING_DATA", "SUCCESS",
                "Deduplication Engine completed: " + newSaved + " new unique billing items saved to H2 DB, " + duplicates + " duplicate rows merged.");
    }

    private DeduplicationReportResponse processDisputesDeduplication(String content) {
        int totalRows = 0;
        int newSaved = 0;
        int duplicates = 0;
        Set<String> seenKeys = new HashSet<>();

        try {
            JsonNode array = objectMapper.readTree(content);
            if (array.isArray()) {
                for (JsonNode node : array) {
                    totalRows++;
                    String dispId = node.has("disputeId") ? node.get("disputeId").asText() : "DSP-" + (1000 + totalRows);

                    if (seenKeys.contains(dispId) || disputeRepository.existsById(dispId)) {
                        duplicates++;
                    } else {
                        seenKeys.add(dispId);
                        String vendor = node.has("vendorName") ? node.get("vendorName").asText() : "Rohan Mikhailov Travel";
                        String route = node.has("route") ? node.get("route").asText() : "Route 4";
                        String subject = node.has("claimSubject") ? node.get("claimSubject").asText() : "Uploaded SLA Dispute";
                        String text = node.has("claimText") ? node.get("claimText").asText() : "Dispute claim content";
                        String month = node.has("month") ? node.get("month").asText() : "July 2026";
                        String bu = node.has("businessUnit") ? node.get("businessUnit").asText() : "All Business Units";
                        int cabs = node.has("affectedCabs") ? node.get("affectedCabs").asInt() : 5;

                        disputeRepository.save(new VendorDisputeEntity(
                                dispId, vendor, route, subject, text, month, bu, cabs, "PENDING_REVIEW", "Uploaded File"
                        ));
                        newSaved++;
                    }
                }
            }
        } catch (Exception e) {
            return new DeduplicationReportResponse(totalRows, newSaved, duplicates, 0.0, "DISPUTES", "ERROR", "Failed to parse disputes: " + e.getMessage());
        }

        double efficiency = totalRows > 0 ? Math.round(((double) duplicates / totalRows) * 1000.0) / 10.0 : 0.0;
        return new DeduplicationReportResponse(totalRows, newSaved, duplicates, efficiency, "VENDOR_DISPUTES", "SUCCESS",
                "Ingested " + newSaved + " new dispute claims to H2 DB, merged " + duplicates + " duplicates.");
    }

    private DeduplicationReportResponse processAlertsDeduplication(String content) {
        int totalRows = 0;
        int newSaved = 0;
        int duplicates = 0;
        Set<String> seenKeys = new HashSet<>();

        try {
            JsonNode array = objectMapper.readTree(content);
            if (array.isArray()) {
                for (JsonNode node : array) {
                    totalRows++;
                    String alertId = node.has("alertId") ? node.get("alertId").asText() : "ALT-" + (1000 + totalRows);

                    if (seenKeys.contains(alertId) || alertRepository.existsByAlertId(alertId)) {
                        duplicates++;
                    } else {
                        seenKeys.add(alertId);
                        String vendor = node.has("vendorName") ? node.get("vendorName").asText() : "Rohan Mikhailov Travel";
                        String vehicle = node.has("vehicleReg") ? node.get("vehicleReg").asText() : "KA-03-MK-9910";
                        String driver = node.has("driverName") ? node.get("driverName").asText() : "Driver";
                        String type = node.has("alertType") ? node.get("alertType").asText() : "Speed Violation";
                        String sev = node.has("severity") ? node.get("severity").asText() : "Sev-1";

                        alertRepository.save(new ComplianceAlertEntity(
                                alertId, vendor, "July 2026", "All Business Units", vehicle, driver, type, sev, "Today"
                        ));
                        newSaved++;
                    }
                }
            }
        } catch (Exception e) {
            return new DeduplicationReportResponse(totalRows, newSaved, duplicates, 0.0, "ALERTS", "ERROR", "Failed to parse alerts: " + e.getMessage());
        }

        double efficiency = totalRows > 0 ? Math.round(((double) duplicates / totalRows) * 1000.0) / 10.0 : 0.0;
        return new DeduplicationReportResponse(totalRows, newSaved, duplicates, efficiency, "COMPLIANCE_ALERTS", "SUCCESS",
                "Saved " + newSaved + " new safety alerts to H2 DB, dropped " + duplicates + " duplicate alerts.");
    }

    private double parseDouble(String str, double defaultVal) {
        try {
            return Double.parseDouble(str.trim().replace("\"", ""));
        } catch (Exception e) {
            return defaultVal;
        }
    }
}
