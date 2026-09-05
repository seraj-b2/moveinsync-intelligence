package com.moveinsync.intelligence.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DeduplicationReportResponse {

    @JsonProperty("totalRowsParsed")
    private int totalRowsParsed;

    @JsonProperty("newRecordsSaved")
    private int newRecordsSaved;

    @JsonProperty("duplicatesMerged")
    private int duplicatesMerged;

    @JsonProperty("dedupeEfficiencyRate")
    private double dedupeEfficiencyRate;

    @JsonProperty("datasetType")
    private String datasetType;

    @JsonProperty("status")
    private String status;

    @JsonProperty("message")
    private String message;

    public DeduplicationReportResponse() {}

    public DeduplicationReportResponse(int totalRowsParsed, int newRecordsSaved, int duplicatesMerged,
                                         double dedupeEfficiencyRate, String datasetType,
                                         String status, String message) {
        this.totalRowsParsed = totalRowsParsed;
        this.newRecordsSaved = newRecordsSaved;
        this.duplicatesMerged = duplicatesMerged;
        this.dedupeEfficiencyRate = dedupeEfficiencyRate;
        this.datasetType = datasetType;
        this.status = status;
        this.message = message;
    }

    public int getTotalRowsParsed() { return totalRowsParsed; }
    public void setTotalRowsParsed(int totalRowsParsed) { this.totalRowsParsed = totalRowsParsed; }

    public int getNewRecordsSaved() { return newRecordsSaved; }
    public void setNewRecordsSaved(int newRecordsSaved) { this.newRecordsSaved = newRecordsSaved; }

    public int getDuplicatesMerged() { return duplicatesMerged; }
    public void setDuplicatesMerged(int duplicatesMerged) { this.duplicatesMerged = duplicatesMerged; }

    public double getDedupeEfficiencyRate() { return dedupeEfficiencyRate; }
    public void setDedupeEfficiencyRate(double dedupeEfficiencyRate) { this.dedupeEfficiencyRate = dedupeEfficiencyRate; }

    public String getDatasetType() { return datasetType; }
    public void setDatasetType(String datasetType) { this.datasetType = datasetType; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
