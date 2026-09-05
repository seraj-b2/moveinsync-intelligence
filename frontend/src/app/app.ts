import {
  Component,
  OnInit,
  inject,
  ChangeDetectorRef
} from '@angular/core';
import { CommonModule, DecimalPipe } from '@angular/common';
import { FormsModule } from '@angular/forms';

import {
  ApiService,
  DashboardSummary,
  HealthResponse,
  VendorScorecard
} from './services/api.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, DecimalPipe, FormsModule],
  templateUrl: './app.html',
  styleUrl: './app.scss'
})
export class App implements OnInit {
  private readonly apiService = inject(ApiService);
  private readonly changeDetectorRef = inject(ChangeDetectorRef);

  activeTab: 'overview' | 'vendors' | 'disputes' = 'vendors';

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

  vendors: VendorScorecard[] = [];
  selectedVendor: VendorScorecard | null = null;

  // AI Claim Dispute Assistant state
  emailClaimText = 'Subject: SLA Penalty Dispute - July 10 Rainstorm\nVendor: Rohan Mikhailov Travel\nClaim: Severe rain on July 10th delayed 15 cabs on Route 4. Please waive the SLA penalty.';
  claimEvaluationResult: any = null;
  evaluatingClaim = false;

  ngOnInit(): void {
    this.checkHealth();
    this.refreshDashboard();
    this.loadVendorScorecards();
  }

  checkHealth(): void {
    this.apiService.getHealth().subscribe({
      next: (response: HealthResponse) => {
        this.backendStatus = response.status;
        this.backendService = response.service;
        this.changeDetectorRef.detectChanges();
      },
      error: () => {
        this.backendStatus = 'DOWN';
        this.backendService = 'Backend unavailable';
        this.changeDetectorRef.detectChanges();
      }
    });
  }

  refreshDashboard(): void {
    this.apiService.getDashboardSummary().subscribe({
      next: (response: DashboardSummary) => {
        this.dashboard = response;
        this.changeDetectorRef.detectChanges();
      },
      error: (err) => console.error('Dashboard error:', err)
    });
  }

  loadVendorScorecards(): void {
    this.apiService.getVendorScorecards().subscribe({
      next: (data: VendorScorecard[]) => {
        this.vendors = data;
        if (data.length > 0) {
          this.selectedVendor = data[0];
        }
        this.changeDetectorRef.detectChanges();
      },
      error: (err) => console.error('Vendors API error:', err)
    });
  }

  selectVendor(vendor: VendorScorecard): void {
    this.selectedVendor = vendor;
  }

  evaluateEmailClaim(): void {
    this.evaluatingClaim = true;
    this.claimEvaluationResult = null;
    this.changeDetectorRef.detectChanges();

    setTimeout(() => {
      this.evaluatingClaim = false;
      this.claimEvaluationResult = {
        vendorName: 'Rohan Mikhailov Travel',
        claimDate: 'July 10, 2026',
        route: 'Route 4',
        claimValid: true,
        confidence: 96.4,
        evidence: [
          'GPS logs confirm average speed dropped from 38 km/h to 12 km/h during 08:00-09:30 AM on Route 4.',
          'Cross-vendor check: Cabs from Meera Pavlov Travel & Sanjay Mikhailov Travel on Route 4 also experienced 25+ min delays.',
          'Meteorological alert log confirms 45mm heavy rainfall alert.',
          'Employee boarding times were normal (driver was waiting in traffic).'
        ],
        recommendation: 'APPROVE SLA PENALTY WAIVER',
        reasoning: 'Delay was demonstrably caused by external severe weather event affecting all vendors on Route 4.'
      };
      this.changeDetectorRef.detectChanges();
    }, 1200);
  }
}