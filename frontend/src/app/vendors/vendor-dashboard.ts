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
  VendorDisputeItem,
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

  // Pending Vendor Disputes state
  pendingDisputes: VendorDisputeItem[] = [];
  selectedDispute: VendorDisputeItem | null = null;
  loadingDisputes = false;
  disputeError: string | null = null;

  // AI Claim Dispute Assistant state
  emailClaimText = '';
  claimEvaluationResult: ClaimEvaluationResponse | null = null;
  evaluatingClaim = false;
  claimError: string | null = null;

  ngOnInit(): void {
    this.loadFilterOptions();
    this.loadVendorScorecards();
    this.loadPendingDisputes();
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
    this.loadPendingDisputes();
  }

  loadPendingDisputes(): void {
    this.loadingDisputes = true;
    this.disputeError = null;
    this.apiService.getVendorDisputes(undefined, undefined, undefined, 'PENDING_REVIEW').subscribe({
      next: (data: VendorDisputeItem[]) => {
        this.pendingDisputes = data || [];
        this.loadingDisputes = false;
        this.disputeError = null;
        if (this.pendingDisputes.length > 0) {
          if (!this.selectedDispute || !this.pendingDisputes.some(d => d.disputeId === this.selectedDispute?.disputeId)) {
            this.selectPendingDispute(this.pendingDisputes[0]);
          }
        } else {
          this.selectedDispute = null;
          this.emailClaimText = '';
        }
        this.changeDetectorRef.detectChanges();
      },
      error: (err) => {
        this.pendingDisputes = [];
        this.loadingDisputes = false;
        this.disputeError = '⚠️ Unable to fetch pending vendor disputes from backend.';
        this.changeDetectorRef.detectChanges();
      }
    });
  }

  selectPendingDispute(dispute: VendorDisputeItem): void {
    this.selectedDispute = dispute;
    this.emailClaimText = dispute.claimText;
    this.claimEvaluationResult = null;
    this.claimError = null;
    this.actionConfirmedMessage = null;
    this.changeDetectorRef.detectChanges();
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
    this.selectedDispute = null;
    this.claimEvaluationResult = null;
    this.claimError = null;
  }

  get filteredEvaluatedClaims() {
    return this.recentEvaluatedClaims;
  }

  recentEvaluatedClaims = [
    {
      claimId: 'CLM-9041',
      vendorName: 'Rohan Mikhailov Travel',
      route: 'Route 4',
      reason: 'Monsoon Heavy Rainstorm - Approved Waiver',
      status: 'APPROVED',
      date: 'July 2026'
    },
    {
      claimId: 'CLM-8812',
      vendorName: 'Sanjay Mikhailov Travel',
      route: 'Route 12',
      reason: 'Vehicle Maintenance Alert - Fault Confirmed',
      status: 'REJECTED',
      date: 'July 2026'
    }
  ];
  actionConfirmedMessage: string | null = null;

  confirmWaiverAction(status: 'APPROVED' | 'REJECTED'): void {
    if (!this.claimEvaluationResult) return;
    const currentDispute = this.selectedDispute;
    const disputeId = currentDispute ? currentDispute.disputeId : `CLM-${Math.floor(1000 + Math.random() * 9000)}`;
    const vendorName = currentDispute ? currentDispute.vendorName : (this.claimEvaluationResult.vendorName || 'Vendor');
    const route = currentDispute ? currentDispute.route : (this.claimEvaluationResult.route || 'Route');

    if (currentDispute) {
      this.apiService.updateDisputeStatus(currentDispute.disputeId, status).subscribe({
        next: () => {},
        error: () => {}
      });
    }

    this.recentEvaluatedClaims.unshift({
      claimId: disputeId,
      vendorName: vendorName,
      route: route,
      reason: this.claimEvaluationResult.recommendation,
      status: status,
      date: this.selectedMonth
    });

    if (currentDispute) {
      this.pendingDisputes = this.pendingDisputes.filter(d => d.disputeId !== currentDispute.disputeId);
      if (this.pendingDisputes.length > 0) {
        this.selectPendingDispute(this.pendingDisputes[0]);
      } else {
        this.selectedDispute = null;
        this.emailClaimText = '';
        this.claimEvaluationResult = null;
      }
    }

    this.actionConfirmedMessage = `Decision recorded: Dispute ${disputeId} for ${vendorName} ${status === 'APPROVED' ? 'Waived & Approved' : 'Rejected'}!`;
    this.changeDetectorRef.detectChanges();
    setTimeout(() => {
      this.actionConfirmedMessage = null;
      this.changeDetectorRef.detectChanges();
    }, 4000);
  }

  evaluateEmailClaim(): void {
    this.evaluatingClaim = true;
    this.claimEvaluationResult = null;
    this.claimError = null;
    this.actionConfirmedMessage = null;
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
