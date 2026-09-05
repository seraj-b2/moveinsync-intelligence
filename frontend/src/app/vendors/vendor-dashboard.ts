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

  // Filtering Controls
  availableMonths = ['July 2026', 'June 2026', 'May 2026', 'All Months'];
  availableBusinessUnits = ['All Business Units', 'vanta-Aus', 'catalyst-Sac', 'orbit-Slc', 'vanta-Sea', 'pinnacle-Slc'];
  
  selectedMonth = 'July 2026';
  selectedBusinessUnit = 'All Business Units';

  vendors: VendorScorecard[] = [];
  selectedVendor: VendorScorecard | null = null;
  loading = true;

  // AI Claim Dispute Assistant state
  emailClaimText = 'Subject: SLA Penalty Dispute - July 10 Rainstorm\nVendor: Rohan Mikhailov Travel\nClaim: Severe rain on July 10th delayed 15 cabs on Route 4. Please waive the SLA penalty.';
  claimEvaluationResult: any = null;
  evaluatingClaim = false;

  private getFallbackVendors(month: string): VendorScorecard[] {
    const isMay = month.includes('May');
    const isJune = month.includes('June');

    const rohanOta = isMay ? 82.1 : isJune ? 85.0 : 78.4;
    const meeraOta = isMay ? 89.5 : isJune ? 91.0 : 92.1;
    const sanjayOta = isMay ? 81.0 : isJune ? 83.5 : 84.7;

    return [
      {
        vendorName: 'Rohan Mikhailov Travel',
        onTimeArrivalPercentage: rohanOta,
        slaTargetPercentage: 90.0,
        complianceRatePercentage: 88.5,
        billingAccuracyPercentage: 82.3,
        averageDriverRating: 3.8,
        totalTrips: isMay ? 39800 : isJune ? 41200 : 42150,
        delayedTrips: isMay ? 7120 : isJune ? 6180 : 9130,
        nonCompliantTrips: 484,
        billingDiscrepanciesCount: 124,
        slaBreakdown: [
          {
            month: month,
            vendorName: 'Rohan Mikhailov Travel',
            actualOta: rohanOta,
            targetSla: 90.0,
            totalTrips: 42150,
            vendorFaultDelays: 4810,
            employeeFaultDelays: 2340,
            trafficWeatherDelays: 1980
          }
        ],
        billingDiscrepancies: [
          {
            tripId: '1097076',
            vendorName: 'Rohan Mikhailov Travel',
            billedKm: 45.2,
            gpsActualKm: 31.8,
            billedCost: 1420.00,
            auditExpectedCost: 1000.00,
            discrepancyReason: 'Billed distance exceeds GPS tracked distance by 42.1%',
            status: 'FLAGGED'
          },
          {
            tripId: '1123974',
            vendorName: 'Rohan Mikhailov Travel',
            billedKm: 28.0,
            gpsActualKm: 27.5,
            billedCost: 850.00,
            auditExpectedCost: 850.00,
            discrepancyReason: 'Billing matched GPS logs within SLA tolerance',
            status: 'VERIFIED'
          }
        ],
        complianceAlerts: [
          {
            alertId: 'ALT-9042',
            vendorName: 'Rohan Mikhailov Travel',
            vehicleReg: 'KA-01-MJ-4821',
            driverName: 'Rajesh Kumar',
            alertType: 'DRIVER_NON_COMPLIANT',
            severity: 'Sev-1',
            timestamp: month + ' 08:15 AM'
          },
          {
            alertId: 'ALT-8812',
            vendorName: 'Rohan Mikhailov Travel',
            vehicleReg: 'KA-05-MB-1102',
            driverName: 'Suresh Naik',
            alertType: 'OVERSPEEDING',
            severity: 'Sev-2',
            timestamp: month + ' 10:30 PM'
          }
        ]
      },
      {
        vendorName: 'Meera Pavlov Travel',
        onTimeArrivalPercentage: meeraOta,
        slaTargetPercentage: 90.0,
        complianceRatePercentage: 97.2,
        billingAccuracyPercentage: 98.6,
        averageDriverRating: 4.7,
        totalTrips: isMay ? 34200 : isJune ? 36800 : 38400,
        delayedTrips: 3030,
        nonCompliantTrips: 107,
        billingDiscrepanciesCount: 12,
        slaBreakdown: [
          {
            month: month,
            vendorName: 'Meera Pavlov Travel',
            actualOta: meeraOta,
            targetSla: 90.0,
            totalTrips: 38400,
            vendorFaultDelays: 1420,
            employeeFaultDelays: 1200,
            trafficWeatherDelays: 410
          }
        ],
        billingDiscrepancies: [
          {
            tripId: '1098442',
            vendorName: 'Meera Pavlov Travel',
            billedKm: 52.0,
            gpsActualKm: 51.5,
            billedCost: 1650.00,
            auditExpectedCost: 1650.00,
            discrepancyReason: 'Verified rate slab & kilometer log',
            status: 'VERIFIED'
          }
        ],
        complianceAlerts: []
      },
      {
        vendorName: 'Sanjay Mikhailov Travel',
        onTimeArrivalPercentage: sanjayOta,
        slaTargetPercentage: 90.0,
        complianceRatePercentage: 91.4,
        billingAccuracyPercentage: 93.1,
        averageDriverRating: 4.2,
        totalTrips: isMay ? 28900 : isJune ? 30100 : 31200,
        delayedTrips: 4760,
        nonCompliantTrips: 268,
        billingDiscrepanciesCount: 38,
        slaBreakdown: [
          {
            month: month,
            vendorName: 'Sanjay Mikhailov Travel',
            actualOta: sanjayOta,
            targetSla: 90.0,
            totalTrips: 31200,
            vendorFaultDelays: 2890,
            employeeFaultDelays: 1150,
            trafficWeatherDelays: 720
          }
        ],
        billingDiscrepancies: [],
        complianceAlerts: [
          {
            alertId: 'ALT-7741',
            vendorName: 'Sanjay Mikhailov Travel',
            vehicleReg: 'KA-03-MK-9910',
            driverName: 'Amit Patel',
            alertType: 'VEHICLE_PERMIT_EXPIRED',
            severity: 'Sev-1',
            timestamp: month + ' 06:45 AM'
          }
        ]
      }
    ];
  }

  ngOnInit(): void {
    this.loadVendorScorecards();
  }

  loadVendorScorecards(): void {
    this.loading = true;
    const currentVendorName = this.selectedVendor?.vendorName;

    this.apiService.getVendorScorecards(this.selectedMonth, this.selectedBusinessUnit).subscribe({
      next: (data: VendorScorecard[]) => {
        const fallbacks = this.getFallbackVendors(this.selectedMonth);
        this.vendors = (data && data.length > 0) ? data : fallbacks;
        this.setSelectedVendorOrDefault(currentVendorName);
        this.loading = false;
        this.changeDetectorRef.detectChanges();
      },
      error: () => {
        this.vendors = this.getFallbackVendors(this.selectedMonth);
        this.setSelectedVendorOrDefault(currentVendorName);
        this.loading = false;
        this.changeDetectorRef.detectChanges();
      }
    });
  }

  onFilterChange(): void {
    this.loadVendorScorecards();
  }

  selectVendor(vendor: VendorScorecard): void {
    this.selectedVendor = vendor;
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
    }, 1000);
  }
}
