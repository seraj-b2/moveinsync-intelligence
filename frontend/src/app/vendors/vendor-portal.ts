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
  SlaShieldResponse,
  VendorDisputeItem,
  VendorScorecard
} from '../services/api.service';

@Component({
  selector: 'app-vendor-portal',
  standalone: true,
  imports: [CommonModule, DecimalPipe, FormsModule],
  templateUrl: './vendor-portal.html',
  styleUrl: './vendor-portal.scss'
})
export class VendorPortalComponent implements OnInit {
  private readonly apiService = inject(ApiService);
  private readonly changeDetectorRef = inject(ChangeDetectorRef);

  // Vendor Login State
  isLoggedIn = false;
  selectedVendorName = 'Rohan Mikhailov Travel';
  loggedInVendor = '';

  vendorOptions: string[] = [
    'Rohan Mikhailov Travel',
    'Sanjay Mikhailov Travel',
    'Meera Pavlov Travel',
    'Priya Mikhailov Travel',
    'Aarav Mikhailov Travel',
    'Anjali Mikhailov Travel',
    'Devansh Popov Transport',
    'Ishaan Smith Transit'
  ];

  activeTab: 'performance' | 'disputes' = 'performance';

  // Filters
  availableMonths: string[] = [];
  availableBusinessUnits: string[] = [];
  selectedMonth = 'July 2026';
  selectedBusinessUnit = 'All Business Units';

  // Single Vendor Data
  vendorScorecard: VendorScorecard | null = null;
  loadingData = false;
  backendError: string | null = null;

  // SLA Shield State
  slaShieldResult: SlaShieldResponse | null = null;
  runningShieldAudit = false;
  shieldError: string | null = null;

  // Vendor Dispute History
  vendorDisputes: VendorDisputeItem[] = [];
  loadingDisputes = false;

  // Submit New Dispute Form State
  newDisputeRoute = 'Route 4';
  newDisputeSubject = '';
  newDisputeClaimText = '';
  newDisputeCabs = 5;
  submittingDispute = false;
  disputeSubmittedSuccess: string | null = null;
  disputeSubmitError: string | null = null;

  ngOnInit(): void {
    this.loadFilterOptions();
  }

  loadFilterOptions(): void {
    this.apiService.getBusinessUnits().subscribe({
      next: (units) => {
        if (units && units.length > 0) {
          this.availableBusinessUnits = units;
        }
      }
    });

    this.apiService.getAvailableMonths().subscribe({
      next: (months) => {
        if (months && months.length > 0) {
          this.availableMonths = months;
        }
      }
    });
  }

  login(): void {
    if (!this.selectedVendorName) return;
    this.loggedInVendor = this.selectedVendorName;
    this.isLoggedIn = true;
    this.loadVendorPortalData();
  }

  logout(): void {
    this.isLoggedIn = false;
    this.loggedInVendor = '';
    this.vendorScorecard = null;
    this.slaShieldResult = null;
    this.vendorDisputes = [];
  }

  onFilterChange(): void {
    if (this.isLoggedIn) {
      this.loadVendorPortalData();
    }
  }

  loadVendorPortalData(): void {
    if (!this.loggedInVendor) return;
    this.loadingData = true;
    this.backendError = null;

    // Fetch Scorecard for logged-in vendor
    this.apiService.getVendorScorecards(this.selectedMonth, this.selectedBusinessUnit).subscribe({
      next: (scorecards) => {
        const match = scorecards.find(s => s.vendorName === this.loggedInVendor);
        this.vendorScorecard = match || (scorecards.length > 0 ? scorecards[0] : null);
        this.loadingData = false;
        this.changeDetectorRef.detectChanges();
      },
      error: (err) => {
        this.loadingData = false;
        this.backendError = '⚠️ Backend Service Offline (http://localhost:8080/api/vendors).';
        this.changeDetectorRef.detectChanges();
      }
    });

    // Run SLA Shield for logged-in vendor
    this.runningShieldAudit = true;
    this.shieldError = null;
    this.apiService.analyzeSlaShield(this.loggedInVendor, this.selectedMonth, this.selectedBusinessUnit).subscribe({
      next: (shield) => {
        this.slaShieldResult = shield;
        this.runningShieldAudit = false;
        this.changeDetectorRef.detectChanges();
      },
      error: (err) => {
        this.runningShieldAudit = false;
        this.shieldError = '⚠️ Unable to fetch AI SLA Protection audit.';
        this.changeDetectorRef.detectChanges();
      }
    });

    // Fetch disputes for logged-in vendor
    this.loadingDisputes = true;
    this.apiService.getVendorDisputes(this.selectedMonth, this.selectedBusinessUnit, this.loggedInVendor).subscribe({
      next: (disputes) => {
        this.vendorDisputes = disputes || [];
        this.loadingDisputes = false;
        this.changeDetectorRef.detectChanges();
      },
      error: (err) => {
        this.loadingDisputes = false;
        this.changeDetectorRef.detectChanges();
      }
    });
  }

  submitNewDispute(): void {
    if (!this.newDisputeSubject || !this.newDisputeClaimText) {
      this.disputeSubmitError = 'Please fill out both the Dispute Subject and Details.';
      return;
    }

    this.submittingDispute = true;
    this.disputeSubmitError = null;
    this.disputeSubmittedSuccess = null;

    const disputeItem: VendorDisputeItem = {
      disputeId: `DSP-${Math.floor(1000 + Math.random() * 9000)}`,
      vendorName: this.loggedInVendor,
      route: this.newDisputeRoute,
      claimSubject: this.newDisputeSubject,
      claimText: `Subject: ${this.newDisputeSubject}\nVendor: ${this.loggedInVendor}\nRoute: ${this.newDisputeRoute}\nClaim: ${this.newDisputeClaimText}`,
      month: this.selectedMonth,
      businessUnit: this.selectedBusinessUnit,
      affectedCabs: this.newDisputeCabs,
      status: 'PENDING_REVIEW',
      submittedAt: 'Just now'
    };

    this.apiService.submitVendorDispute(disputeItem).subscribe({
      next: (created) => {
        this.submittingDispute = false;
        this.vendorDisputes.unshift(created || disputeItem);
        this.disputeSubmittedSuccess = `Dispute claim #${disputeItem.disputeId} submitted successfully to MoveInSync Admin!`;
        this.newDisputeSubject = '';
        this.newDisputeClaimText = '';
        this.changeDetectorRef.detectChanges();

        setTimeout(() => {
          this.disputeSubmittedSuccess = null;
          this.changeDetectorRef.detectChanges();
        }, 5000);
      },
      error: (err) => {
        this.submittingDispute = false;
        this.vendorDisputes.unshift(disputeItem);
        this.disputeSubmittedSuccess = `Dispute claim #${disputeItem.disputeId} logged locally!`;
        this.newDisputeSubject = '';
        this.newDisputeClaimText = '';
        this.changeDetectorRef.detectChanges();
      }
    });
  }

  loadSampleClaimTemplate(type: 'rain' | 'rider' | 'traffic'): void {
    if (type === 'rain') {
      this.newDisputeSubject = 'Monsoon Heavy Rainstorm Delay Appeal';
      this.newDisputeRoute = 'Route 4';
      this.newDisputeCabs = 8;
      this.newDisputeClaimText = 'Severe rainstorm on Route 4 delayed 8 cabs by 22 minutes. Requesting SLA penalty waiver as traffic was stopped by waterlogging.';
    } else if (type === 'rider') {
      this.newDisputeSubject = 'Late Employee Boarding Penalty Waiver';
      this.newDisputeRoute = 'Route 12';
      this.newDisputeCabs = 4;
      this.newDisputeClaimText = 'Cabs waited 18 minutes at pickup node because 10 riders boarded late. Driver arrived on time. Requesting penalty exemption.';
    } else if (type === 'traffic') {
      this.newDisputeSubject = 'ORR Highway Gate Traffic Bottleneck';
      this.newDisputeRoute = 'Route 2';
      this.newDisputeCabs = 6;
      this.newDisputeClaimText = 'Unannounced Outer Ring Road highway construction bottleneck delayed cabs by 15 mins. Requesting SLA waiver.';
    }
  }
}
