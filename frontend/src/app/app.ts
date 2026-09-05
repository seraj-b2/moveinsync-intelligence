import {
  Component,
  OnInit,
  inject,
  ChangeDetectorRef
} from '@angular/core';
import { DecimalPipe } from '@angular/common';

import {
  ApiService,
  DashboardSummary,
  HealthResponse
} from './services/api.service';

@Component({
  selector: 'app-root',
  imports: [DecimalPipe],
  templateUrl: './app.html',
  styleUrl: './app.scss'
})
export class App implements OnInit {
  private readonly apiService = inject(ApiService);
  private readonly changeDetectorRef = inject(ChangeDetectorRef);

  backendStatus = 'Checking...';
  backendService = '';

  dashboard: DashboardSummary = {
    totalTrips: 0,
    activeVehicles: 0,
    onTimePercentage: 0,
    delayedTrips: 0,
    employeesTransported: 0,
    routeUtilization: 0
  };

  refreshDashboard(): void {
    this.apiService.getDashboardSummary().subscribe({
      next: (response: DashboardSummary) => {
        this.dashboard = response;
        this.changeDetectorRef.detectChanges();
      },
      error: (error) => {
        console.error('Dashboard refresh failed:', error);
      }
    });
  }

  ngOnInit(): void {
   this.apiService.getHealth().subscribe({
    next: (response: HealthResponse) => {
      console.log('Health API response:', response);

      this.backendStatus = response.status;
      this.backendService = response.service;

      this.changeDetectorRef.detectChanges();
    },
    error: (error) => {
      console.error('Health API error:', error);

      this.backendStatus = 'DOWN';
      this.backendService = 'Backend unavailable';

      this.changeDetectorRef.detectChanges();
    }
  });

    this.apiService.getDashboardSummary().subscribe({
      next: (response: DashboardSummary) => {
        console.log('Dashboard API response:', response);

        this.dashboard = response;

        this.changeDetectorRef.detectChanges();
      },
      error: (error) => {
        console.error('Dashboard API error:', error);
      }
    });

  }
}