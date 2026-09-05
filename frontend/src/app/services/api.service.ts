import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface HealthResponse {
  service: string;
  status: string;
}

export interface DashboardSummary {
  totalTrips: number;
  activeVehicles: number;
  onTimePercentage: number;
  delayedTrips: number;
  employeesTransported: number;
  routeUtilization: number;
}

export interface VendorSlaDetail {
  month: string;
  vendorName: string;
  actualOta: number;
  targetSla: number;
  totalTrips: number;
  vendorFaultDelays: number;
  employeeFaultDelays: number;
  trafficWeatherDelays: number;
}

export interface BillingAuditDetail {
  tripId: string;
  vendorName: string;
  billedKm: number;
  gpsActualKm: number;
  billedCost: number;
  auditExpectedCost: number;
  discrepancyReason: string;
  status: string;
}

export interface ComplianceAlertDetail {
  alertId: string;
  vendorName: string;
  vehicleReg: string;
  driverName: string;
  alertType: string;
  severity: string;
  timestamp: string;
}

export interface VendorScorecard {
  vendorName: string;
  onTimeArrivalPercentage: number;
  slaTargetPercentage: number;
  complianceRatePercentage: number;
  billingAccuracyPercentage: number;
  averageDriverRating: number;
  totalTrips: number;
  delayedTrips: number;
  nonCompliantTrips: number;
  billingDiscrepanciesCount: number;
  slaBreakdown: VendorSlaDetail[];
  billingDiscrepancies: BillingAuditDetail[];
  complianceAlerts: ComplianceAlertDetail[];
}

export interface ClaimEvaluationResponse {
  vendorName: string;
  claimDate: string;
  route: string;
  claimValid: boolean;
  confidence: number;
  evidence: string[];
  recommendation: string;
  reasoning: string;
}

export interface TripAttributionDetail {
  tripId: string;
  employeeId: string;
  pickupTimeScheduled: string;
  pickupTimeActual: string;
  employeeBoardedTime: string;
  totalDelayMinutes: number;
  delayCategory: string;
  attributionReason: string;
  penaltyExempt: boolean;
}

export interface SlaShieldResponse {
  vendorName: string;
  month: string;
  rawOtaPercentage: number;
  adjustedSlaPercentage: number;
  targetSlaPercentage: number;
  savedPenaltyAmount: number;
  totalTrips: number;
  totalDelayedTrips: number;
  vendorFaultDelays: number;
  employeeFaultDelays: number;
  trafficWeatherDelays: number;
  sarvamAiAttributionAnalysis: string;
  sampleAnalyzedTrips: TripAttributionDetail[];
}

export interface TeamMemberRosterDetail {
  stwid: string;
  employeeName: string;
  role: string;
  managerId: string;
  office: string;
  shiftTime: string;
  routeId: string;
  cabReg: string;
  pickupStatus: string;
  delayMinutes: number;
  plannedKm: number;
  traveledKm: number;
  isNoShow: boolean;
  noShowReason: string;
  gender: string;
}

export interface TeamSafetyAlertDetail {
  alertId: string;
  eventType: string;
  severity: string;
  stwid: string;
  employeeName: string;
  cabReg: string;
  escortAssigned: boolean;
  stateText: string;
  timestamp: string;
  actionRequired: string;
}

export interface TeamCsatDetail {
  overallCsat: number;
  driverRating: number;
  cabRating: number;
  routeRating: number;
  safetyRating: number;
  marshalRating: number;
  totalFeedbackCount: number;
}

export interface ManagerShiftSummary {
  shiftId: string;
  shiftName: string;
  timeWindow: string;
  totalDirectReports: number;
  boardedCount: number;
  noShowCount: number;
  noShowRatePercentage: number;
  onTimeBoardingPercentage: number;
  shiftReadinessIndex: number;
  roster: TeamMemberRosterDetail[];
  safetyAlerts: TeamSafetyAlertDetail[];
  csatBreakdown: TeamCsatDetail;
}

export interface ManagerDashboardResponse {
  managerId: string;
  managerName: string;
  title: string;
  department: string;
  companyName: string;
  office: string;
  teamSize: number;
  teamReadinessIndex: number;
  averageCsat: number;
  shifts: ManagerShiftSummary[];
}

export interface ManagerProfile {
  managerId: string;
  managerName: string;
  title: string;
  department: string;
  companyName: string;
  teamSize: number;
  office: string;
}

// ================= EMPLOYEE PORTAL MODELS =================
export interface EmployeeProfile {
  stwid: string;
  employeeName: string;
  role: string;
  companyName: string;
  office: string;
  gender: string;
  managerId: string;
  managerName: string;
  onTimeBoardingRate: number;
  totalTripsTaken: number;
  safetyScore: number;
}

export interface LiveTripDetail {
  tripId: string;
  tripDate: string;
  shiftType: string;
  tripType: string;
  routeId: string;
  cabReg: string;
  driverName: string;
  driverPhone: string;
  pickupLocation: string;
  scheduledPickupTime: string;
  etaMinutes: number;
  boardingOtp: string;
  status: string; // 'SCHEDULED' | 'IN_TRANSIT' | 'BOARDED' | 'COMPLETED'
  escortAssigned: boolean;
  escortName: string;
  hasRideToday: boolean;
}

export interface TripActivityRecord {
  tripId: string;
  tripDate: string;
  dayOfWeek: string;
  shiftType: string;
  tripType: string;
  routeId: string;
  cabReg: string;
  plannedPickup: string;
  actualPickup: string;
  plannedKm: number;
  traveledKm: number;
  boardingStatus: string;
  delayMinutes: number;
  noShowReason: string;
  isNoShow: boolean;
}

export interface EmployeeSafetyAlert {
  alertId: string;
  eventType: string;
  severity: string;
  timestamp: string;
  cabReg: string;
  stateText: string;
  resolutionDetails: string;
}

export interface EmployeeFeedbackRecord {
  tripId: string;
  tripDate: string;
  overallRating: number;
  driverRating: number;
  cabRating: number;
  routeRating: number;
  safetyRating: number;
  marshalRating: number;
  comments: string;
}

export interface EmployeeCommuteStats {
  totalTrips: number;
  onTimePercentage: number;
  totalKmTraveled: number;
  avgPickupDelayMins: number;
  avgCsatGiven: number;
}

export interface EmployeePortalResponse {
  profile: EmployeeProfile;
  liveTrip: LiveTripDetail;
  tripHistory: TripActivityRecord[];
  safetyAlerts: EmployeeSafetyAlert[];
  feedbackHistory: EmployeeFeedbackRecord[];
  commuteStats: EmployeeCommuteStats;
}

export interface FeedbackSubmitRequest {
  stwid: string;
  tripId: string;
  driverRating: number;
  cabRating: number;
  routeRating: number;
  safetyRating: number;
  marshalRating: number;
  comments: string;
}

export interface SosTriggerRequest {
  stwid: string;
  tripId: string;
  eventType: string;
  userNote: string;
  location: string;
}

export interface ActionResponse {
  success: boolean;
  message: string;
  incidentId: string;
  timestamp: string;
}

@Injectable({
  providedIn: 'root'
})
export class ApiService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = 'http://localhost:8080/api';

  getHealth(): Observable<HealthResponse> {
    return this.http.get<HealthResponse>(`${this.apiUrl}/health`);
  }

  getDashboardSummary(): Observable<DashboardSummary> {
    return this.http.get<DashboardSummary>(`${this.apiUrl}/dashboard/summary`);
  }

  getVendorScorecards(month?: string, businessUnit?: string): Observable<VendorScorecard[]> {
    let url = `${this.apiUrl}/vendors/scorecards`;
    const params: string[] = [];
    if (month) params.push(`month=${encodeURIComponent(month)}`);
    if (businessUnit) params.push(`businessUnit=${encodeURIComponent(businessUnit)}`);
    if (params.length > 0) url += `?${params.join('&')}`;
    return this.http.get<VendorScorecard[]>(url);
  }

  evaluateVendorClaim(claimText: string, month: string): Observable<ClaimEvaluationResponse> {
    return this.http.post<ClaimEvaluationResponse>(`${this.apiUrl}/vendors/evaluate-claim`, {
      claimText,
      selectedMonth: month
    });
  }

  analyzeSlaShield(vendorName: string, month: string, businessUnit?: string): Observable<SlaShieldResponse> {
    return this.http.post<SlaShieldResponse>(`${this.apiUrl}/vendors/sla-shield/analyze`, {
      vendorName,
      selectedMonth: month,
      businessUnit
    });
  }

  getBusinessUnits(): Observable<string[]> {
    return this.http.get<string[]>(`${this.apiUrl}/vendors/business-units`);
  }

  getAvailableMonths(): Observable<string[]> {
    return this.http.get<string[]>(`${this.apiUrl}/vendors/months`);
  }

  getManagerProfiles(companyName?: string): Observable<ManagerProfile[]> {
    let url = `${this.apiUrl}/managers/profiles`;
    if (companyName) url += `?companyName=${encodeURIComponent(companyName)}`;
    return this.http.get<ManagerProfile[]>(url);
  }

  getManagerDashboard(companyName: string, managerId: string, month?: string): Observable<ManagerDashboardResponse> {
    let url = `${this.apiUrl}/managers/${encodeURIComponent(companyName)}/${encodeURIComponent(managerId)}`;
    if (month) url += `?month=${encodeURIComponent(month)}`;
    return this.http.get<ManagerDashboardResponse>(url);
  }

  getEmployeePortalData(companyName: string, stwid: string, month?: string): Observable<EmployeePortalResponse> {
    let url = `${this.apiUrl}/employees/${encodeURIComponent(companyName)}/${encodeURIComponent(stwid)}`;
    if (month) url += `?month=${encodeURIComponent(month)}`;
    return this.http.get<EmployeePortalResponse>(url);
  }

  submitEmployeeFeedback(request: FeedbackSubmitRequest): Observable<ActionResponse> {
    return this.http.post<ActionResponse>(`${this.apiUrl}/employees/feedback`, request);
  }

  triggerEmployeeSos(request: SosTriggerRequest): Observable<ActionResponse> {
    return this.http.post<ActionResponse>(`${this.apiUrl}/employees/sos`, request);
  }
}