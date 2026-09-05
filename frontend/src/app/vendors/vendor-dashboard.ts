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
  ClaimEvaluationResponse,
  SlaShieldResponse,
  VendorScorecard
} from '../services/api.service';

@Component({
  selector: 'app-vendor-dashboard',
  standalone: true,
  imports: [CommonModule, DecimalPipe, FormsModule],
  templateUrl: './vendor-dashboard.html',
  styleUrl: './vendor-dashboard.scss'
})
export class VendorDashboardComponent implements OnInit {
  private readonly apiService = inject(ApiService);
  private readonly changeDetectorRef = inject(ChangeDetectorRef);

  activeTab: 'vendors' | 'disputes' = 'vendors';

  // Filtering Controls (Loaded dynamically from Backend)
  availableMonths: string[] = [];
  availableBusinessUnits: string[] = [];
  
  selectedMonth = 'July 2026';
  selectedBusinessUnit = 'All Business Units';

  vendors: VendorScorecard[] = [];
  selectedVendor: VendorScorecard | null = null;
  loading = true;
  backendError: string | null = null;

  // SLA Shield State
  slaShieldResult: SlaShieldResponse | null = null;
  runningShieldAudit = false;
  shieldError: string | null = null;

  // AI Claim Dispute Assistant state
  emailClaimText = 'Subject: SLA Penalty Dispute - July 10 Rainstorm\nVendor: Rohan Mikhailov Travel\nClaim: Severe rain on July 10th delayed 15 cabs on Route 4. Please waive the SLA penalty.';
  claimEvaluationResult: ClaimEvaluationResponse | null = null;
  evaluatingClaim = false;
  claimError: string | null = null;

  ngOnInit(): void {
    this.loadFilterOptions();
    this.loadVendorScorecards();
  }

  loadFilterOptions(): void {
    this.apiService.getBusinessUnits().subscribe({
      next: (units) => {
        if (units && units.length > 0) {
          this.availableBusinessUnits = units;
          this.changeDetectorRef.detectChanges();
        }
      }
    });

    this.apiService.getAvailableMonths().subscribe({
      next: (months) => {
        if (months && months.length > 0) {
          this.availableMonths = months;
          this.changeDetectorRef.detectChanges();
        }
      }
    });
  }

  loadVendorScorecards(): void {
    this.loading = true;
    this.backendError = null;
    const currentVendorName = this.selectedVendor?.vendorName;

    this.apiService.getVendorScorecards(this.selectedMonth, this.selectedBusinessUnit).subscribe({
      next: (data: VendorScorecard[]) => {
        this.vendors = data || [];
        this.setSelectedVendorOrDefault(currentVendorName);
        this.loading = false;
        this.backendError = null;
        if (this.selectedVendor) {
          this.runSlaShieldAudit();
        }
        this.changeDetectorRef.detectChanges();
      },
      error: (err) => {
        this.vendors = [];
        this.selectedVendor = null;
        this.slaShieldResult = null;
        this.loading = false;
        this.backendError = '⚠️ Backend Service Disconnected (http://localhost:8080/api/vendors). Please start Spring Boot backend.';
        this.changeDetectorRef.detectChanges();
      }
    });
  }

  onFilterChange(): void {
    this.loadVendorScorecards();
  }

  selectVendor(vendor: VendorScorecard): void {
    this.selectedVendor = vendor;
    this.slaShieldResult = null;
    this.runningShieldAudit = true;
    this.shieldError = null;
    this.changeDetectorRef.detectChanges();
    this.runSlaShieldAudit();
  }

  runSlaShieldAudit(): void {
    if (!this.selectedVendor) return;
    this.runningShieldAudit = true;
    this.slaShieldResult = null;
    this.shieldError = null;
    this.changeDetectorRef.detectChanges();

    this.apiService.analyzeSlaShield(this.selectedVendor.vendorName, this.selectedMonth, this.selectedBusinessUnit).subscribe({
      next: (res: SlaShieldResponse) => {
        this.runningShieldAudit = false;
        this.slaShieldResult = res;
        this.shieldError = null;
        this.changeDetectorRef.detectChanges();
      },
      error: (err) => {
        this.runningShieldAudit = false;
        this.slaShieldResult = null;
        this.shieldError = '⚠️ Failed to perform AI SLA Shield analysis: Backend server unreachable.';
        this.changeDetectorRef.detectChanges();
      }
    });
  }

  private setSelectedVendorOrDefault(preferredName?: string): void {
    if (preferredName && this.vendors.length > 0) {
      const match = this.vendors.find(v => v.vendorName === preferredName);
      if (match) {
        this.selectedVendor = match;
        return;
      }
    }
    if (this.vendors.length > 0) {
      this.selectedVendor = this.vendors[0];
    }
  }

  loadSampleClaim(type: 'weather' | 'employee' | 'breakdown'): void {
    if (type === 'weather') {
      this.emailClaimText = 'Subject: SLA Penalty Dispute - Heavy Rainstorm\nVendor: Rohan Mikhailov Travel\nClaim: Heavy rain on Route 4 delayed 15 cabs. Please waive the SLA penalty for this shift.';
    } else if (type === 'employee') {
      this.emailClaimText = 'Subject: Delay Penalty Dispute - Late Rider Boarding\nVendor: Meera Pavlov Travel\nClaim: Cabs on Route 12 waited 18 mins at node because 12 riders boarded late. Requesting penalty waiver.';
    } else if (type === 'breakdown') {
      this.emailClaimText = 'Subject: Penalty Waiver Appeal - Cab Engine Breakdown\nVendor: Sanjay Mikhailov Travel\nClaim: Cab KA-03-MK-9910 had engine failure. Requesting SLA penalty waiver for delayed replacement.';
    }
    this.claimEvaluationResult = null;
    this.claimError = null;
  }

  evaluateEmailClaim(): void {
    this.evaluatingClaim = true;
    this.claimEvaluationResult = null;
    this.claimError = null;
    this.changeDetectorRef.detectChanges();

    this.apiService.evaluateVendorClaim(this.emailClaimText, this.selectedMonth).subscribe({
      next: (res: ClaimEvaluationResponse) => {
        this.evaluatingClaim = false;
        this.claimEvaluationResult = res;
        this.claimError = null;
        this.changeDetectorRef.detectChanges();
      },
      error: (err) => {
        this.evaluatingClaim = false;
        this.claimEvaluationResult = null;
        this.claimError = '⚠️ Unable to evaluate claim: Backend server offline (http://localhost:8080/api/vendors/evaluate-claim).';
        this.changeDetectorRef.detectChanges();
      }
    });
  }
}
