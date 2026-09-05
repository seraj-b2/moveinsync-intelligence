package com.moveinsync.intelligence.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SlaShieldRequest {
    @JsonProperty("vendorName")
    private String vendorName;

    @JsonProperty("selectedMonth")
    private String selectedMonth;

    @JsonProperty("businessUnit")
    private String businessUnit;

    public SlaShieldRequest() {}

    public SlaShieldRequest(String vendorName, String selectedMonth, String businessUnit) {
        this.vendorName = vendorName;
        this.selectedMonth = selectedMonth;
        this.businessUnit = businessUnit;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public String getSelectedMonth() {
        return selectedMonth;
    }

    public void setSelectedMonth(String selectedMonth) {
        this.selectedMonth = selectedMonth;
    }

    public String getBusinessUnit() {
        return businessUnit;
    }

    public void setBusinessUnit(String businessUnit) {
        this.businessUnit = businessUnit;
    }

    public String vendorName() { return vendorName; }
    public String selectedMonth() { return selectedMonth; }
    public String businessUnit() { return businessUnit; }
}

