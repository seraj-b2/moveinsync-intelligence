package com.moveinsync.intelligence.controller;

import com.moveinsync.intelligence.dto.DeduplicationReportResponse;
import com.moveinsync.intelligence.service.DataDeduplicationService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.Map;

@RestController
@RequestMapping("/api/ingest")
@CrossOrigin(origins = "*")
public class DataIngestionController {

    private final DataDeduplicationService deduplicationService;

    public DataIngestionController(DataDeduplicationService deduplicationService) {
        this.deduplicationService = deduplicationService;
    }

    @PostMapping("/upload")
    public DeduplicationReportResponse uploadAndDeduplicate(@RequestBody Map<String, String> payload) {
        String content = payload.get("fileContent");
        String datasetType = payload.getOrDefault("datasetType", "BILLING");
        return deduplicationService.processAndDeduplicate(content, datasetType);
    }

    @PostMapping("/upload-file")
    public DeduplicationReportResponse uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "datasetType", defaultValue = "BILLING") String datasetType
    ) {
        try {
            String content = new String(file.getBytes(), StandardCharsets.UTF_8);
            return deduplicationService.processAndDeduplicate(content, datasetType);
        } catch (Exception e) {
            return new DeduplicationReportResponse(0, 0, 0, 0.0, datasetType, "ERROR", "Failed to read file: " + e.getMessage());
        }
    }
}
