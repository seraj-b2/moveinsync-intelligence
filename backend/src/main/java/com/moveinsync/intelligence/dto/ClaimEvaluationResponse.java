package com.moveinsync.intelligence.dto;

import java.util.List;

public record ClaimEvaluationResponse(
        String vendorName,
        String claimDate,
        String route,
        boolean claimValid,
        double confidence,
        List<String> evidence,
        String recommendation,
        String reasoning
) {}
