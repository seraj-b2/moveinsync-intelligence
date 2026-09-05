import {
  Component,
  OnInit,
  OnDestroy,
  inject,
  ChangeDetectorRef
} from '@angular/core';
import { CommonModule, DecimalPipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule, ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';

import {
  ApiService,
  ManagerDashboardResponse,
  ManagerShiftSummary,
  TeamMemberRosterDetail
} from '../services/api.service';

@Component({
  selector: 'app-manager-dashboard',
  standalone: true,
  imports: [CommonModule, DecimalPipe, FormsModule, RouterModule],
  templateUrl: './manager-dashboard.html',
  styleUrl: './manager-dashboard.scss'
})
export class ManagerDashboardComponent implements OnInit, OnDestroy {
  private readonly apiService = inject(ApiService);
  private readonly changeDetectorRef = inject(ChangeDetectorRef);
  private readonly route = inject(ActivatedRoute);

  private routeSub?: Subscription;

  // Route Parameters
  companyName: string = 'catalyst-Sac';
  managerId: string = 'MGR-103';

  // Navigation & Filter State
  activeTab: 'roster' | 'safety' | 'csat' | 'ai-advisor' = 'roster';
  rosterFilter: 'ALL' | 'ON_TIME' | 'DELAYED' | 'NO_SHOW' = 'ALL';

  availableMonths = ['July 2026', 'June 2026', 'May 2026', 'All Months'];
  selectedMonth = 'July 2026';

  dashboardData: ManagerDashboardResponse | null = null;
  selectedShift: ManagerShiftSummary | null = null;
  loading = true;

  // Agentic AI Shift Risk Assistant
  simulatedScenario = '';
  evaluatingShiftRisk = false;
  shiftRiskResult: any = null;

  ngOnInit(): void {
    this.routeSub = this.route.paramMap.subscribe(params => {
      this.companyName = params.get('companyName') || 'catalyst-Sac';
      this.managerId = (params.get('managerId') || 'MGR-103').toUpperCase();
      this.updateDefaultScenario();
      this.loadDashboard();
    });
  }

  ngOnDestroy(): void {
    this.routeSub?.unsubscribe();
  }

  private updateDefaultScenario(): void {
    if (this.managerId === 'MGR-103') {
      this.simulatedScenario = `Geofence Deviation & Delay: Route 11 cab carrying Deepika Rao detoured 1.2km outside corridor with 22m delay for 18:15 shift handover at ${this.companyName}.`;
    } else if (this.managerId === 'MGR-102') {
      this.simulatedScenario = `Expressway Congestion Alert: Route 07 cab carrying Aditya Joshi & Divya Menon delayed 14m due to bridge maintenance ahead of 08:30 shift at ${this.companyName}.`;
    } else {
      this.simulatedScenario = `Critical Delay Alert: Route 04 cab with Pooja Hegde delayed 18 mins due to expressway waterlogging ahead of 21:30 shift at ${this.companyName}.`;
    }
    this.shiftRiskResult = null;
  }

  private getFallbackData(month: string): ManagerDashboardResponse {
    if (this.managerId === 'MGR-103') {
      const roster: TeamMemberRosterDetail[] = [
        { stwid: 'STW-718290', employeeName: 'Meenakshi Raman', role: 'Support Lead', managerId: 'MGR-103', office: 'Cedar Ridge Office', shiftTime: '18:15', routeId: 'Route 03', cabReg: 'KA-05-MK-3310', pickupStatus: 'ON_TIME', delayMinutes: 0, plannedKm: 10.50, traveledKm: 10.20, isNoShow: false, noShowReason: '', gender: 'FEMALE' },
        { stwid: 'STW-829104', employeeName: 'Tanmay Sen', role: 'Tier-2 Analyst', managerId: 'MGR-103', office: 'Cedar Ridge Office', shiftTime: '18:15', routeId: 'Route 03', cabReg: 'KA-05-MK-3310', pickupStatus: 'ON_TIME', delayMinutes: 0, plannedKm: 13.20, traveledKm: 13.00, isNoShow: false, noShowReason: '', gender: 'MALE' },
        { stwid: 'STW-930192', employeeName: 'Deepika Rao', role: 'Escalation Desk', managerId: 'MGR-103', office: 'Fairview Commons', shiftTime: '18:15', routeId: 'Route 11', cabReg: 'KA-01-NJ-7712', pickupStatus: 'DELAYED', delayMinutes: 22, plannedKm: 19.80, traveledKm: 26.50, isNoShow: false, noShowReason: 'SEVERE_TRAFFIC', gender: 'FEMALE' },
        { stwid: 'STW-104928', employeeName: 'Siddharth Paul', role: 'NOC Engineer', managerId: 'MGR-103', office: 'Fairview Commons', shiftTime: '18:15', routeId: 'Route 11', cabReg: 'KA-01-NJ-7712', pickupStatus: 'NO_SHOW', delayMinutes: 0, plannedKm: 15.00, traveledKm: 0.00, isNoShow: true, noShowReason: 'PERSONAL_EMERGENCY', gender: 'MALE' }
      ];

      return {
        managerId: 'MGR-103',
        managerName: 'Meenakshi Raman',
        title: 'Operations Manager — 24/7 Global Escalations',
        department: 'Global Tier-2 & NOC Incident Desk',
        companyName: this.companyName,
        office: 'Cedar Ridge Office',
        teamSize: 4,
        teamReadinessIndex: 75.0,
        averageCsat: 4.4,
        shifts: [
          {
            shiftId: 'SHIFT_1815',
            shiftName: 'Shift 18:15 - Evening Support Handover',
            timeWindow: '18:15 - 03:00',
            totalDirectReports: 4,
            boardedCount: 3,
            noShowCount: 1,
            noShowRatePercentage: 25.0,
            onTimeBoardingPercentage: 75.0,
            shiftReadinessIndex: 75.0,
            roster: roster,
            safetyAlerts: [
              { alertId: 'ALT-SAF-6041', eventType: 'EMPLOYEE_GEOFENCE_VIOLATION', severity: 'Sev-3', stwid: 'STW-930192', employeeName: 'Deepika Rao', cabReg: 'KA-01-NJ-7712', escortAssigned: false, stateText: 'ACKNOWLEDGED', timestamp: month + ' 18:38', actionRequired: 'Driver detoured 1.2km outside geofence corridor due to flyover construction' }
            ],
            csatBreakdown: { overallCsat: 4.4, driverRating: 4.5, cabRating: 4.3, routeRating: 4.1, safetyRating: 4.7, marshalRating: 4.5, totalFeedbackCount: 82 }
          }
        ]
      };
    } else if (this.managerId === 'MGR-102') {
      const roster: TeamMemberRosterDetail[] = [
        { stwid: 'STW-204918', employeeName: 'Karthik Iyer', role: 'Principal Architect', managerId: 'MGR-102', office: 'Fairview Commons', shiftTime: '08:30', routeId: 'Route 01', cabReg: 'KA-02-EA-1920', pickupStatus: 'ON_TIME', delayMinutes: 0, plannedKm: 16.20, traveledKm: 15.90, isNoShow: false, noShowReason: '', gender: 'MALE' },
        { stwid: 'STW-309182', employeeName: 'Sneha Kulkarni', role: 'Frontend Lead', managerId: 'MGR-102', office: 'Fairview Commons', shiftTime: '08:30', routeId: 'Route 01', cabReg: 'KA-02-EA-1920', pickupStatus: 'ON_TIME', delayMinutes: 0, plannedKm: 12.40, traveledKm: 12.10, isNoShow: false, noShowReason: '', gender: 'FEMALE' },
        { stwid: 'STW-401928', employeeName: 'Aditya Joshi', role: 'Backend Engg', managerId: 'MGR-102', office: 'Santa Clara Office', shiftTime: '08:30', routeId: 'Route 07', cabReg: 'KA-04-TR-8811', pickupStatus: 'DELAYED', delayMinutes: 14, plannedKm: 18.90, traveledKm: 24.10, isNoShow: false, noShowReason: 'ROAD_CLOSURE_DETOUR', gender: 'MALE' },
        { stwid: 'STW-510293', employeeName: 'Divya Menon', role: 'QA Specialist', managerId: 'MGR-102', office: 'Santa Clara Office', shiftTime: '08:30', routeId: 'Route 07', cabReg: 'KA-04-TR-8811', pickupStatus: 'ON_TIME', delayMinutes: 3, plannedKm: 14.10, traveledKm: 14.50, isNoShow: false, noShowReason: '', gender: 'FEMALE' },
        { stwid: 'STW-619283', employeeName: 'Rohan Das', role: 'Product Designer', managerId: 'MGR-102', office: 'Clearwater Campus', shiftTime: '08:30', routeId: 'Route 15', cabReg: 'KA-01-PL-4490', pickupStatus: 'NO_SHOW', delayMinutes: 0, plannedKm: 9.80, traveledKm: 0.00, isNoShow: true, noShowReason: 'UNRESPONSIVE_CALL', gender: 'MALE' }
      ];

      return {
        managerId: 'MGR-102',
        managerName: 'Priya Sharma',
        title: 'Engineering Manager — Digital Banking Platform',
        department: 'Digital Banking & Frontend Systems',
        companyName: this.companyName,
        office: 'Fairview Commons',
        teamSize: 5,
        teamReadinessIndex: 80.0,
        averageCsat: 4.8,
        shifts: [
          {
            shiftId: 'SHIFT_0830',
            shiftName: 'Shift 08:30 - Morning Core Engg',
            timeWindow: '08:30 - 17:30',
            totalDirectReports: 5,
            boardedCount: 4,
            noShowCount: 1,
            noShowRatePercentage: 20.0,
            onTimeBoardingPercentage: 80.0,
            shiftReadinessIndex: 80.0,
            roster: roster,
            safetyAlerts: [
              { alertId: 'ALT-SAF-7120', eventType: 'OVER_SPEEDING', severity: 'Sev-2', stwid: 'STW-401928', employeeName: 'Aditya Joshi (Route 07)', cabReg: 'KA-04-TR-8811', escortAssigned: false, stateText: 'RESOLVED', timestamp: month + ' 08:12', actionRequired: 'Vehicle speed exceeded 65 km/h on Expressway; automated warning buzzer acknowledged' }
            ],
            csatBreakdown: { overallCsat: 4.8, driverRating: 4.9, cabRating: 4.7, routeRating: 4.6, safetyRating: 4.9, marshalRating: 4.7, totalFeedbackCount: 95 }
          }
        ]
      };
    } else {
      // MGR-101 (or default)
      const roster: TeamMemberRosterDetail[] = [
        { stwid: 'STW-484475', employeeName: 'Ananya Sharma', role: 'Senior SRE', managerId: 'MGR-101', office: 'Fairview Commons', shiftTime: '21:30', routeId: 'Route 12', cabReg: 'KA-01-MJ-4821', pickupStatus: 'ON_TIME', delayMinutes: 0, plannedKm: 9.97, traveledKm: 9.33, isNoShow: false, noShowReason: '', gender: 'FEMALE' },
        { stwid: 'STW-332325', employeeName: 'Vikram Malhotra', role: 'Team Lead', managerId: 'MGR-101', office: 'Fairview Commons', shiftTime: '21:30', routeId: 'Route 12', cabReg: 'KA-01-MJ-4821', pickupStatus: 'ON_TIME', delayMinutes: 2, plannedKm: 14.50, traveledKm: 14.80, isNoShow: false, noShowReason: '', gender: 'MALE' },
        { stwid: 'STW-194821', employeeName: 'Pooja Hegde', role: 'Cloud Engineer', managerId: 'MGR-101', office: 'Cedar Ridge Office', shiftTime: '21:30', routeId: 'Route 04', cabReg: 'KA-05-MB-1102', pickupStatus: 'DELAYED', delayMinutes: 18, plannedKm: 22.10, traveledKm: 28.40, isNoShow: false, noShowReason: 'TRAFFIC_BOTTLENECK', gender: 'FEMALE' },
        { stwid: 'STW-101925', employeeName: 'Rahul Verma', role: 'DBA', managerId: 'MGR-101', office: 'Cedar Ridge Office', shiftTime: '21:30', routeId: 'Route 04', cabReg: 'KA-05-MB-1102', pickupStatus: 'NO_SHOW', delayMinutes: 0, plannedKm: 7.66, traveledKm: 0.00, isNoShow: true, noShowReason: 'TRIP_CANCELLED_FROM_DASHBOARD', gender: 'MALE' },
        { stwid: 'STW-093335', employeeName: 'Neha Sundaram', role: 'Security Analyst', managerId: 'MGR-101', office: 'Santa Clara Office', shiftTime: '21:30', routeId: 'Route 09', cabReg: 'KA-03-MK-9910', pickupStatus: 'ON_TIME', delayMinutes: 0, plannedKm: 11.20, traveledKm: 11.50, isNoShow: false, noShowReason: '', gender: 'FEMALE' },
        { stwid: 'STW-512401', employeeName: 'Arjun Nair', role: 'Support Engg', managerId: 'MGR-101', office: 'Fairview Commons', shiftTime: '21:30', routeId: 'Route 12', cabReg: 'KA-01-MJ-4821', pickupStatus: 'ON_TIME', delayMinutes: 0, plannedKm: 8.40, traveledKm: 8.20, isNoShow: false, noShowReason: '', gender: 'MALE' }
      ];

      return {
        managerId: 'MGR-101',
        managerName: 'Vikram Malhotra',
        title: 'Engineering Manager — Cloud & SRE Floor Ops',
        department: 'Cloud Infrastructure & SRE',
        companyName: this.companyName,
        office: 'Fairview Commons',
        teamSize: 6,
        teamReadinessIndex: 83.3,
        averageCsat: 4.6,
        shifts: [
          {
            shiftId: 'SHIFT_2130',
            shiftName: 'Shift 21:30 - Night Floor Ops',
            timeWindow: '21:30 - 06:00',
            totalDirectReports: 6,
            boardedCount: 5,
            noShowCount: 1,
            noShowRatePercentage: 16.7,
            onTimeBoardingPercentage: 83.3,
            shiftReadinessIndex: 83.3,
            roster: roster,
            safetyAlerts: [
              { alertId: 'ALT-SAF-8901', eventType: 'WOMAN_TRAVELLING_ALONE', severity: 'Sev-1', stwid: 'STW-194821', employeeName: 'Pooja Hegde', cabReg: 'KA-05-MB-1102', escortAssigned: true, stateText: 'ACTIVE_ESCORT', timestamp: month + ' 21:52', actionRequired: 'Live GPS Telemetry Tracking & Verified Security Escort Assigned' },
              { alertId: 'ALT-SAF-8902', eventType: 'FIRST_MALE_NO_SHOW', severity: 'Sev-2', stwid: 'STW-101925', employeeName: 'Rahul Verma', cabReg: 'KA-05-MB-1102', escortAssigned: false, stateText: 'RESOLVED', timestamp: month + ' 21:15', actionRequired: 'Roster updated; confirmed via automated SMS cancellation' }
            ],
            csatBreakdown: { overallCsat: 4.6, driverRating: 4.7, cabRating: 4.4, routeRating: 4.3, safetyRating: 4.9, marshalRating: 4.8, totalFeedbackCount: 128 }
          }
        ]
      };
    }
  }

  loadDashboard(): void {
    this.loading = true;
    const currentShiftId = this.selectedShift?.shiftId;

    this.apiService.getManagerDashboard(this.companyName, this.managerId, this.selectedMonth).subscribe({
      next: (data) => {
        this.dashboardData = data || this.getFallbackData(this.selectedMonth);
        this.setSelectedShiftOrDefault(currentShiftId);
        this.loading = false;
        this.changeDetectorRef.detectChanges();
      },
      error: () => {
        this.dashboardData = this.getFallbackData(this.selectedMonth);
        this.setSelectedShiftOrDefault(currentShiftId);
        this.loading = false;
        this.changeDetectorRef.detectChanges();
      }
    });
  }

  onFilterChange(): void {
    this.loadDashboard();
  }

  selectShift(shift: ManagerShiftSummary): void {
    this.selectedShift = shift;
    this.rosterFilter = 'ALL';
  }

  private setSelectedShiftOrDefault(preferredId?: string): void {
    if (!this.dashboardData || !this.dashboardData.shifts || this.dashboardData.shifts.length === 0) {
      this.selectedShift = null;
      return;
    }
    if (preferredId) {
      const match = this.dashboardData.shifts.find(s => s.shiftId === preferredId);
      if (match) {
        this.selectedShift = match;
        return;
      }
    }
    this.selectedShift = this.dashboardData.shifts[0];
  }

  get filteredRoster(): TeamMemberRosterDetail[] {
    if (!this.selectedShift) return [];
    if (this.rosterFilter === 'ON_TIME') {
      return this.selectedShift.roster.filter(r => r.pickupStatus === 'ON_TIME' && !r.isNoShow);
    }
    if (this.rosterFilter === 'DELAYED') {
      return this.selectedShift.roster.filter(r => r.pickupStatus === 'DELAYED');
    }
    if (this.rosterFilter === 'NO_SHOW') {
      return this.selectedShift.roster.filter(r => r.isNoShow);
    }
    return this.selectedShift.roster;
  }

  evaluateShiftRisk(): void {
    this.evaluatingShiftRisk = true;
    this.shiftRiskResult = null;
    this.changeDetectorRef.detectChanges();

    setTimeout(() => {
      this.evaluatingShiftRisk = false;
      const mgrName = this.dashboardData?.managerName || 'Manager';
      const deptName = this.dashboardData?.department || 'Operations Team';

      this.shiftRiskResult = {
        managerAssigned: `${mgrName} (${this.managerId})`,
        company: this.companyName,
        teamScope: deptName,
        shiftName: this.selectedShift?.shiftName || 'Active Shift',
        affectedRoute: this.managerId === 'MGR-103' ? 'Route 11 (Flyover Detour corridor)' : this.managerId === 'MGR-102' ? 'Route 07 (Expressway Detour)' : 'Route 04 (Cedar Ridge corridor)',
        impactedEmployees: this.managerId === 'MGR-103'
          ? ['Deepika Rao (Escalation Desk)', 'Siddharth Paul (NOC - No-Show)']
          : this.managerId === 'MGR-102'
          ? ['Aditya Joshi (Backend Engg)', 'Divya Menon (QA Specialist)']
          : ['Pooja Hegde (Cloud Engg)', 'Rahul Verma (DBA - No-Show)'],
        floorReadinessImpact: `Immediate readiness impact for ${mgrName}'s direct reports at ${this.companyName}: -25% coverage during shift handover`,
        confidence: 97.4,
        agenticActionsTaken: [
          `Autonomous notification sent directly to ${mgrName} with team roster impact overview.`,
          'Backup nodal transport shuttle auto-dispatched to bypass identified traffic bottleneck.',
          'Woman safety escort protocol confirmed for night commuters.',
          'Shift handover SLA grace window dynamically applied to affected team members without penalty.'
        ],
        recommendation: 'EXECUTE AUTONOMOUS SHIFT MITIGATION',
        summary: `AI Agent successfully isolated the incident to ${mgrName}'s direct reports and protected team SLA targets.`
      };
      this.changeDetectorRef.detectChanges();
    }, 1200);
  }
}
