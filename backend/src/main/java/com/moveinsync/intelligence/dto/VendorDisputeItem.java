package com.moveinsync.intelligence.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class VendorDisputeItem {
    @JsonProperty("disputeId")
    private String disputeId;

    @JsonProperty("vendorName")
    private String vendorName;

    @JsonProperty("route")
    private String route;

    @JsonProperty("claimSubject")
    private String claimSubject;

    @JsonProperty("claimText")
    private String claimText;

    @JsonProperty("month")
    private String month;

    @JsonProperty("businessUnit")
    private String businessUnit;

    @JsonProperty("affectedCabs")
    private int affectedCabs;

    @JsonProperty("status")
    private String status; // PENDING_REVIEW, APPROVED, REJECTED

    @JsonProperty("submittedAt")
    private String submittedAt;

    public VendorDisputeItem() {}

    public VendorDisputeItem(String disputeId, String vendorName, String route, String claimSubject, 
                             String claimText, String month, String businessUnit, int affectedCabs, 
                             String status, String submittedAt) {
        this.disputeId = disputeId;
        this.vendorName = vendorName;
        this.route = route;
        this.claimSubject = claimSubject;
        this.claimText = claimText;
        this.month = month;
        this.businessUnit = businessUnit;
        this.affectedCabs = affectedCabs;
        this.status = status;
        this.submittedAt = submittedAt;
    }

    public String getDisputeId() { return disputeId; }
    public void setDisputeId(String disputeId) { this.disputeId = disputeId; }

    public String getVendorName() { return vendorName; }
    public void setVendorName(String vendorName) { this.vendorName = vendorName; }

    public String getRoute() { return route; }
    public void setRoute(String route) { this.route = route; }

    public String getClaimSubject() { return claimSubject; }
    public void setClaimSubject(String claimSubject) { this.claimSubject = claimSubject; }

    public String getClaimText() { return claimText; }
    public void setClaimText(String claimText) { this.claimText = claimText; }

    public String getMonth() { return month; }
    public void setMonth(String month) { this.month = month; }

    public String getBusinessUnit() { return businessUnit; }
    public void setBusinessUnit(String businessUnit) { this.businessUnit = businessUnit; }

    public int getAffectedCabs() { return affectedCabs; }
    public void setAffectedCabs(int affectedCabs) { this.affectedCabs = affectedCabs; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getSubmittedAt() { return submittedAt; }
    public void setSubmittedAt(String submittedAt) { this.submittedAt = submittedAt; }
}
