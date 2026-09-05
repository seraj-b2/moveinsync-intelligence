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

@Injectable({
  providedIn: 'root'
})
export class ApiService {
  private readonly http = inject(HttpClient);

  private readonly apiUrl = 'http://localhost:8080/api';

  getHealth(): Observable<HealthResponse> {
    return this.http.get<HealthResponse>(
      `${this.apiUrl}/health`
    );
  }

  getDashboardSummary(): Observable<DashboardSummary> {
    return this.http.get<DashboardSummary>(
      `${this.apiUrl}/dashboard/summary`
    );
  }
}