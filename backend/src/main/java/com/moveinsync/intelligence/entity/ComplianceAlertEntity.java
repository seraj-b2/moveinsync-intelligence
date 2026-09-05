package com.moveinsync.intelligence.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "compliance_alerts")
public class ComplianceAlertEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "alert_id")
    private String alertId;

    @Column(name = "vendor_name")
    private String vendorName;

    @Column(name = "month")
    private String month;

    @Column(name = "business_unit")
    private String businessUnit;

    @Column(name = "vehicle_reg")
    private String vehicleReg;

    @Column(name = "driver_name")
    private String driverName;

    @Column(name = "alert_type")
    private String alertType;

    @Column(name = "severity")
    private String severity;

    @Column(name = "timestamp")
    private String timestamp;

    public ComplianceAlertEntity() {}

    public ComplianceAlertEntity(String alertId, String vendorName, String month, String businessUnit,
                                 String vehicleReg, String driverName, String alertType,
                                 String severity, String timestamp) {
        this.alertId = alertId;
        this.vendorName = vendorName;
        this.month = month;
        this.businessUnit = businessUnit;
        this.vehicleReg = vehicleReg;
        this.driverName = driverName;
        this.alertType = alertType;
        this.severity = severity;
        this.timestamp = timestamp;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getAlertId() { return alertId; }
    public void setAlertId(String alertId) { this.alertId = alertId; }

    public String getVendorName() { return vendorName; }
    public void setVendorName(String vendorName) { this.vendorName = vendorName; }

    public String getMonth() { return month; }
    public void setMonth(String month) { this.month = month; }

    public String getBusinessUnit() { return businessUnit; }
    public void setBusinessUnit(String businessUnit) { this.businessUnit = businessUnit; }

    public String getVehicleReg() { return vehicleReg; }
    public void setVehicleReg(String vehicleReg) { this.vehicleReg = vehicleReg; }

    public String getDriverName() { return driverName; }
    public void setDriverName(String driverName) { this.driverName = driverName; }

    public String getAlertType() { return alertType; }
    public void setAlertType(String alertType) { this.alertType = alertType; }

    public String getSeverity() { return severity; }
    public void setSeverity(String severity) { this.severity = severity; }

    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
}
