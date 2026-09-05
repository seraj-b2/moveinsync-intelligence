import { Component, OnInit, inject, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { 
  ApiService, 
  EmployeePortalResponse, 
  TripActivityRecord, 
  FeedbackSubmitRequest, 
  SosTriggerRequest,
  ActionResponse 
} from '../services/api.service';

@Component({
  selector: 'app-employee-dashboard',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './employee-dashboard.html',
  styleUrls: ['./employee-dashboard.scss']
})
export class EmployeeDashboardComponent implements OnInit {
  private readonly route = inject(ActivatedRoute);
  private readonly apiService = inject(ApiService);
  private readonly cdr = inject(ChangeDetectorRef);

  companyName = 'catalyst-Sac';
  employeeId = 'STW-484475';
  selectedMonth = 'All Historical Data (May - July 2026)';
  availableMonths: string[] = [
    'All Historical Data (May - July 2026)',
    'July 2026',
    'June 2026',
    'May 2026'
  ];

  activeTab: 'overview' | 'live' | 'safety' | 'feedback' | 'copilot' = 'overview';
  historyFilter: 'ALL' | 'COMMUTED' | 'ON_TIME' | 'DELAYED' | 'NO_SHOW' | 'OFF' = 'ALL';

  loading = false;
  error: string | null = null;
  dashboardData: EmployeePortalResponse | null = null;

  // Boarding action
  boardingConfirmed = false;
  boardingMessage = '';

  // Feedback form
  feedbackTripId = '';
  feedbackDriverRating = 5;
  feedbackCabRating = 5;
  feedbackRouteRating = 5;
  feedbackSafetyRating = 5;
  feedbackMarshalRating = 5;
  feedbackComment = '';
  submittingFeedback = false;
  feedbackSuccessMsg = '';

  // SOS Emergency
  sosTriggering = false;
  sosAlertActive = false;
  sosResponse: ActionResponse | null = null;

  // AI Copilot
  copilotQuery = '';
  isCopilotThinking = false;
  copilotHistory: Array<{ sender: 'user' | 'ai'; message: string; time: string; actions?: string[] }> = [
    {
      sender: 'ai',
      message: 'Hello! I am your MoveInSync Commute Copilot. I actively track your upcoming shifts, monitor traffic bottlenecks along your corridor, verify female night escort protocols, and coordinate directly with your reporting manager. How can I help you today?',
      time: 'Just now'
    }
  ];

  quickPrompts: string[] = [
    'What is my pickup ETA and driver contact tonight?',
    'Is my security escort verified for night shift 21:30?',
    'Can I notify my manager that my cab is delayed in traffic?',
    'Request an ad-hoc logout shift cab change to 01:00'
  ];

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      const paramCompany = params.get('companyName');
      const paramEmpId = params.get('employeeId');

      if (paramCompany) {
        this.companyName = paramCompany;
      }
      if (paramEmpId) {
        this.employeeId = paramEmpId;
      }

      this.loadEmployeeData();
    });
  }

  loadEmployeeData(): void {
    this.loading = true;
    this.error = null;

    this.apiService.getEmployeePortalData(this.companyName, this.employeeId, this.selectedMonth).subscribe({
      next: (data) => {
        this.dashboardData = data;
        if (data.liveTrip) {
          this.feedbackTripId = data.liveTrip.tripId;
        } else if (data.tripHistory && data.tripHistory.length > 0) {
          this.feedbackTripId = data.tripHistory[0].tripId;
        }
        this.loading = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('Error loading employee portal data', err);
        this.error = 'Failed to load employee commute data. Please verify your STW ID and network connection.';
        this.loading = false;
        this.cdr.detectChanges();
      }
    });
  }

  onFilterChange(): void {
    this.loadEmployeeData();
  }

  get filteredHistory(): TripActivityRecord[] {
    if (!this.dashboardData || !this.dashboardData.tripHistory) {
      return [];
    }
    if (this.historyFilter === 'ALL') {
      return this.dashboardData.tripHistory;
    }
    if (this.historyFilter === 'COMMUTED') {
      return this.dashboardData.tripHistory.filter(t => !t.isNoShow && t.boardingStatus !== 'Weekend Off' && !t.boardingStatus.includes('WFH'));
    }
    if (this.historyFilter === 'ON_TIME') {
      return this.dashboardData.tripHistory.filter(t => t.delayMinutes === 0 && (t.boardingStatus === 'Boarded' || t.boardingStatus === 'On-Time'));
    }
    if (this.historyFilter === 'DELAYED') {
      return this.dashboardData.tripHistory.filter(t => t.delayMinutes > 0 || t.boardingStatus === 'Delayed');
    }
    if (this.historyFilter === 'NO_SHOW') {
      return this.dashboardData.tripHistory.filter(t => t.isNoShow || t.boardingStatus === 'No-Show');
    }
    if (this.historyFilter === 'OFF') {
      return this.dashboardData.tripHistory.filter(t => t.boardingStatus === 'Weekend Off' || t.boardingStatus.includes('WFH'));
    }
    return this.dashboardData.tripHistory;
  }

  get totalDaysCount(): number {
    return this.dashboardData?.tripHistory?.length || 0;
  }

  get commutedDaysCount(): number {
    return this.dashboardData?.tripHistory?.filter(t => !t.isNoShow && t.boardingStatus !== 'Weekend Off' && !t.boardingStatus.includes('WFH')).length || 0;
  }

  get delayedDaysCount(): number {
    return this.dashboardData?.tripHistory?.filter(t => t.delayMinutes > 0 || t.boardingStatus === 'Delayed').length || 0;
  }

  get noShowDaysCount(): number {
    return this.dashboardData?.tripHistory?.filter(t => t.isNoShow || t.boardingStatus === 'No-Show').length || 0;
  }

  get offDaysCount(): number {
    return this.dashboardData?.tripHistory?.filter(t => t.boardingStatus === 'Weekend Off' || t.boardingStatus.includes('WFH')).length || 0;
  }

  confirmBoarding(): void {
    this.boardingConfirmed = true;
    this.boardingMessage = `Boarding confirmed with OTP ${this.dashboardData?.liveTrip.boardingOtp}! GPS telemetry live tracking activated. Have a safe journey!`;
    if (this.dashboardData?.liveTrip) {
      this.dashboardData.liveTrip.status = 'BOARDED';
    }
    this.cdr.detectChanges();
  }

  triggerSos(): void {
    if (!this.dashboardData) return;
    this.sosTriggering = true;

    const req: SosTriggerRequest = {
      stwid: this.employeeId,
      tripId: this.dashboardData.liveTrip?.tripId || 'LIVE-SOS',
      eventType: 'PANIC_MOBILE',
      userNote: 'Emergency SOS button triggered from Employee Mobility Portal',
      location: this.dashboardData.liveTrip?.pickupLocation || 'Active Route Corridor'
    };

    this.apiService.triggerEmployeeSos(req).subscribe({
      next: (res) => {
        this.sosResponse = res;
        this.sosAlertActive = true;
        this.sosTriggering = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('Error triggering SOS', err);
        this.sosTriggering = false;
        this.cdr.detectChanges();
      }
    });
  }

  dismissSos(): void {
    this.sosAlertActive = false;
    this.sosResponse = null;
    this.cdr.detectChanges();
  }

  submitRating(): void {
    if (!this.feedbackTripId) return;
    this.submittingFeedback = true;
    this.feedbackSuccessMsg = '';

    const req: FeedbackSubmitRequest = {
      stwid: this.employeeId,
      tripId: this.feedbackTripId,
      driverRating: this.feedbackDriverRating,
      cabRating: this.feedbackCabRating,
      routeRating: this.feedbackRouteRating,
      safetyRating: this.feedbackSafetyRating,
      marshalRating: this.feedbackMarshalRating,
      comments: this.feedbackComment
    };

    this.apiService.submitEmployeeFeedback(req).subscribe({
      next: (res) => {
        this.submittingFeedback = false;
        this.feedbackSuccessMsg = res.message;
        // Prepend to local history
        if (this.dashboardData) {
          const overall = Number(((this.feedbackDriverRating + this.feedbackCabRating + this.feedbackRouteRating + this.feedbackSafetyRating + this.feedbackMarshalRating) / 5).toFixed(1));
          this.dashboardData.feedbackHistory.unshift({
            tripId: this.feedbackTripId,
            tripDate: 'Today (Just now)',
            overallRating: overall,
            driverRating: this.feedbackDriverRating,
            cabRating: this.feedbackCabRating,
            routeRating: this.feedbackRouteRating,
            safetyRating: this.feedbackSafetyRating,
            marshalRating: this.feedbackMarshalRating,
            comments: this.feedbackComment || 'Smooth commute experience.'
          });
        }
        this.feedbackComment = '';
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('Error submitting feedback', err);
        this.submittingFeedback = false;
        this.cdr.detectChanges();
      }
    });
  }

  sendCopilotQuery(customText?: string): void {
    const text = (customText || this.copilotQuery).trim();
    if (!text) return;

    this.copilotHistory.push({
      sender: 'user',
      message: text,
      time: 'Just now'
    });

    this.copilotQuery = '';
    this.isCopilotThinking = true;
    this.cdr.detectChanges();

    setTimeout(() => {
      this.isCopilotThinking = false;
      const lower = text.toLowerCase();
      let reply = '';
      let actions: string[] = [];

      if (lower.includes('eta') || lower.includes('driver') || lower.includes('cab')) {
        const live = this.dashboardData?.liveTrip;
        reply = `Your cab (${live?.cabReg}) with Driver ${live?.driverName} is currently ${live?.status === 'BOARDED' ? 'in transit with you on-board' : 'en route with an estimated ETA of ' + live?.etaMinutes + ' minutes'}. Your scheduled pickup window is ${live?.scheduledPickupTime} at ${live?.pickupLocation}.`;
        actions = [
          `Verified cab registration telemetry on ${live?.routeId}`,
          `Call Driver directly: ${live?.driverPhone}`,
          `Share live trip coordinates with emergency contact`
        ];
      } else if (lower.includes('escort') || lower.includes('safety') || lower.includes('night')) {
        const live = this.dashboardData?.liveTrip;
        reply = live?.escortAssigned 
          ? `MoveInSync Night Security Protocol is ACTIVE for your shift. ${live.escortName} has been assigned to your vehicle and verified by the 24/7 Security Operations Center.` 
          : `Your current shift (${live?.shiftType}) falls during standard daytime hours. Full telemetry geofencing and automatic deviation alerting remain active throughout your trip.`;
        actions = [
          `100% Female Night Drop Security Protocol enforced`,
          `Continuous GPS geofence radar monitoring active`,
          `24/7 Security Escalation Desk on standby`
        ];
      } else if (lower.includes('manager') || lower.includes('delay')) {
        const mgr = this.dashboardData?.profile.managerName;
        reply = `Understood. I have drafted an autonomous status update to your reporting manager (${mgr}). If your cab incurs traffic delays exceeding 15 minutes, your floor shift readiness SLA will automatically adjust with an excused commute allowance.`;
        actions = [
          `Pushed telemetry note to ${mgr}'s Shift Readiness Roster`,
          `Protected shift login grace window (+15 mins)`,
          `Notified Central Dispatch to reroute backup vehicle if required`
        ];
      } else if (lower.includes('cancel') || lower.includes('change') || lower.includes('logout')) {
        reply = `Ad-hoc trip modification request received for ${this.employeeId}. You can reschedule or cancel your upcoming leg without SLA penalty up to 45 minutes prior to shift departure. Would you like me to book your alternate logout slot?`;
        actions = [
          `Synced with Transport Scheduling Engine`,
          `Verified seat allocation on requested route`,
          `Instant SMS confirmation sent`
        ];
      } else {
        reply = `I have analyzed your commute profile for ${this.dashboardData?.profile.employeeName} (${this.employeeId}). Your overall on-time rate is ${this.dashboardData?.commuteStats.onTimePercentage}% across ${this.dashboardData?.commuteStats.totalTrips} completed trips. Everything is in order for your upcoming shift!`;
        actions = [
          `Checked live route conditions`,
          `Verified boarding pass readiness`,
          `Manager notified of on-track status`
        ];
      }

      this.copilotHistory.push({
        sender: 'ai',
        message: reply,
        time: 'Just now',
        actions: actions
      });
      this.cdr.detectChanges();
    }, 700);
  }
}
