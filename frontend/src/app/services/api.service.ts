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

export interface VendorDisputeItem {
  disputeId: string;
  vendorName: string;
  route: string;
  claimSubject: string;
  claimText: string;
  month: string;
  businessUnit: string;
  affectedCabs: number;
  status: string;
  submittedAt: string;
}

export interface DeduplicationReportResponse {
  totalRowsParsed: number;
  newRecordsSaved: number;
  duplicatesMerged: number;
  dedupeEfficiencyRate: number;
  datasetType: string;
  status: string;
  message: string;
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

  getVendorDisputes(month?: string, businessUnit?: string, vendorName?: string, status?: string): Observable<VendorDisputeItem[]> {
    let url = `${this.apiUrl}/vendors/disputes`;
    const params: string[] = [];
    if (month) params.push(`month=${encodeURIComponent(month)}`);
    if (businessUnit) params.push(`businessUnit=${encodeURIComponent(businessUnit)}`);
    if (vendorName) params.push(`vendorName=${encodeURIComponent(vendorName)}`);
    if (status) params.push(`status=${encodeURIComponent(status)}`);
    if (params.length > 0) url += `?${params.join('&')}`;
    return this.http.get<VendorDisputeItem[]>(url);
  }

  submitVendorDispute(dispute: VendorDisputeItem): Observable<VendorDisputeItem> {
    return this.http.post<VendorDisputeItem>(`${this.apiUrl}/vendors/disputes`, dispute);
  }

  updateDisputeStatus(disputeId: string, status: string): Observable<VendorDisputeItem> {
    return this.http.post<VendorDisputeItem>(`${this.apiUrl}/vendors/disputes/${encodeURIComponent(disputeId)}/status?status=${encodeURIComponent(status)}`, {});
  }

  uploadDataset(fileContent: string, datasetType: string): Observable<DeduplicationReportResponse> {
    return this.http.post<DeduplicationReportResponse>(`${this.apiUrl}/ingest/upload`, {
      fileContent,
      datasetType
    });
  }

  getBusinessUnits(): Observable<string[]> {
    return this.http.get<string[]>(`${this.apiUrl}/vendors/business-units`);
  }

  getAvailableMonths(): Observable<string[]> {
    return this.http.get<string[]>(`${this.apiUrl}/vendors/months`);
  }
}