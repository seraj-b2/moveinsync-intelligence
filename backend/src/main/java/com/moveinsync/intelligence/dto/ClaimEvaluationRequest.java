package com.moveinsync.intelligence.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ClaimEvaluationRequest {
    @JsonProperty("claimText")
    private String claimText;

    @JsonProperty("selectedMonth")
    private String selectedMonth;

    public ClaimEvaluationRequest() {}

    public ClaimEvaluationRequest(String claimText, String selectedMonth) {
        this.claimText = claimText;
        this.selectedMonth = selectedMonth;
    }

    public String getClaimText() {
        return claimText;
    }

    public void setClaimText(String claimText) {
        this.claimText = claimText;
    }

    public String getSelectedMonth() {
        return selectedMonth;
    }

    public void setSelectedMonth(String selectedMonth) {
        this.selectedMonth = selectedMonth;
    }

    public String claimText() { return claimText; }
    public String selectedMonth() { return selectedMonth; }
}

