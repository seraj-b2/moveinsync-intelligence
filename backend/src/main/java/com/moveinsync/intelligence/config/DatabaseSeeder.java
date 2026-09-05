package com.moveinsync.intelligence.config;

import com.moveinsync.intelligence.entity.BillingDiscrepancyEntity;
import com.moveinsync.intelligence.entity.ComplianceAlertEntity;
import com.moveinsync.intelligence.entity.VendorDisputeEntity;
import com.moveinsync.intelligence.repository.BillingDiscrepancyRepository;
import com.moveinsync.intelligence.repository.ComplianceAlertRepository;
import com.moveinsync.intelligence.repository.VendorDisputeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DatabaseSeeder implements CommandLineRunner {

    private final VendorDisputeRepository disputeRepository;
    private final BillingDiscrepancyRepository billingRepository;
    private final ComplianceAlertRepository alertRepository;

    public DatabaseSeeder(VendorDisputeRepository disputeRepository,
                          BillingDiscrepancyRepository billingRepository,
                          ComplianceAlertRepository alertRepository) {
        this.disputeRepository = disputeRepository;
        this.billingRepository = billingRepository;
        this.alertRepository = alertRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (disputeRepository.count() == 0) {
            disputeRepository.save(new VendorDisputeEntity(
                    "DSP-9041", "Rohan Mikhailov Travel", "Route 4",
                    "Monsoon Heavy Rainstorm Delay Claim",
                    "Subject: SLA Penalty Dispute - Monsoon Rainstorm\nVendor: Rohan Mikhailov Travel\nClaim: Severe rain on Route 4 delayed 15 cabs by 25 minutes. Requesting SLA penalty waiver for this shift.",
                    "July 2026", "All Business Units", 15, "PENDING_REVIEW", "July 2026 09:30 AM"
            ));

            disputeRepository.save(new VendorDisputeEntity(
                    "DSP-8812", "Meera Pavlov Travel", "Route 12",
                    "Late Rider Node Boarding Claim",
                    "Subject: Penalty Waiver Appeal - Late Employee Boarding\nVendor: Meera Pavlov Travel\nClaim: Cabs on Route 12 waited 18 mins at pickup node because 12 riders boarded late. Driver arrived on time at node.",
                    "July 2026", "All Business Units", 8, "PENDING_REVIEW", "July 2026 02:15 PM"
            ));

            disputeRepository.save(new VendorDisputeEntity(
                    "DSP-7741", "Sanjay Mikhailov Travel", "Route 8",
                    "Cab Engine Failure Dispute",
                    "Subject: Penalty Dispute - Cab Engine Breakdown\nVendor: Sanjay Mikhailov Travel\nClaim: Cab KA-03-MK-9910 experienced sudden engine failure on Route 8. Requesting waiver for delayed backup replacement.",
                    "July 2026", "All Business Units", 1, "PENDING_REVIEW", "July 2026 05:40 PM"
            ));

            disputeRepository.save(new VendorDisputeEntity(
                    "DSP-6620", "Priya Mikhailov Travel", "Route 2",
                    "Outer Ring Road Traffic Gate Congestion",
                    "Subject: Delay Penalty Dispute - Road Closure & Traffic Gate\nVendor: Priya Mikhailov Travel\nClaim: Heavy Outer Ring Road traffic bottleneck delayed 6 cabs servicing All Business Units by 20 minutes.",
                    "July 2026", "All Business Units", 6, "PENDING_REVIEW", "July 2026 11:10 AM"
            ));
        }

        if (billingRepository.count() == 0) {
            billingRepository.save(new BillingDiscrepancyEntity(
                    "TRP-8841", "Rohan Mikhailov Travel", "July 2026", "All Business Units",
                    42.5, 34.1, 115.00, 92.50, "GPS tracked distance 8.4 km lower than billed odometer", "FLAGGED"
            ));
            billingRepository.save(new BillingDiscrepancyEntity(
                    "TRP-7712", "Sanjay Mikhailov Travel", "July 2026", "All Business Units",
                    58.0, 46.2, 160.00, 128.00, "Unapproved detour mileage charged in contract invoice", "FLAGGED"
            ));
        }

        if (alertRepository.count() == 0) {
            alertRepository.save(new ComplianceAlertEntity(
                    "ALT-104", "Rohan Mikhailov Travel", "July 2026", "All Business Units",
                    "KA-03-MK-9910", "Vikram Singh", "Cab Speeding Violation (>85 km/h)", "Sev-1", "July 14 08:22 AM"
            ));
            alertRepository.save(new ComplianceAlertEntity(
                    "ALT-105", "Meera Pavlov Travel", "July 2026", "All Business Units",
                    "KA-05-AB-4412", "Rajesh Kumar", "Pending Driver Document Renewal", "Sev-2", "July 12 11:45 AM"
            ));
        }
    }
}
