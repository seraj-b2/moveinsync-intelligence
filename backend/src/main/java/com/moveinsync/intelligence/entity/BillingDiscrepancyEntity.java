package com.moveinsync.intelligence.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "billing_discrepancies")
public class BillingDiscrepancyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "trip_id")
    private String tripId;

    @Column(name = "vendor_name")
    private String vendorName;

    @Column(name = "month")
    private String month;

    @Column(name = "business_unit")
    private String businessUnit;

    @Column(name = "billed_km")
    private double billedKm;

    @Column(name = "gps_actual_km")
    private double gpsActualKm;

    @Column(name = "billed_cost")
    private double billedCost;

    @Column(name = "audit_expected_cost")
    private double auditExpectedCost;

    @Column(name = "discrepancy_reason")
    private String discrepancyReason;

    @Column(name = "status")
    private String status;

    public BillingDiscrepancyEntity() {}

    public BillingDiscrepancyEntity(String tripId, String vendorName, String month, String businessUnit,
                                    double billedKm, double gpsActualKm, double billedCost,
                                    double auditExpectedCost, String discrepancyReason, String status) {
        this.tripId = tripId;
        this.vendorName = vendorName;
        this.month = month;
        this.businessUnit = businessUnit;
        this.billedKm = billedKm;
        this.gpsActualKm = gpsActualKm;
        this.billedCost = billedCost;
        this.auditExpectedCost = auditExpectedCost;
        this.discrepancyReason = discrepancyReason;
        this.status = status;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTripId() { return tripId; }
    public void setTripId(String tripId) { this.tripId = tripId; }

    public String getVendorName() { return vendorName; }
    public void setVendorName(String vendorName) { this.vendorName = vendorName; }

    public String getMonth() { return month; }
    public void setMonth(String month) { this.month = month; }

    public String getBusinessUnit() { return businessUnit; }
    public void setBusinessUnit(String businessUnit) { this.businessUnit = businessUnit; }

    public double getBilledKm() { return billedKm; }
    public void setBilledKm(double billedKm) { this.billedKm = billedKm; }

    public double getGpsActualKm() { return gpsActualKm; }
    public void setGpsActualKm(double gpsActualKm) { this.gpsActualKm = gpsActualKm; }

    public double getBilledCost() { return billedCost; }
    public void setBilledCost(double billedCost) { this.billedCost = billedCost; }

    public double getAuditExpectedCost() { return auditExpectedCost; }
    public void setAuditExpectedCost(double auditExpectedCost) { this.auditExpectedCost = auditExpectedCost; }

    public String getDiscrepancyReason() { return discrepancyReason; }
    public void setDiscrepancyReason(String discrepancyReason) { this.discrepancyReason = discrepancyReason; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
