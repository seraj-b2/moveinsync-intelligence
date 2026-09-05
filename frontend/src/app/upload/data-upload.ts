import {
  Component,
  inject,
  ChangeDetectorRef
} from '@angular/core';
import { CommonModule, DecimalPipe } from '@angular/common';
import { FormsModule } from '@angular/forms';

import {
  ApiService,
  DeduplicationReportResponse
} from '../services/api.service';

@Component({
  selector: 'app-data-upload',
  standalone: true,
  imports: [CommonModule, DecimalPipe, FormsModule],
  templateUrl: './data-upload.html',
  styleUrl: './data-upload.scss'
})
export class DataUploadComponent {
  private readonly apiService = inject(ApiService);
  private readonly changeDetectorRef = inject(ChangeDetectorRef);

  selectedDatasetType: 'BILLING' | 'DISPUTES' | 'ALERTS' = 'BILLING';
  rawFileContent = '';
  isUploading = false;
  uploadError: string | null = null;
  reportResult: DeduplicationReportResponse | null = null;

  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      const file = input.files[0];
      const reader = new FileReader();
      reader.onload = (e) => {
        this.rawFileContent = e.target?.result as string || '';
        this.changeDetectorRef.detectChanges();
      };
      reader.readAsText(file);
    }
  }

  processUpload(): void {
    if (!this.rawFileContent || this.rawFileContent.trim().length === 0) {
      this.uploadError = 'Please select a file or paste dataset content in the text editor below.';
      return;
    }

    this.isUploading = true;
    this.uploadError = null;
    this.reportResult = null;
    this.changeDetectorRef.detectChanges();

    this.apiService.uploadDataset(this.rawFileContent, this.selectedDatasetType).subscribe({
      next: (res: DeduplicationReportResponse) => {
        this.isUploading = false;
        this.reportResult = res;
        this.uploadError = null;
        this.changeDetectorRef.detectChanges();
      },
      error: (err) => {
        this.isUploading = false;
        this.uploadError = '⚠️ Unable to process data upload: Backend ingestion service unavailable.';
        this.changeDetectorRef.detectChanges();
      }
    });
  }

  loadSampleDataset(type: 'billing_with_duplicates' | 'disputes_json' | 'alerts_json'): void {
    if (type === 'billing_with_duplicates') {
      this.selectedDatasetType = 'BILLING';
      this.rawFileContent = `tripId,vendorName,billedKm,gpsActualKm,billedCost,auditExpectedCost,discrepancyReason
TRP-8841,Rohan Mikhailov Travel,42.5,34.1,115.00,92.50,"GPS tracked distance lower"
TRP-8841,Rohan Mikhailov Travel,42.5,34.1,115.00,92.50,"GPS tracked distance lower (DUPLICATE)"
TRP-7712,Sanjay Mikhailov Travel,58.0,46.2,160.00,128.00,"Unapproved detour mileage"
TRP-7712,Sanjay Mikhailov Travel,58.0,46.2,160.00,128.00,"Unapproved detour mileage (DUPLICATE)"
TRP-9923,Meera Pavlov Travel,32.0,31.8,85.00,85.00,"Normal verified route"
TRP-9924,Priya Mikhailov Travel,64.2,52.0,180.00,145.00,"High odometer variance"`;
    } else if (type === 'disputes_json') {
      this.selectedDatasetType = 'DISPUTES';
      this.rawFileContent = `[
  {
    "disputeId": "DSP-9041",
    "vendorName": "Rohan Mikhailov Travel",
    "route": "Route 4",
    "claimSubject": "Monsoon Heavy Rainstorm Delay Claim",
    "claimText": "Severe rain on Route 4 delayed 15 cabs.",
    "affectedCabs": 15
  },
  {
    "disputeId": "DSP-9041",
    "vendorName": "Rohan Mikhailov Travel",
    "route": "Route 4",
    "claimSubject": "Monsoon Heavy Rainstorm Delay Claim (DUPLICATE)",
    "claimText": "Severe rain on Route 4 delayed 15 cabs.",
    "affectedCabs": 15
  },
  {
    "disputeId": "DSP-5511",
    "vendorName": "Devansh Popov Transport",
    "route": "Route 10",
    "claimSubject": "Airport Gate Toll Bottleneck Waiver",
    "claimText": "Airport toll plaza congestion delayed 5 cabs.",
    "affectedCabs": 5
  }
]`;
    } else if (type === 'alerts_json') {
      this.selectedDatasetType = 'ALERTS';
      this.rawFileContent = `[
  {
    "alertId": "ALT-104",
    "vendorName": "Rohan Mikhailov Travel",
    "vehicleReg": "KA-03-MK-9910",
    "driverName": "Vikram Singh",
    "alertType": "Cab Speeding Violation (>85 km/h)",
    "severity": "Sev-1"
  },
  {
    "alertId": "ALT-104",
    "vendorName": "Rohan Mikhailov Travel",
    "vehicleReg": "KA-03-MK-9910",
    "driverName": "Vikram Singh",
    "alertType": "Cab Speeding Violation (>85 km/h) (DUPLICATE)",
    "severity": "Sev-1"
  },
  {
    "alertId": "ALT-208",
    "vendorName": "Ishaan Smith Transit",
    "vehicleReg": "KA-01-AB-1234",
    "driverName": "Amit Sharma",
    "alertType": "Unscheduled Route Deviation Alert",
    "severity": "Sev-2"
  }
]`;
    }
    this.reportResult = null;
    this.uploadError = null;
  }
}
