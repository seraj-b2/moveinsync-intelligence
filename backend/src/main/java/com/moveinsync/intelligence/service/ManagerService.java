package com.moveinsync.intelligence.service;

import com.moveinsync.intelligence.dto.ManagerDashboardResponse;
import com.moveinsync.intelligence.dto.ManagerDashboardResponse.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ManagerService {

    public record EmpDirectoryItem(
            String stwid,
            String name,
            String role,
            String managerId,
            String office,
            String shiftTime,
            String routeId,
            String cabReg,
            String driverName,
            String driverPhone,
            String gender,
            double plannedKm
    ) {}

    private final Map<String, EmpDirectoryItem> empDirectory = new LinkedHashMap<>();
    private final Map<String, List<EmployeeDelayNotification>> managerNotificationsMap = new ConcurrentHashMap<>();

    public ManagerService() {
        initDirectory();
        initDefaultNotifications();
    }

    private void initDirectory() {
        // MGR-103: 4 direct reports
        empDirectory.put("STW-718290", new EmpDirectoryItem("STW-718290", "Meenakshi Raman", "Support Lead", "MGR-103", "Cedar Ridge Office", "18:15", "Route 03", "KA-05-MK-3310", "Manjunath K", "+91-98804-99881", "FEMALE", 10.50));
        empDirectory.put("STW-829104", new EmpDirectoryItem("STW-829104", "Tanmay Sen", "Tier-2 Analyst", "MGR-103", "Cedar Ridge Office", "18:15", "Route 03", "KA-05-MK-3310", "Manjunath K", "+91-98804-99881", "MALE", 13.20));
        empDirectory.put("STW-930192", new EmpDirectoryItem("STW-930192", "Deepika Rao", "Escalation Desk", "MGR-103", "Fairview Commons", "18:15", "Route 11", "KA-01-NJ-7712", "Anand Rao", "+91-98803-12490", "FEMALE", 19.80));
        empDirectory.put("STW-104928", new EmpDirectoryItem("STW-104928", "Siddharth Paul", "NOC Engineer", "MGR-103", "Fairview Commons", "18:15", "Route 11", "KA-01-NJ-7712", "Anand Rao", "+91-98803-12490", "MALE", 15.00));

        // MGR-102: 5 direct reports
        empDirectory.put("STW-204918", new EmpDirectoryItem("STW-204918", "Karthik Iyer", "Principal Architect", "MGR-102", "Fairview Commons", "08:30", "Route 01", "KA-02-EA-1920", "Raghavendra M", "+91-98806-11223", "MALE", 16.20));
        empDirectory.put("STW-309182", new EmpDirectoryItem("STW-309182", "Sneha Kulkarni", "Frontend Lead", "MGR-102", "Fairview Commons", "08:30", "Route 01", "KA-02-EA-1920", "Raghavendra M", "+91-98806-11223", "FEMALE", 12.40));
        empDirectory.put("STW-401928", new EmpDirectoryItem("STW-401928", "Aditya Joshi", "Backend Engg", "MGR-102", "Santa Clara Office", "08:30", "Route 07", "KA-04-TR-8811", "Kishore Kumar", "+91-98807-55667", "MALE", 18.90));
        empDirectory.put("STW-510293", new EmpDirectoryItem("STW-510293", "Divya Menon", "QA Specialist", "MGR-102", "Santa Clara Office", "08:30", "Route 07", "KA-04-TR-8811", "Kishore Kumar", "+91-98807-55667", "FEMALE", 14.10));
        empDirectory.put("STW-619283", new EmpDirectoryItem("STW-619283", "Rohan Das", "Product Designer", "MGR-102", "Clearwater Campus", "08:30", "Route 15", "KA-01-PL-4490", "Pradeep S", "+91-98808-99001", "MALE", 9.80));

        // MGR-101: 6 direct reports
        empDirectory.put("STW-484475", new EmpDirectoryItem("STW-484475", "Ananya Sharma", "Senior SRE", "MGR-101", "Fairview Commons", "21:30", "Route 12", "KA-01-MJ-4821", "Rajesh Kumar", "+91-98801-44321", "FEMALE", 9.97));
        empDirectory.put("STW-332325", new EmpDirectoryItem("STW-332325", "Vikram Malhotra", "Team Lead", "MGR-101", "Fairview Commons", "21:30", "Route 12", "KA-01-MJ-4821", "Rajesh Kumar", "+91-98801-44321", "MALE", 14.50));
        empDirectory.put("STW-194821", new EmpDirectoryItem("STW-194821", "Pooja Hegde", "Cloud Engineer", "MGR-101", "Cedar Ridge Office", "21:30", "Route 04", "KA-05-MB-1102", "Suresh Gowda", "+91-98802-77112", "FEMALE", 22.10));
        empDirectory.put("STW-101925", new EmpDirectoryItem("STW-101925", "Rahul Verma", "DBA", "MGR-101", "Cedar Ridge Office", "21:30", "Route 04", "KA-05-MB-1102", "Suresh Gowda", "+91-98802-77112", "MALE", 7.66));
        empDirectory.put("STW-093335", new EmpDirectoryItem("STW-093335", "Neha Sundaram", "Security Analyst", "MGR-101", "Santa Clara Office", "21:30", "Route 09", "KA-03-MK-9910", "Dinesh Verma", "+91-98805-33441", "FEMALE", 11.20));
        empDirectory.put("STW-512401", new EmpDirectoryItem("STW-512401", "Arjun Nair", "Support Engg", "MGR-101", "Fairview Commons", "21:30", "Route 12", "KA-01-MJ-4821", "Rajesh Kumar", "+91-98801-44321", "MALE", 8.40));
    }

    private void initDefaultNotifications() {
        // MGR-103: Deepika Rao delay notification
        List<EmployeeDelayNotification> mgr103Notifs = new ArrayList<>();
        mgr103Notifs.add(new EmployeeDelayNotification(
                "NOTIF-DLY-1031",
                "STW-930192",
                "Deepika Rao",
                "Global Escalation Specialist",
                "18:15",
                "Route 11",
                "KA-01-NJ-7712",
                "Anand Rao",
                "+91-98803-12490",
                22,
                "Severe corridor bottleneck on Outer Ring Road Flyover due to ongoing metro expansion construction.",
                "CRITICAL",
                "meenakshi.raman@catalyst.com",
                "[URGENT COMMUTE DELAY ALERT] Direct Report Deepika Rao (STW-930192) Delayed +22m",
                "Dear Meenakshi Raman,\n\nMoveInSync Telemetry Alert: Your direct report Deepika Rao (Global Escalation Specialist, STW-930192) has encountered a transit delay on Route 11 (Cab KA-01-NJ-7712).\n\n• Delay Duration: +22 minutes\n• Root Cause / Issue: Severe corridor bottleneck on Outer Ring Road Flyover due to ongoing metro expansion construction.\n• Assigned Driver: Anand Rao (+91-98803-12490)\n• Planned Pickup: 17:35 | Actual Pickup: 17:57\n• Expected Floor Arrival: 18:37 (Scheduled Shift Login: 18:15)\n• Shift Coverage Impact: Floor handover grace period activated (+15m).\n\nCentral Dispatch is monitoring telemetry. An automatic update will be pushed when rider confirms boarding OTP.\n\nRegards,\nMoveInSync Autonomous Mobility Dispatch Desk",
                "Today, September 5, 2026 - 17:58 IST",
                "DELIVERED_TO_INBOX",
                false
        ));
        managerNotificationsMap.put("MGR-103", mgr103Notifs);

        // MGR-101: Pooja Hegde delay notification
        List<EmployeeDelayNotification> mgr101Notifs = new ArrayList<>();
        mgr101Notifs.add(new EmployeeDelayNotification(
                "NOTIF-DLY-1011",
                "STW-194821",
                "Pooja Hegde",
                "Cloud Infrastructure Engineer",
                "21:30",
                "Route 04",
                "KA-05-MB-1102",
                "Suresh Gowda",
                "+91-98802-77112",
                18,
                "Heavy traffic gridlock at Koramangala 4th Block signal; cab delayed at pickup node.",
                "CRITICAL",
                "vikram.malhotra@catalyst.com",
                "[URGENT COMMUTE DELAY ALERT] Direct Report Pooja Hegde (STW-194821) Delayed +18m",
                "Dear Vikram Malhotra,\n\nMoveInSync Telemetry Alert: Your direct report Pooja Hegde (Cloud Infrastructure Engineer, STW-194821) has encountered an unexpected transit delay on Route 04 (Cab KA-05-MB-1102).\n\n• Delay Duration: +18 minutes\n• Root Cause / Issue: Heavy traffic gridlock at Koramangala 4th Block signal; cab delayed at pickup node.\n• Assigned Driver: Suresh Gowda (+91-98802-77112)\n• Scheduled Pickup: 20:50 | Actual Pickup: 21:08\n• Expected Floor Arrival: 21:48 (Scheduled Shift Login: 21:30)\n• Duty of Care: Woman Traveling Alone protocol active. Security escort onboard verified.\n\nRegards,\nMoveInSync Autonomous Mobility Dispatch Desk",
                "Today, September 5, 2026 - 21:10 IST",
                "DELIVERED_TO_INBOX",
                false
        ));
        managerNotificationsMap.put("MGR-101", mgr101Notifs);

        // MGR-102: Aditya Joshi delay notification
        List<EmployeeDelayNotification> mgr102Notifs = new ArrayList<>();
        mgr102Notifs.add(new EmployeeDelayNotification(
                "NOTIF-DLY-1021",
                "STW-401928",
                "Aditya Joshi",
                "Senior Backend Engineer",
                "08:30",
                "Route 07",
                "KA-04-TR-8811",
                "Kishore Kumar",
                "+91-98807-55667",
                14,
                "Unplanned road closure and pipe laying diversion on Whitefield Main Road corridor.",
                "WARNING",
                "priya.sharma@catalyst.com",
                "[COMMUTE DELAY ALERT] Direct Report Aditya Joshi (STW-401928) Delayed +14m",
                "Dear Priya Sharma,\n\nMoveInSync Telemetry Alert: Your direct report Aditya Joshi (Senior Backend Engineer, STW-401928) has reported a 14-minute commute delay on Route 07 (Cab KA-04-TR-8811).\n\n• Delay Duration: +14 minutes\n• Root Cause / Issue: Unplanned road closure and pipe laying diversion on Whitefield Main Road corridor.\n• Assigned Driver: Kishore Kumar (+91-98807-55667)\n• Scheduled Pickup: 07:50 | Actual Pickup: 08:04\n• Expected Shift Arrival: 08:44 (Scheduled Shift Login: 08:30)\n\nRegards,\nMoveInSync Autonomous Mobility Dispatch Desk",
                "Today, September 5, 2026 - 08:06 IST",
                "DELIVERED_TO_INBOX",
                false
        ));
        managerNotificationsMap.put("MGR-102", mgr102Notifs);
    }

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
        String filterMonth = (month == null || month.isBlank()) ? "July 2026" : month;

        List<EmployeeDelayNotification> delayNotifications = managerNotificationsMap.getOrDefault(activeManagerId, Collections.emptyList());

        if ("MGR-103".equalsIgnoreCase(activeManagerId)) {
            return buildMgr103Dashboard(company, filterMonth, delayNotifications);
        } else if ("MGR-102".equalsIgnoreCase(activeManagerId)) {
            return buildMgr102Dashboard(company, filterMonth, delayNotifications);
        } else {
            return buildMgr101Dashboard(company, filterMonth, delayNotifications);
        }
    }

    private ManagerDashboardResponse buildMgr103Dashboard(String company, String month, List<EmployeeDelayNotification> delayNotifications) {
        List<TeamMemberRosterDetail> roster = new ArrayList<>();
        List<TeamSafetyAlertDetail> safetyAlerts = new ArrayList<>();
        TeamCsatDetail csat;

        String mLower = month.toLowerCase();
        if (mLower.contains("june")) {
            // June 2026 - High performance
            roster.add(new TeamMemberRosterDetail("STW-718290", "Meenakshi Raman", "Support Lead", "MGR-103", "Cedar Ridge Office", "18:15", "Route 03", "KA-05-MK-3310", "ON_TIME", 0, 10.50, 10.40, false, "", "FEMALE"));
            roster.add(new TeamMemberRosterDetail("STW-829104", "Tanmay Sen", "Tier-2 Analyst", "MGR-103", "Cedar Ridge Office", "18:15", "Route 03", "KA-05-MK-3310", "ON_TIME", 0, 13.20, 13.10, false, "", "MALE"));
            roster.add(new TeamMemberRosterDetail("STW-930192", "Deepika Rao", "Escalation Desk", "MGR-103", "Fairview Commons", "18:15", "Route 11", "KA-01-NJ-7712", "ON_TIME", 2, 19.80, 20.10, false, "", "FEMALE"));
            roster.add(new TeamMemberRosterDetail("STW-104928", "Siddharth Paul", "NOC Engineer", "MGR-103", "Fairview Commons", "18:15", "Route 11", "KA-01-NJ-7712", "ON_TIME", 0, 15.00, 14.80, false, "", "MALE"));
            csat = new TeamCsatDetail(4.8, 4.9, 4.7, 4.6, 4.9, 4.8, 88);
        } else if (mLower.contains("may")) {
            // May 2026 - Moderate traffic delay
            roster.add(new TeamMemberRosterDetail("STW-718290", "Meenakshi Raman", "Support Lead", "MGR-103", "Cedar Ridge Office", "18:15", "Route 03", "KA-05-MK-3310", "ON_TIME", 0, 10.50, 10.20, false, "", "FEMALE"));
            roster.add(new TeamMemberRosterDetail("STW-829104", "Tanmay Sen", "Tier-2 Analyst", "MGR-103", "Cedar Ridge Office", "18:15", "Route 03", "KA-05-MK-3310", "DELAYED", 8, 13.20, 15.40, false, "CORRIDOR_ROADWORKS", "MALE"));
            roster.add(new TeamMemberRosterDetail("STW-930192", "Deepika Rao", "Escalation Desk", "MGR-103", "Fairview Commons", "18:15", "Route 11", "KA-01-NJ-7712", "ON_TIME", 0, 19.80, 19.60, false, "", "FEMALE"));
            roster.add(new TeamMemberRosterDetail("STW-104928", "Siddharth Paul", "NOC Engineer", "MGR-103", "Fairview Commons", "18:15", "Route 11", "KA-01-NJ-7712", "ON_TIME", 0, 15.00, 15.10, false, "", "MALE"));
            csat = new TeamCsatDetail(4.6, 4.6, 4.5, 4.4, 4.8, 4.7, 85);
        } else if (mLower.contains("all")) {
            // All Months Aggregated
            roster.add(new TeamMemberRosterDetail("STW-718290", "Meenakshi Raman", "Support Lead", "MGR-103", "Cedar Ridge Office", "18:15", "Route 03", "KA-05-MK-3310", "ON_TIME", 0, 10.50, 10.30, false, "99.2% Historical On-Time", "FEMALE"));
            roster.add(new TeamMemberRosterDetail("STW-829104", "Tanmay Sen", "Tier-2 Analyst", "MGR-103", "Cedar Ridge Office", "18:15", "Route 03", "KA-05-MK-3310", "ON_TIME", 2, 13.20, 13.50, false, "96.4% Historical On-Time", "MALE"));
            roster.add(new TeamMemberRosterDetail("STW-930192", "Deepika Rao", "Escalation Desk", "MGR-103", "Fairview Commons", "18:15", "Route 11", "KA-01-NJ-7712", "DELAYED", 11, 19.80, 22.10, false, "91.8% Historical On-Time", "FEMALE"));
            roster.add(new TeamMemberRosterDetail("STW-104928", "Siddharth Paul", "NOC Engineer", "MGR-103", "Fairview Commons", "18:15", "Route 11", "KA-01-NJ-7712", "ON_TIME", 1, 15.00, 15.00, false, "94.1% Historical On-Time", "MALE"));
            safetyAlerts.add(new TeamSafetyAlertDetail("ALT-SAF-6041", "EMPLOYEE_GEOFENCE_VIOLATION", "Sev-3", "STW-930192", "Deepika Rao", "KA-01-NJ-7712", false, "ACKNOWLEDGED", month + " 18:38", "Driver detoured 1.2km outside geofence corridor due to flyover construction"));
            csat = new TeamCsatDetail(4.6, 4.7, 4.5, 4.4, 4.8, 4.7, 255);
        } else {
            // July 2026 (Current Active)
            roster.add(new TeamMemberRosterDetail("STW-718290", "Meenakshi Raman", "Support Lead", "MGR-103", "Cedar Ridge Office", "18:15", "Route 03", "KA-05-MK-3310", "ON_TIME", 0, 10.50, 10.20, false, "", "FEMALE"));
            roster.add(new TeamMemberRosterDetail("STW-829104", "Tanmay Sen", "Tier-2 Analyst", "MGR-103", "Cedar Ridge Office", "18:15", "Route 03", "KA-05-MK-3310", "ON_TIME", 0, 13.20, 13.00, false, "", "MALE"));
            roster.add(new TeamMemberRosterDetail("STW-930192", "Deepika Rao", "Escalation Desk", "MGR-103", "Fairview Commons", "18:15", "Route 11", "KA-01-NJ-7712", "DELAYED", 22, 19.80, 26.50, false, "SEVERE_TRAFFIC", "FEMALE"));
            roster.add(new TeamMemberRosterDetail("STW-104928", "Siddharth Paul", "NOC Engineer", "MGR-103", "Fairview Commons", "18:15", "Route 11", "KA-01-NJ-7712", "NO_SHOW", 0, 15.00, 0.00, true, "PERSONAL_EMERGENCY", "MALE"));
            safetyAlerts.add(new TeamSafetyAlertDetail("ALT-SAF-6041", "EMPLOYEE_GEOFENCE_VIOLATION", "Sev-3", "STW-930192", "Deepika Rao", "KA-01-NJ-7712", false, "ACKNOWLEDGED", month + " 18:38", "Driver detoured 1.2km outside geofence corridor due to flyover construction"));
            csat = new TeamCsatDetail(4.4, 4.5, 4.3, 4.1, 4.7, 4.5, 82);
        }

        // Overlay active delay notifications into roster if any
        overlayActiveDelayNotifications(roster, delayNotifications);

        ManagerShiftSummary shift = buildShiftSummary(
                "SHIFT_1815",
                "Shift 18:15 - Evening Support Handover",
                "18:15 - 03:00",
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
                roster.size(),
                shift.shiftReadinessIndex(),
                shift.csatBreakdown().overallCsat(),
                List.of(shift),
                delayNotifications
        );
    }

    private ManagerDashboardResponse buildMgr102Dashboard(String company, String month, List<EmployeeDelayNotification> delayNotifications) {
        List<TeamMemberRosterDetail> roster = new ArrayList<>();
        List<TeamSafetyAlertDetail> safetyAlerts = new ArrayList<>();
        TeamCsatDetail csat;

        String mLower = month.toLowerCase();
        if (mLower.contains("june")) {
            // June 2026
            roster.add(new TeamMemberRosterDetail("STW-204918", "Karthik Iyer", "Principal Architect", "MGR-102", "Fairview Commons", "08:30", "Route 01", "KA-02-EA-1920", "ON_TIME", 0, 16.20, 16.00, false, "", "MALE"));
            roster.add(new TeamMemberRosterDetail("STW-309182", "Sneha Kulkarni", "Frontend Lead", "MGR-102", "Fairview Commons", "08:30", "Route 01", "KA-02-EA-1920", "ON_TIME", 0, 12.40, 12.20, false, "", "FEMALE"));
            roster.add(new TeamMemberRosterDetail("STW-401928", "Aditya Joshi", "Backend Engg", "MGR-102", "Santa Clara Office", "08:30", "Route 07", "KA-04-TR-8811", "ON_TIME", 0, 18.90, 18.80, false, "", "MALE"));
            roster.add(new TeamMemberRosterDetail("STW-510293", "Divya Menon", "QA Specialist", "MGR-102", "Santa Clara Office", "08:30", "Route 07", "KA-04-TR-8811", "ON_TIME", 0, 14.10, 14.20, false, "", "FEMALE"));
            roster.add(new TeamMemberRosterDetail("STW-619283", "Rohan Das", "Product Designer", "MGR-102", "Clearwater Campus", "08:30", "Route 15", "KA-01-PL-4490", "ON_TIME", 3, 9.80, 10.00, false, "", "MALE"));
            csat = new TeamCsatDetail(4.9, 5.0, 4.8, 4.8, 5.0, 4.9, 102);
        } else if (mLower.contains("may")) {
            // May 2026
            roster.add(new TeamMemberRosterDetail("STW-204918", "Karthik Iyer", "Principal Architect", "MGR-102", "Fairview Commons", "08:30", "Route 01", "KA-02-EA-1920", "ON_TIME", 0, 16.20, 16.10, false, "", "MALE"));
            roster.add(new TeamMemberRosterDetail("STW-309182", "Sneha Kulkarni", "Frontend Lead", "MGR-102", "Fairview Commons", "08:30", "Route 01", "KA-02-EA-1920", "ON_TIME", 0, 12.40, 12.00, false, "", "FEMALE"));
            roster.add(new TeamMemberRosterDetail("STW-401928", "Aditya Joshi", "Backend Engg", "MGR-102", "Santa Clara Office", "08:30", "Route 07", "KA-04-TR-8811", "ON_TIME", 0, 18.90, 19.00, false, "", "MALE"));
            roster.add(new TeamMemberRosterDetail("STW-510293", "Divya Menon", "QA Specialist", "MGR-102", "Santa Clara Office", "08:30", "Route 07", "KA-04-TR-8811", "DELAYED", 9, 14.10, 16.50, false, "FLYOVER_CONSTRUCTION", "FEMALE"));
            roster.add(new TeamMemberRosterDetail("STW-619283", "Rohan Das", "Product Designer", "MGR-102", "Clearwater Campus", "08:30", "Route 15", "KA-01-PL-4490", "ON_TIME", 0, 9.80, 9.70, false, "", "MALE"));
            csat = new TeamCsatDetail(4.7, 4.8, 4.6, 4.5, 4.8, 4.7, 98);
        } else if (mLower.contains("all")) {
            // All Months
            roster.add(new TeamMemberRosterDetail("STW-204918", "Karthik Iyer", "Principal Architect", "MGR-102", "Fairview Commons", "08:30", "Route 01", "KA-02-EA-1920", "ON_TIME", 0, 16.20, 16.00, false, "98.8% Historical On-Time", "MALE"));
            roster.add(new TeamMemberRosterDetail("STW-309182", "Sneha Kulkarni", "Frontend Lead", "MGR-102", "Fairview Commons", "08:30", "Route 01", "KA-02-EA-1920", "ON_TIME", 0, 12.40, 12.10, false, "99.1% Historical On-Time", "FEMALE"));
            roster.add(new TeamMemberRosterDetail("STW-401928", "Aditya Joshi", "Backend Engg", "MGR-102", "Santa Clara Office", "08:30", "Route 07", "KA-04-TR-8811", "DELAYED", 6, 18.90, 20.20, false, "93.5% Historical On-Time", "MALE"));
            roster.add(new TeamMemberRosterDetail("STW-510293", "Divya Menon", "QA Specialist", "MGR-102", "Santa Clara Office", "08:30", "Route 07", "KA-04-TR-8811", "ON_TIME", 4, 14.10, 14.80, false, "95.2% Historical On-Time", "FEMALE"));
            roster.add(new TeamMemberRosterDetail("STW-619283", "Rohan Das", "Product Designer", "MGR-102", "Clearwater Campus", "08:30", "Route 15", "KA-01-PL-4490", "ON_TIME", 1, 9.80, 9.90, false, "94.8% Historical On-Time", "MALE"));
            safetyAlerts.add(new TeamSafetyAlertDetail("ALT-SAF-7120", "OVER_SPEEDING", "Sev-2", "STW-401928", "Aditya Joshi (Route 07)", "KA-04-TR-8811", false, "RESOLVED", month + " 08:12", "Vehicle speed exceeded 65 km/h on Expressway; automated warning buzzer acknowledged"));
            csat = new TeamCsatDetail(4.8, 4.9, 4.7, 4.6, 4.9, 4.8, 295);
        } else {
            // July 2026 (Current Active)
            roster.add(new TeamMemberRosterDetail("STW-204918", "Karthik Iyer", "Principal Architect", "MGR-102", "Fairview Commons", "08:30", "Route 01", "KA-02-EA-1920", "ON_TIME", 0, 16.20, 15.90, false, "", "MALE"));
            roster.add(new TeamMemberRosterDetail("STW-309182", "Sneha Kulkarni", "Frontend Lead", "MGR-102", "Fairview Commons", "08:30", "Route 01", "KA-02-EA-1920", "ON_TIME", 0, 12.40, 12.10, false, "", "FEMALE"));
            roster.add(new TeamMemberRosterDetail("STW-401928", "Aditya Joshi", "Backend Engg", "MGR-102", "Santa Clara Office", "08:30", "Route 07", "KA-04-TR-8811", "DELAYED", 14, 18.90, 24.10, false, "ROAD_CLOSURE_DETOUR", "MALE"));
            roster.add(new TeamMemberRosterDetail("STW-510293", "Divya Menon", "QA Specialist", "MGR-102", "Santa Clara Office", "08:30", "Route 07", "KA-04-TR-8811", "ON_TIME", 3, 14.10, 14.50, false, "", "FEMALE"));
            roster.add(new TeamMemberRosterDetail("STW-619283", "Rohan Das", "Product Designer", "MGR-102", "Clearwater Campus", "08:30", "Route 15", "KA-01-PL-4490", "NO_SHOW", 0, 9.80, 0.00, true, "UNRESPONSIVE_CALL", "MALE"));
            safetyAlerts.add(new TeamSafetyAlertDetail("ALT-SAF-7120", "OVER_SPEEDING", "Sev-2", "STW-401928", "Aditya Joshi (Route 07)", "KA-04-TR-8811", false, "RESOLVED", month + " 08:12", "Vehicle speed exceeded 65 km/h on Expressway; automated warning buzzer acknowledged"));
            csat = new TeamCsatDetail(4.8, 4.9, 4.7, 4.6, 4.9, 4.7, 95);
        }

        overlayActiveDelayNotifications(roster, delayNotifications);

        ManagerShiftSummary shift = buildShiftSummary(
                "SHIFT_0830",
                "Shift 08:30 - Morning Core Engg",
                "08:30 - 17:30",
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
                roster.size(),
                shift.shiftReadinessIndex(),
                shift.csatBreakdown().overallCsat(),
                List.of(shift),
                delayNotifications
        );
    }

    private ManagerDashboardResponse buildMgr101Dashboard(String company, String month, List<EmployeeDelayNotification> delayNotifications) {
        List<TeamMemberRosterDetail> roster = new ArrayList<>();
        List<TeamSafetyAlertDetail> safetyAlerts = new ArrayList<>();
        TeamCsatDetail csat;

        String mLower = month.toLowerCase();
        if (mLower.contains("june")) {
            // June 2026
            roster.add(new TeamMemberRosterDetail("STW-484475", "Ananya Sharma", "Senior SRE", "MGR-101", "Fairview Commons", "21:30", "Route 12", "KA-01-MJ-4821", "ON_TIME", 0, 9.97, 9.50, false, "", "FEMALE"));
            roster.add(new TeamMemberRosterDetail("STW-332325", "Vikram Malhotra", "Team Lead", "MGR-101", "Fairview Commons", "21:30", "Route 12", "KA-01-MJ-4821", "ON_TIME", 0, 14.50, 14.30, false, "", "MALE"));
            roster.add(new TeamMemberRosterDetail("STW-194821", "Pooja Hegde", "Cloud Engineer", "MGR-101", "Cedar Ridge Office", "21:30", "Route 04", "KA-05-MB-1102", "ON_TIME", 0, 22.10, 22.00, false, "", "FEMALE"));
            roster.add(new TeamMemberRosterDetail("STW-101925", "Rahul Verma", "DBA", "MGR-101", "Cedar Ridge Office", "21:30", "Route 04", "KA-05-MB-1102", "ON_TIME", 0, 7.66, 7.50, false, "", "MALE"));
            roster.add(new TeamMemberRosterDetail("STW-093335", "Neha Sundaram", "Security Analyst", "MGR-101", "Santa Clara Office", "21:30", "Route 09", "KA-03-MK-9910", "ON_TIME", 0, 11.20, 11.30, false, "", "FEMALE"));
            roster.add(new TeamMemberRosterDetail("STW-512401", "Arjun Nair", "Support Engg", "MGR-101", "Fairview Commons", "21:30", "Route 12", "KA-01-MJ-4821", "ON_TIME", 0, 8.40, 8.30, false, "", "MALE"));
            safetyAlerts.add(new TeamSafetyAlertDetail("ALT-SAF-8901", "WOMAN_TRAVELLING_ALONE", "Sev-1", "STW-194821", "Pooja Hegde", "KA-05-MB-1102", true, "ACTIVE_ESCORT", month + " 21:52", "Live GPS Telemetry Tracking & Verified Security Escort Assigned"));
            csat = new TeamCsatDetail(4.85, 4.9, 4.7, 4.6, 5.0, 4.9, 135);
        } else if (mLower.contains("may")) {
            // May 2026
            roster.add(new TeamMemberRosterDetail("STW-484475", "Ananya Sharma", "Senior SRE", "MGR-101", "Fairview Commons", "21:30", "Route 12", "KA-01-MJ-4821", "ON_TIME", 0, 9.97, 9.80, false, "", "FEMALE"));
            roster.add(new TeamMemberRosterDetail("STW-332325", "Vikram Malhotra", "Team Lead", "MGR-101", "Fairview Commons", "21:30", "Route 12", "KA-01-MJ-4821", "ON_TIME", 0, 14.50, 14.60, false, "", "MALE"));
            roster.add(new TeamMemberRosterDetail("STW-194821", "Pooja Hegde", "Cloud Engineer", "MGR-101", "Cedar Ridge Office", "21:30", "Route 04", "KA-05-MB-1102", "ON_TIME", 0, 22.10, 22.40, false, "", "FEMALE"));
            roster.add(new TeamMemberRosterDetail("STW-101925", "Rahul Verma", "DBA", "MGR-101", "Cedar Ridge Office", "21:30", "Route 04", "KA-05-MB-1102", "ON_TIME", 0, 7.66, 7.60, false, "", "MALE"));
            roster.add(new TeamMemberRosterDetail("STW-093335", "Neha Sundaram", "Security Analyst", "MGR-101", "Santa Clara Office", "21:30", "Route 09", "KA-03-MK-9910", "ON_TIME", 0, 11.20, 11.10, false, "", "FEMALE"));
            roster.add(new TeamMemberRosterDetail("STW-512401", "Arjun Nair", "Support Engg", "MGR-101", "Fairview Commons", "21:30", "Route 12", "KA-01-MJ-4821", "DELAYED", 7, 8.40, 10.10, false, "TRAFFIC_AT_SILKBOARD", "MALE"));
            safetyAlerts.add(new TeamSafetyAlertDetail("ALT-SAF-8901", "WOMAN_TRAVELLING_ALONE", "Sev-1", "STW-194821", "Pooja Hegde", "KA-05-MB-1102", true, "ACTIVE_ESCORT", month + " 21:52", "Live GPS Telemetry Tracking & Verified Security Escort Assigned"));
            csat = new TeamCsatDetail(4.7, 4.8, 4.5, 4.4, 4.9, 4.8, 130);
        } else if (mLower.contains("all")) {
            // All Months
            roster.add(new TeamMemberRosterDetail("STW-484475", "Ananya Sharma", "Senior SRE", "MGR-101", "Fairview Commons", "21:30", "Route 12", "KA-01-MJ-4821", "ON_TIME", 0, 9.97, 9.40, false, "99.4% Historical On-Time", "FEMALE"));
            roster.add(new TeamMemberRosterDetail("STW-332325", "Vikram Malhotra", "Team Lead", "MGR-101", "Fairview Commons", "21:30", "Route 12", "KA-01-MJ-4821", "ON_TIME", 1, 14.50, 14.70, false, "98.2% Historical On-Time", "MALE"));
            roster.add(new TeamMemberRosterDetail("STW-194821", "Pooja Hegde", "Cloud Engineer", "MGR-101", "Cedar Ridge Office", "21:30", "Route 04", "KA-05-MB-1102", "DELAYED", 9, 22.10, 24.50, false, "92.6% Historical On-Time", "FEMALE"));
            roster.add(new TeamMemberRosterDetail("STW-101925", "Rahul Verma", "DBA", "MGR-101", "Cedar Ridge Office", "21:30", "Route 04", "KA-05-MB-1102", "ON_TIME", 0, 7.66, 7.60, false, "96.5% Historical On-Time", "MALE"));
            roster.add(new TeamMemberRosterDetail("STW-093335", "Neha Sundaram", "Security Analyst", "MGR-101", "Santa Clara Office", "21:30", "Route 09", "KA-03-MK-9910", "ON_TIME", 0, 11.20, 11.30, false, "98.0% Historical On-Time", "FEMALE"));
            roster.add(new TeamMemberRosterDetail("STW-512401", "Arjun Nair", "Support Engg", "MGR-101", "Fairview Commons", "21:30", "Route 12", "KA-01-MJ-4821", "ON_TIME", 2, 8.40, 8.80, false, "97.1% Historical On-Time", "MALE"));
            safetyAlerts.add(new TeamSafetyAlertDetail("ALT-SAF-8901", "WOMAN_TRAVELLING_ALONE", "Sev-1", "STW-194821", "Pooja Hegde", "KA-05-MB-1102", true, "ACTIVE_ESCORT", month + " 21:52", "Live GPS Telemetry Tracking & Verified Security Escort Assigned"));
            csat = new TeamCsatDetail(4.72, 4.8, 4.5, 4.4, 4.9, 4.85, 393);
        } else {
            // July 2026 (Current Active)
            roster.add(new TeamMemberRosterDetail("STW-484475", "Ananya Sharma", "Senior SRE", "MGR-101", "Fairview Commons", "21:30", "Route 12", "KA-01-MJ-4821", "ON_TIME", 0, 9.97, 9.33, false, "", "FEMALE"));
            roster.add(new TeamMemberRosterDetail("STW-332325", "Vikram Malhotra", "Team Lead", "MGR-101", "Fairview Commons", "21:30", "Route 12", "KA-01-MJ-4821", "ON_TIME", 2, 14.50, 14.80, false, "", "MALE"));
            roster.add(new TeamMemberRosterDetail("STW-194821", "Pooja Hegde", "Cloud Engineer", "MGR-101", "Cedar Ridge Office", "21:30", "Route 04", "KA-05-MB-1102", "DELAYED", 18, 22.10, 28.40, false, "TRAFFIC_BOTTLENECK", "FEMALE"));
            roster.add(new TeamMemberRosterDetail("STW-101925", "Rahul Verma", "DBA", "MGR-101", "Cedar Ridge Office", "21:30", "Route 04", "KA-05-MB-1102", "NO_SHOW", 0, 7.66, 0.00, true, "TRIP_CANCELLED_FROM_DASHBOARD", "MALE"));
            roster.add(new TeamMemberRosterDetail("STW-093335", "Neha Sundaram", "Security Analyst", "MGR-101", "Santa Clara Office", "21:30", "Route 09", "KA-03-MK-9910", "ON_TIME", 0, 11.20, 11.50, false, "", "FEMALE"));
            roster.add(new TeamMemberRosterDetail("STW-512401", "Arjun Nair", "Support Engg", "MGR-101", "Fairview Commons", "21:30", "Route 12", "KA-01-MJ-4821", "ON_TIME", 0, 8.40, 8.20, false, "", "MALE"));
            safetyAlerts.add(new TeamSafetyAlertDetail("ALT-SAF-8901", "WOMAN_TRAVELLING_ALONE", "Sev-1", "STW-194821", "Pooja Hegde", "KA-05-MB-1102", true, "ACTIVE_ESCORT", month + " 21:52", "Live GPS Telemetry Tracking & Verified Security Escort Assigned"));
            safetyAlerts.add(new TeamSafetyAlertDetail("ALT-SAF-8902", "FIRST_MALE_NO_SHOW", "Sev-2", "STW-101925", "Rahul Verma", "KA-05-MB-1102", false, "RESOLVED", month + " 21:15", "Roster updated; confirmed via automated SMS cancellation"));
            csat = new TeamCsatDetail(4.6, 4.7, 4.4, 4.3, 4.9, 4.8, 128);
        }

        overlayActiveDelayNotifications(roster, delayNotifications);

        ManagerShiftSummary shift = buildShiftSummary(
                "SHIFT_2130",
                "Shift 21:30 - Night Floor Ops",
                "21:30 - 06:00",
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
                roster.size(),
                shift.shiftReadinessIndex(),
                shift.csatBreakdown().overallCsat(),
                List.of(shift),
                delayNotifications
        );
    }

    private void overlayActiveDelayNotifications(List<TeamMemberRosterDetail> roster, List<EmployeeDelayNotification> delayNotifications) {
        if (delayNotifications == null || delayNotifications.isEmpty()) return;

        for (EmployeeDelayNotification notif : delayNotifications) {
            for (int i = 0; i < roster.size(); i++) {
                TeamMemberRosterDetail member = roster.get(i);
                if (member.stwid().equalsIgnoreCase(notif.stwid())) {
                    // Update member with latest active telemetry delay
                    TeamMemberRosterDetail updated = new TeamMemberRosterDetail(
                            member.stwid(),
                            member.employeeName(),
                            member.role(),
                            member.managerId(),
                            member.office(),
                            member.shiftTime(),
                            notif.routeId(),
                            notif.cabReg(),
                            "DELAYED",
                            (int) notif.delayMinutes(),
                            member.plannedKm(),
                            Math.round((member.plannedKm() + (notif.delayMinutes() * 0.35)) * 10.0) / 10.0,
                            false,
                            notif.delayReason(),
                            member.gender()
                    );
                    roster.set(i, updated);
                    break;
                }
            }
        }
    }

    private ManagerShiftSummary buildShiftSummary(
            String shiftId,
            String shiftName,
            String timeWindow,
            List<TeamMemberRosterDetail> roster,
            List<TeamSafetyAlertDetail> safetyAlerts,
            TeamCsatDetail csatBreakdown
    ) {
        int total = roster.size();
        int noShowCount = (int) roster.stream().filter(TeamMemberRosterDetail::isNoShow).count();
        int onTimeCount = (int) roster.stream().filter(r -> "ON_TIME".equalsIgnoreCase(r.pickupStatus()) && !r.isNoShow()).count();
        int delayedCount = (int) roster.stream().filter(r -> "DELAYED".equalsIgnoreCase(r.pickupStatus()) && !r.isNoShow()).count();
        int boardedCount = total - noShowCount;

        double noShowRate = total > 0 ? Math.round(((double) noShowCount / total) * 1000.0) / 10.0 : 0.0;
        double onTimeRate = total > 0 ? Math.round(((double) onTimeCount / total) * 1000.0) / 10.0 : 0.0;
        // Shift Readiness: 100% for on-time, 60% partial readiness for delayed arrival, 0% for no-shows
        double readiness = total > 0 ? Math.round(((onTimeCount * 1.0 + delayedCount * 0.6) / total) * 1000.0) / 10.0 : 100.0;

        return new ManagerShiftSummary(
                shiftId,
                shiftName,
                timeWindow,
                total,
                boardedCount,
                noShowCount,
                noShowRate,
                onTimeRate,
                readiness,
                roster,
                safetyAlerts,
                csatBreakdown
        );
    }

    public boolean acknowledgeNotification(String managerId, String notificationId) {
        List<EmployeeDelayNotification> notifs = managerNotificationsMap.get(managerId);
        if (notifs == null) return false;

        for (int i = 0; i < notifs.size(); i++) {
            EmployeeDelayNotification current = notifs.get(i);
            if (current.notificationId().equalsIgnoreCase(notificationId)) {
                EmployeeDelayNotification updated = new EmployeeDelayNotification(
                        current.notificationId(),
                        current.stwid(),
                        current.employeeName(),
                        current.role(),
                        current.shiftTime(),
                        current.routeId(),
                        current.cabReg(),
                        current.driverName(),
                        current.driverPhone(),
                        current.delayMinutes(),
                        current.delayReason(),
                        current.severity(),
                        current.emailRecipient(),
                        current.emailSubject(),
                        current.emailBody(),
                        current.emailDispatchedAt(),
                        current.emailDeliveryStatus(),
                        true
                );
                notifs.set(i, updated);
                return true;
            }
        }
        return false;
    }

    public EmployeeDelayNotification simulateDelay(String managerId, SimulateDelayRequest req) {
        String mgrId = (managerId == null || managerId.isBlank()) ? "MGR-103" : managerId.toUpperCase().trim();
        List<EmployeeDelayNotification> notifs = managerNotificationsMap.computeIfAbsent(mgrId, k -> new ArrayList<>());

        String stw = (req.stwid() != null && !req.stwid().isBlank()) ? req.stwid().toUpperCase().trim() : "STW-829104";
        long delayMins = req.delayMinutes() > 0 ? req.delayMinutes() : 18;
        String reason = (req.reason() != null && !req.reason().isBlank()) ? req.reason() : "Traffic congestion on arterial junction corridor";

        EmpDirectoryItem item = empDirectory.getOrDefault(stw, new EmpDirectoryItem(
                stw, "Direct Report " + stw, "Operations Specialist", mgrId, "Office", "18:15", "Route 03", "KA-05-MK-3310", "Manjunath K", "+91-98804-99881", "MALE", 12.0
        ));

        String mgrEmail;
        if ("MGR-101".equalsIgnoreCase(mgrId)) {
            mgrEmail = "vikram.malhotra@catalyst.com";
        } else if ("MGR-102".equalsIgnoreCase(mgrId)) {
            mgrEmail = "priya.sharma@catalyst.com";
        } else {
            mgrEmail = "meenakshi.raman@catalyst.com";
        }

        String notifId = "NOTIF-DLY-" + (System.currentTimeMillis() % 9000 + 1000);
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMM dd, yyyy - HH:mm:ss")) + " IST";
        String subject = "[URGENT COMMUTE DELAY ALERT] Direct Report " + item.name() + " (" + stw + ") Delayed +" + delayMins + "m";
        String body = "Dear Manager,\n\nMoveInSync Real-Time Telemetry Alert: Your direct report " + item.name() + " (" + item.role() + ", " + stw + ") has incurred a transit delay on " + item.routeId() + " (Cab " + item.cabReg() + ").\n\n• Delay Duration: +" + delayMins + " minutes\n• Root Cause / Reported Reason: " + reason + "\n• Assigned Driver: " + item.driverName() + " (" + item.driverPhone() + ")\n• Scheduled Shift Window: " + item.shiftTime() + "\n• Expected Floor Arrival: Delayed by " + delayMins + " minutes\n• Manager Action: Shift floor readiness grace window (+15m) automatically applied.\n\nAn automated SMS dispatch has also been broadcast to Central Dispatch.\n\nRegards,\nMoveInSync Automated Telemetry Command Desk";

        EmployeeDelayNotification newNotif = new EmployeeDelayNotification(
                notifId,
                stw,
                item.name(),
                item.role(),
                item.shiftTime(),
                item.routeId(),
                item.cabReg(),
                item.driverName(),
                item.driverPhone(),
                delayMins,
                reason,
                delayMins >= 15 ? "CRITICAL" : "WARNING",
                mgrEmail,
                subject,
                body,
                timestamp,
                "DELIVERED_TO_INBOX",
                false
        );

        System.out.println("======================================================================");
        System.out.println(">>> [MOVEINSYNC AUTOMATED EMAIL DISPATCH TO MANAGER] <<<");
        System.out.println("TO: " + mgrEmail);
        System.out.println("SUBJECT: " + subject);
        System.out.println("TIMESTAMP: " + timestamp);
        System.out.println("STATUS: 250 OK - Message delivered to " + mgrEmail);
        System.out.println("======================================================================");

        notifs.add(0, newNotif);
        return newNotif;
    }
}
