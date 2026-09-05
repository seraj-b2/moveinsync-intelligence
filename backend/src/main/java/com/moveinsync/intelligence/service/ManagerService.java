package com.moveinsync.intelligence.service;

import com.moveinsync.intelligence.dto.ManagerDashboardResponse;
import com.moveinsync.intelligence.dto.ManagerDashboardResponse.ManagerProfile;
import com.moveinsync.intelligence.dto.ManagerDashboardResponse.ManagerShiftSummary;
import com.moveinsync.intelligence.dto.ManagerDashboardResponse.TeamCsatDetail;
import com.moveinsync.intelligence.dto.ManagerDashboardResponse.TeamMemberRosterDetail;
import com.moveinsync.intelligence.dto.ManagerDashboardResponse.TeamSafetyAlertDetail;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ManagerService {

    public List<ManagerProfile> getAllManagers(String companyName) {
        String company = (companyName == null || companyName.isBlank()) ? "catalyst-Sac" : companyName;
        return List.of(
                new ManagerProfile("MGR-101", "Vikram Malhotra", "Engineering Manager - Cloud & SRE Floor Ops", "Cloud Infrastructure & SRE", company, 6, "Fairview Commons"),
                new ManagerProfile("MGR-102", "Priya Sharma", "Engineering Manager - Digital Banking Platform", "Digital Banking & Frontend Systems", company, 5, "Fairview Commons"),
                new ManagerProfile("MGR-103", "Meenakshi Raman", "Operations Manager - 24/7 Global Escalations", "Global Tier-2 & NOC Incident Desk", company, 4, "Cedar Ridge Office")
        );
    }

    public ManagerDashboardResponse getManagerDashboard(String companyName, String managerId, String month) {
        String company = (companyName == null || companyName.isBlank()) ? "catalyst-Sac" : companyName;
        String activeManagerId = (managerId == null || managerId.isBlank()) ? "MGR-103" : managerId.toUpperCase().trim();
        String filterMonth = (month == null || month.isBlank() || month.equalsIgnoreCase("ALL")) ? "July 2026" : month;

        if ("MGR-103".equalsIgnoreCase(activeManagerId)) {
            // Meenakshi Raman — 24/7 Global Escalations & NOC Support
            List<TeamMemberRosterDetail> roster = List.of(
                    new TeamMemberRosterDetail("STW-718290", "Meenakshi Raman", "Support Lead", "MGR-103", "Cedar Ridge Office", "18:15", "Route 03", "KA-05-MK-3310", "ON_TIME", 0, 10.50, 10.20, false, "", "FEMALE"),
                    new TeamMemberRosterDetail("STW-829104", "Tanmay Sen", "Tier-2 Analyst", "MGR-103", "Cedar Ridge Office", "18:15", "Route 03", "KA-05-MK-3310", "ON_TIME", 0, 13.20, 13.00, false, "", "MALE"),
                    new TeamMemberRosterDetail("STW-930192", "Deepika Rao", "Escalation Desk", "MGR-103", "Fairview Commons", "18:15", "Route 11", "KA-01-NJ-7712", "DELAYED", 22, 19.80, 26.50, false, "SEVERE_TRAFFIC", "FEMALE"),
                    new TeamMemberRosterDetail("STW-104928", "Siddharth Paul", "NOC Engineer", "MGR-103", "Fairview Commons", "18:15", "Route 11", "KA-01-NJ-7712", "NO_SHOW", 0, 15.00, 0.00, true, "PERSONAL_EMERGENCY", "MALE")
            );

            List<TeamSafetyAlertDetail> safetyAlerts = List.of(
                    new TeamSafetyAlertDetail("ALT-SAF-6041", "EMPLOYEE_GEOFENCE_VIOLATION", "Sev-3", "STW-930192", "Deepika Rao", "KA-01-NJ-7712", false, "ACKNOWLEDGED", filterMonth + " 18:38", "Driver detoured 1.2km outside geofence corridor due to flyover construction")
            );

            TeamCsatDetail csat = new TeamCsatDetail(4.4, 4.5, 4.3, 4.1, 4.7, 4.5, 82);

            ManagerShiftSummary shift = new ManagerShiftSummary(
                    "SHIFT_1815",
                    "Shift 18:15 - Evening Support Handover",
                    "18:15 - 03:00",
                    4,
                    3,
                    1,
                    25.0,
                    75.0,
                    75.0,
                    roster,
                    safetyAlerts,
                    csat
            );

            return new ManagerDashboardResponse(
                    "MGR-103",
                    "Meenakshi Raman",
                    "Operations Manager - 24/7 Global Escalations",
                    "Global Tier-2 & NOC Incident Desk",
                    company,
                    "Cedar Ridge Office",
                    4,
                    75.0,
                    4.4,
                    List.of(shift)
            );
        } else if ("MGR-102".equalsIgnoreCase(activeManagerId)) {
            // Priya Sharma - Digital Banking Platform & Frontend Systems
            List<TeamMemberRosterDetail> roster = List.of(
                    new TeamMemberRosterDetail("STW-204918", "Karthik Iyer", "Principal Architect", "MGR-102", "Fairview Commons", "08:30", "Route 01", "KA-02-EA-1920", "ON_TIME", 0, 16.20, 15.90, false, "", "MALE"),
                    new TeamMemberRosterDetail("STW-309182", "Sneha Kulkarni", "Frontend Lead", "MGR-102", "Fairview Commons", "08:30", "Route 01", "KA-02-EA-1920", "ON_TIME", 0, 12.40, 12.10, false, "", "FEMALE"),
                    new TeamMemberRosterDetail("STW-401928", "Aditya Joshi", "Backend Engg", "MGR-102", "Santa Clara Office", "08:30", "Route 07", "KA-04-TR-8811", "DELAYED", 14, 18.90, 24.10, false, "ROAD_CLOSURE_DETOUR", "MALE"),
                    new TeamMemberRosterDetail("STW-510293", "Divya Menon", "QA Specialist", "MGR-102", "Santa Clara Office", "08:30", "Route 07", "KA-04-TR-8811", "ON_TIME", 3, 14.10, 14.50, false, "", "FEMALE"),
                    new TeamMemberRosterDetail("STW-619283", "Rohan Das", "Product Designer", "MGR-102", "Clearwater Campus", "08:30", "Route 15", "KA-01-PL-4490", "NO_SHOW", 0, 9.80, 0.00, true, "UNRESPONSIVE_CALL", "MALE")
            );

            List<TeamSafetyAlertDetail> safetyAlerts = List.of(
                    new TeamSafetyAlertDetail("ALT-SAF-7120", "OVER_SPEEDING", "Sev-2", "STW-401928", "Aditya Joshi (Route 07)", "KA-04-TR-8811", false, "RESOLVED", filterMonth + " 08:12", "Vehicle speed exceeded 65 km/h on Expressway; automated warning buzzer acknowledged")
            );

            TeamCsatDetail csat = new TeamCsatDetail(4.8, 4.9, 4.7, 4.6, 4.9, 4.7, 95);

            ManagerShiftSummary shift = new ManagerShiftSummary(
                    "SHIFT_0830",
                    "Shift 08:30 - Morning Core Engg",
                    "08:30 - 17:30",
                    5,
                    4,
                    1,
                    20.0,
                    80.0,
                    80.0,
                    roster,
                    safetyAlerts,
                    csat
            );

            return new ManagerDashboardResponse(
                    "MGR-102",
                    "Priya Sharma",
                    "Engineering Manager - Digital Banking Platform",
                    "Digital Banking & Frontend Systems",
                    company,
                    "Fairview Commons",
                    5,
                    80.0,
                    4.8,
                    List.of(shift)
            );
        } else {
            // MGR-101 (or default) - Vikram Malhotra (Cloud Infrastructure & SRE Floor Ops)
            List<TeamMemberRosterDetail> roster = List.of(
                    new TeamMemberRosterDetail("STW-484475", "Ananya Sharma", "Senior SRE", "MGR-101", "Fairview Commons", "21:30", "Route 12", "KA-01-MJ-4821", "ON_TIME", 0, 9.97, 9.33, false, "", "FEMALE"),
                    new TeamMemberRosterDetail("STW-332325", "Vikram Malhotra", "Team Lead", "MGR-101", "Fairview Commons", "21:30", "Route 12", "KA-01-MJ-4821", "ON_TIME", 2, 14.50, 14.80, false, "", "MALE"),
                    new TeamMemberRosterDetail("STW-194821", "Pooja Hegde", "Cloud Engineer", "MGR-101", "Cedar Ridge Office", "21:30", "Route 04", "KA-05-MB-1102", "DELAYED", 18, 22.10, 28.40, false, "TRAFFIC_BOTTLENECK", "FEMALE"),
                    new TeamMemberRosterDetail("STW-101925", "Rahul Verma", "DBA", "MGR-101", "Cedar Ridge Office", "21:30", "Route 04", "KA-05-MB-1102", "NO_SHOW", 0, 7.66, 0.00, true, "TRIP_CANCELLED_FROM_DASHBOARD", "MALE"),
                    new TeamMemberRosterDetail("STW-093335", "Neha Sundaram", "Security Analyst", "MGR-101", "Santa Clara Office", "21:30", "Route 09", "KA-03-MK-9910", "ON_TIME", 0, 11.20, 11.50, false, "", "FEMALE"),
                    new TeamMemberRosterDetail("STW-512401", "Arjun Nair", "Support Engg", "MGR-101", "Fairview Commons", "21:30", "Route 12", "KA-01-MJ-4821", "ON_TIME", 0, 8.40, 8.20, false, "", "MALE")
            );

            List<TeamSafetyAlertDetail> safetyAlerts = List.of(
                    new TeamSafetyAlertDetail("ALT-SAF-8901", "WOMAN_TRAVELLING_ALONE", "Sev-1", "STW-194821", "Pooja Hegde", "KA-05-MB-1102", true, "ACTIVE_ESCORT", filterMonth + " 21:52", "Live GPS Telemetry Tracking & Verified Security Escort Assigned"),
                    new TeamSafetyAlertDetail("ALT-SAF-8902", "FIRST_MALE_NO_SHOW", "Sev-2", "STW-101925", "Rahul Verma", "KA-05-MB-1102", false, "RESOLVED", filterMonth + " 21:15", "Roster updated; confirmed via automated SMS cancellation")
            );

            TeamCsatDetail csat = new TeamCsatDetail(4.6, 4.7, 4.4, 4.3, 4.9, 4.8, 128);

            ManagerShiftSummary shift = new ManagerShiftSummary(
                    "SHIFT_2130",
                    "Shift 21:30 - Night Floor Ops",
                    "21:30 - 06:00",
                    6,
                    5,
                    1,
                    16.7,
                    83.3,
                    83.3,
                    roster,
                    safetyAlerts,
                    csat
            );

            return new ManagerDashboardResponse(
                    "MGR-101",
                    "Vikram Malhotra",
                    "Engineering Manager - Cloud & SRE Floor Ops",
                    "Cloud Infrastructure & SRE",
                    company,
                    "Fairview Commons",
                    6,
                    83.3,
                    4.6,
                    List.of(shift)
            );
        }
    }
}
