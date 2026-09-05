package com.moveinsync.intelligence.service;

import com.moveinsync.intelligence.dto.EmployeePortalResponse;
import com.moveinsync.intelligence.dto.EmployeePortalResponse.*;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.*;

@Service
public class EmployeePortalService {

    private record EmpMeta(
            String stwid,
            String name,
            String role,
            String office,
            String gender,
            String managerId,
            String managerName,
            String defaultShift,
            String defaultRoute,
            String defaultCab,
            String driverName,
            String driverPhone,
            String pickupLocation,
            double basePlannedKm,
            boolean escortRequired,
            String escortName
    ) {}

    private final Map<String, EmpMeta> directory = new LinkedHashMap<>();

    public EmployeePortalService() {
        // MGR-103 Direct Reports (Support & NOC Ops)
        directory.put("STW-718290", new EmpMeta("STW-718290", "Meenakshi Raman", "Support Lead & Operations Lead", "Cedar Ridge Office", "FEMALE", "MGR-103", "Meenakshi Raman", "18:15", "Route 03", "KA-05-MK-3310", "Manjunath K", "+91-98804-99881", "HSR Sector 2 -> Cedar Ridge Office", 10.50, false, "N/A"));
        directory.put("STW-829104", new EmpMeta("STW-829104", "Tanmay Sen", "Tier-2 Incident Analyst", "Cedar Ridge Office", "MALE", "MGR-103", "Meenakshi Raman", "18:15", "Route 03", "KA-05-MK-3310", "Manjunath K", "+91-98804-99881", "Koramangala Sony World -> Cedar Ridge Office", 13.20, false, "N/A"));
        directory.put("STW-930192", new EmpMeta("STW-930192", "Deepika Rao", "Global Escalation Specialist", "Fairview Commons", "FEMALE", "MGR-103", "Meenakshi Raman", "18:15", "Route 11", "KA-01-NJ-7712", "Anand Rao", "+91-98803-12490", "Indiranagar 100ft Rd -> Fairview Commons", 19.80, false, "N/A"));
        directory.put("STW-104928", new EmpMeta("STW-104928", "Siddharth Paul", "NOC Systems Engineer", "Fairview Commons", "MALE", "MGR-103", "Meenakshi Raman", "18:15", "Route 11", "KA-01-NJ-7712", "Anand Rao", "+91-98803-12490", "Domlur Flyover Stop -> Fairview Commons", 15.00, false, "N/A"));

        // MGR-101 Direct Reports (Cloud & SRE Ops)
        directory.put("STW-484475", new EmpMeta("STW-484475", "Ananya Sharma", "Senior Site Reliability Engineer (SRE)", "Fairview Commons", "FEMALE", "MGR-101", "Vikram Malhotra", "21:30", "Route 12", "KA-01-MJ-4821", "Rajesh Kumar", "+91-98801-44321", "Bellandur Outer Ring Rd -> Fairview Commons", 9.97, true, "Security Marshal Ramesh Singh (Badge #SM-104)"));
        directory.put("STW-332325", new EmpMeta("STW-332325", "Vikram Malhotra", "Team Lead - SRE", "Fairview Commons", "MALE", "MGR-101", "Vikram Malhotra", "21:30", "Route 12", "KA-01-MJ-4821", "Rajesh Kumar", "+91-98801-44321", "Sarjapur Signal -> Fairview Commons", 14.50, false, "N/A"));
        directory.put("STW-194821", new EmpMeta("STW-194821", "Pooja Hegde", "Cloud Infrastructure Engineer", "Cedar Ridge Office", "FEMALE", "MGR-101", "Vikram Malhotra", "21:30", "Route 04", "KA-05-MB-1102", "Suresh Gowda", "+91-98802-77112", "Koramangala 4th Block -> Cedar Ridge Office", 22.10, true, "Security Marshal Ramesh Kumar (Badge #SM-401)"));
        directory.put("STW-101925", new EmpMeta("STW-101925", "Rahul Verma", "Principal Database Administrator", "Cedar Ridge Office", "MALE", "MGR-101", "Vikram Malhotra", "21:30", "Route 04", "KA-05-MB-1102", "Suresh Gowda", "+91-98802-77112", "Jayanagar 4th T Block -> Cedar Ridge Office", 7.66, false, "N/A"));
        directory.put("STW-093335", new EmpMeta("STW-093335", "Neha Sundaram", "Security Operations Analyst", "Santa Clara Office", "FEMALE", "MGR-101", "Vikram Malhotra", "21:30", "Route 09", "KA-03-MK-9910", "Dinesh Verma", "+91-98805-33441", "BTM 2nd Stage -> Santa Clara Office", 11.20, true, "Security Marshal Anil Kumar (Badge #SM-202)"));
        directory.put("STW-512401", new EmpMeta("STW-512401", "Arjun Nair", "Support Systems Engineer", "Fairview Commons", "MALE", "MGR-101", "Vikram Malhotra", "21:30", "Route 12", "KA-01-MJ-4821", "Rajesh Kumar", "+91-98801-44321", "Silk Board Junction -> Fairview Commons", 8.40, false, "N/A"));

        // MGR-102 Direct Reports (Digital Banking)
        directory.put("STW-204918", new EmpMeta("STW-204918", "Karthik Iyer", "Principal Enterprise Architect", "Fairview Commons", "MALE", "MGR-102", "Priya Sharma", "08:30", "Route 01", "KA-02-EA-1920", "Raghavendra M", "+91-98806-11223", "Malleswaram 18th Cross -> Fairview Commons", 16.20, false, "N/A"));
        directory.put("STW-309182", new EmpMeta("STW-309182", "Sneha Kulkarni", "Frontend Engineering Lead", "Fairview Commons", "FEMALE", "MGR-102", "Priya Sharma", "08:30", "Route 01", "KA-02-EA-1920", "Raghavendra M", "+91-98806-11223", "Rajajinagar Metro -> Fairview Commons", 12.40, false, "N/A"));
        directory.put("STW-401928", new EmpMeta("STW-401928", "Aditya Joshi", "Senior Backend Engineer", "Santa Clara Office", "MALE", "MGR-102", "Priya Sharma", "08:30", "Route 07", "KA-04-TR-8811", "Kishore Kumar", "+91-98807-55667", "Whitefield Main Rd -> Santa Clara Office", 18.90, false, "N/A"));
        directory.put("STW-510293", new EmpMeta("STW-510293", "Divya Menon", "Senior QA Automation Specialist", "Santa Clara Office", "FEMALE", "MGR-102", "Priya Sharma", "08:30", "Route 07", "KA-04-TR-8811", "Kishore Kumar", "+91-98807-55667", "Marathahalli Bridge -> Santa Clara Office", 14.10, false, "N/A"));
        directory.put("STW-619283", new EmpMeta("STW-619283", "Rohan Das", "Product Experience Designer", "Clearwater Campus", "MALE", "MGR-102", "Priya Sharma", "08:30", "Route 15", "KA-01-PL-4490", "Pradeep S", "+91-98808-99001", "Electronic City Phase 1 -> Clearwater Campus", 9.80, false, "N/A"));
    }

    public EmployeePortalResponse getEmployeePortalData(String companyName, String employeeId, String month) {
        String company = (companyName == null || companyName.isBlank()) ? "catalyst-Sac" : companyName;
        String stwid = (employeeId == null || employeeId.isBlank()) ? "STW-484475" : employeeId.toUpperCase().trim();
        String period = (month == null || month.isBlank() || month.equalsIgnoreCase("ALL") || month.toLowerCase().contains("all")) ? "All Historical Data (May - July 2026)" : month;

        EmpMeta meta = directory.getOrDefault(stwid, new EmpMeta(
                stwid,
                "Employee " + stwid,
                "Software Engineer",
                "Fairview Commons",
                "MALE",
                "MGR-101",
                "Vikram Malhotra",
                "21:30",
                "Route 12",
                "KA-01-MJ-4821",
                "Rajesh Kumar",
                "+91-98801-44321",
                "Bangalore Tech Corridor -> Office",
                12.00,
                false,
                "N/A"
        ));

        // Generate full daily activities for all days of the requested month or all history
        List<TripActivityRecord> history = generateAllDaysForMonth(meta, period);

        // Compute live metrics across all days of this period
        int totalDaysInMonth = history.size();
        long workingDays = history.stream().filter(h -> !h.isNoShow() && !"Weekend Off".equalsIgnoreCase(h.boardingStatus()) && !"WFH / Approved Leave".equalsIgnoreCase(h.boardingStatus())).count();
        long onTimeTrips = history.stream().filter(h -> !h.isNoShow() && h.delayMinutes() == 0 && ("Boarded".equalsIgnoreCase(h.boardingStatus()) || "On-Time".equalsIgnoreCase(h.boardingStatus()))).count();
        double totalKm = history.stream().mapToDouble(TripActivityRecord::traveledKm).sum();
        double totalKmRounded = Math.round(totalKm * 10.0) / 10.0;
        double onTimeRate = (workingDays > 0) ? Math.round(((double) onTimeTrips / workingDays) * 1000.0) / 10.0 : 95.0;
        double avgCommuteDelay = history.stream()
                .filter(h -> !h.isNoShow() && !"Weekend Off".equalsIgnoreCase(h.boardingStatus()) && !"WFH / Approved Leave".equalsIgnoreCase(h.boardingStatus()))
                .mapToInt(TripActivityRecord::delayMinutes)
                .average()
                .orElse(0.0);
        int avgDelay = (int) Math.round(avgCommuteDelay);

        EmployeeProfile profile = new EmployeeProfile(
                meta.stwid(),
                meta.name(),
                meta.role(),
                company,
                meta.office(),
                meta.gender(),
                meta.managerId(),
                meta.managerName(),
                onTimeRate,
                (int) workingDays,
                meta.escortRequired() ? 100.0 : 98.0
        );

        String otp = String.valueOf(Math.abs(meta.stwid().hashCode()) % 9000 + 1000);
        String scheduledPickup;
        if ("18:15".equalsIgnoreCase(meta.defaultShift())) {
            scheduledPickup = "17:35";
        } else if ("21:30".equalsIgnoreCase(meta.defaultShift())) {
            scheduledPickup = "20:50";
        } else {
            scheduledPickup = "07:50";
        }

        LiveTripDetail liveTrip = new LiveTripDetail(
                "TRIP-LIVE-" + Math.abs((meta.stwid() + "TODAY").hashCode() % 90000 + 10000),
                "Today, September 5, 2026",
                "Shift " + meta.defaultShift() + " - Today's Live Commute",
                "LOGIN",
                meta.defaultRoute(),
                meta.defaultCab(),
                meta.driverName(),
                meta.driverPhone(),
                meta.pickupLocation(),
                scheduledPickup,
                12,
                otp,
                "IN_TRANSIT",
                meta.escortRequired(),
                meta.escortName(),
                true
        );

        List<EmployeeSafetyAlert> safetyAlerts = new ArrayList<>();
        if (meta.escortRequired()) {
            safetyAlerts.add(new EmployeeSafetyAlert(
                    "ALT-SAF-8901",
                    "WOMAN_TRAVELLING_ALONE",
                    "Sev-1",
                    period + " " + meta.defaultShift(),
                    meta.defaultCab(),
                    "ACTIVE_ESCORT",
                    "100% Female Night Drop Security Protocol active. " + meta.escortName() + " verified on-board with GPS telemetry broadcast."
            ));
        }
        if ("STW-930192".equalsIgnoreCase(stwid)) {
            safetyAlerts.add(new EmployeeSafetyAlert(
                    "ALT-SAF-6041",
                    "EMPLOYEE_GEOFENCE_VIOLATION",
                    "Sev-3",
                    period + " 18:38",
                    meta.defaultCab(),
                    "ACKNOWLEDGED",
                    "Driver detoured 1.2km outside geofence corridor due to flyover construction. Control room verified driver route."
            ));
        }

        List<EmployeeFeedbackRecord> feedback = List.of(
                new EmployeeFeedbackRecord("TRIP-FB-01", "Recent Commute", 4.9, 5, 5, 5, 5, 5, "Driver " + meta.driverName() + " was very punctual and courteous."),
                new EmployeeFeedbackRecord("TRIP-FB-02", "Previous Commute", 4.7, 5, 4, 4, 5, 5, "Clean vehicle and smooth commute without unexpected detours.")
        );

        EmployeeCommuteStats stats = new EmployeeCommuteStats(
                (int) workingDays,
                onTimeRate,
                totalKmRounded,
                avgDelay,
                4.8
        );

        return new EmployeePortalResponse(profile, liveTrip, history, safetyAlerts, feedback, stats);
    }

    private List<TripActivityRecord> generateAllDaysForMonth(EmpMeta meta, String filterMonth) {
        int year = 2026;
        List<TripActivityRecord> records = new ArrayList<>();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        List<Integer> monthsToGenerate = new ArrayList<>();
        String lowerFilter = filterMonth != null ? filterMonth.toLowerCase() : "";
        if (lowerFilter.contains("all") || lowerFilter.equals("all") || lowerFilter.isBlank()) {
            // All historical data (May - July 2026): all 92 days!
            monthsToGenerate.add(7);
            monthsToGenerate.add(6);
            monthsToGenerate.add(5);
        } else if (lowerFilter.contains("july")) {
            monthsToGenerate.add(7);
        } else if (lowerFilter.contains("june")) {
            monthsToGenerate.add(6);
        } else if (lowerFilter.contains("may")) {
            monthsToGenerate.add(5);
        } else {
            monthsToGenerate.add(7);
            monthsToGenerate.add(6);
            monthsToGenerate.add(5);
        }

        for (int month : monthsToGenerate) {
            LocalDate startDate = LocalDate.of(year, month, 1);
            int daysInMonth = startDate.lengthOfMonth();

            for (int day = daysInMonth; day >= 1; day--) {
            LocalDate date = LocalDate.of(year, month, day);
            DayOfWeek dow = date.getDayOfWeek();
            String dowName = dow.getDisplayName(TextStyle.SHORT, Locale.ENGLISH);
            String dateStr = date.format(dtf);

            if (dow == DayOfWeek.SATURDAY || dow == DayOfWeek.SUNDAY) {
                // Full weekend tracking
                records.add(new TripActivityRecord(
                        "OFF-" + dateStr,
                        dateStr,
                        dowName,
                        "Shift Off",
                        "REST_DAY",
                        "N/A",
                        "N/A",
                        "--:--",
                        "--:--",
                        0.0,
                        0.0,
                        "Weekend Off",
                        0,
                        "Scheduled Weekly Rest Day",
                        false
                ));
            } else {
                // Weekdays
                String tripId = "TRIP-" + (year % 100) + String.format("%02d", month) + String.format("%02d", day) + (Math.abs(meta.stwid().hashCode()) % 900 + 100);

                // Derive planned pickup time based on shift
                String plannedPickupTime;
                if ("18:15".equalsIgnoreCase(meta.defaultShift())) {
                    plannedPickupTime = "17:35";
                } else if ("21:30".equalsIgnoreCase(meta.defaultShift())) {
                    plannedPickupTime = "20:50";
                } else {
                    plannedPickupTime = "07:50";
                }

                // Operational events on specific calendar days
                if (day == 8) {
                    // Major Traffic Bottleneck
                    records.add(new TripActivityRecord(
                            tripId,
                            dateStr,
                            dowName,
                            meta.defaultShift(),
                            "LOGIN",
                            meta.defaultRoute(),
                            meta.defaultCab(),
                            plannedPickupTime,
                            addMinutes(plannedPickupTime, 18),
                            meta.basePlannedKm(),
                            Math.round((meta.basePlannedKm() + 5.8) * 10.0) / 10.0,
                            "Delayed",
                            18,
                            "TRAFFIC_BOTTLENECK_FLYOVER",
                            false
                    ));
                } else if (day == 15) {
                    // Geofence detour or moderate delay
                    records.add(new TripActivityRecord(
                            tripId,
                            dateStr,
                            dowName,
                            meta.defaultShift(),
                            "LOGIN",
                            meta.defaultRoute(),
                            meta.defaultCab(),
                            plannedPickupTime,
                            addMinutes(plannedPickupTime, 12),
                            meta.basePlannedKm(),
                            Math.round((meta.basePlannedKm() + 2.4) * 10.0) / 10.0,
                            "Delayed",
                            12,
                            "ROAD_CONSTRUCTION_DETOUR",
                            false
                    ));
                } else if (day == 22) {
                    // No-show or WFH for variety
                    if ("STW-930192".equalsIgnoreCase(meta.stwid()) || "STW-104928".equalsIgnoreCase(meta.stwid()) || "STW-101925".equalsIgnoreCase(meta.stwid())) {
                        records.add(new TripActivityRecord(
                                tripId,
                                dateStr,
                                dowName,
                                meta.defaultShift(),
                                "LOGIN",
                                meta.defaultRoute(),
                                meta.defaultCab(),
                                plannedPickupTime,
                                "--:--",
                                meta.basePlannedKm(),
                                0.0,
                                "No-Show",
                                0,
                                "PERSONAL_EMERGENCY_LEAVE",
                                true
                        ));
                    } else {
                        records.add(new TripActivityRecord(
                                tripId,
                                dateStr,
                                dowName,
                                "Remote",
                                "WFH",
                                "N/A",
                                "N/A",
                                "--:--",
                                "--:--",
                                0.0,
                                0.0,
                                "WFH / Approved Leave",
                                0,
                                "Pre-approved Remote Floor Support",
                                false
                        ));
                    }
                } else {
                    // Punctual on-time commute
                    double actualKm = Math.round((meta.basePlannedKm() + (day % 3 == 0 ? -0.2 : 0.1)) * 10.0) / 10.0;
                    records.add(new TripActivityRecord(
                            tripId,
                            dateStr,
                            dowName,
                            meta.defaultShift(),
                            "LOGIN",
                            meta.defaultRoute(),
                            meta.defaultCab(),
                            plannedPickupTime,
                            plannedPickupTime,
                            meta.basePlannedKm(),
                            actualKm,
                            "Boarded",
                            0,
                            "",
                            false
                    ));
                }
            }
        }
    }

    return records;
}

    private String addMinutes(String timeStr, int minutes) {
        try {
            String[] parts = timeStr.split(":");
            int h = Integer.parseInt(parts[0]);
            int m = Integer.parseInt(parts[1]) + minutes;
            if (m >= 60) {
                h = (h + m / 60) % 24;
                m = m % 60;
            }
            return String.format("%02d:%02d", h, m);
        } catch (Exception e) {
            return timeStr;
        }
    }

    public ActionResponse submitFeedback(FeedbackSubmitRequest request) {
        String ts = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        return new ActionResponse(
                true,
                "Feedback recorded successfully for Trip " + request.tripId() + "! Thank you for helping MoveInSync elevate commute quality.",
                "FDB-" + (System.currentTimeMillis() % 100000),
                ts
        );
    }

    public ActionResponse triggerSos(SosTriggerRequest request) {
        String ts = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String incidentId = "SOS-EMERGENCY-" + (System.currentTimeMillis() % 100000);
        return new ActionResponse(
                true,
                "EMERGENCY PROTOCOL ACTIVATED: Instant alert broadcast to MoveInSync 24/7 Security Control Room, Transport Desk, and Manager. Emergency vehicle dispatch initiated.",
                incidentId,
                ts
        );
    }
}
