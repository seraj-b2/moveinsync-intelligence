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

  getVendorScorecards(): Observable<VendorScorecard[]> {
    return this.http.get<VendorScorecard[]>(`${this.apiUrl}/vendors/scorecards`);
  }
}