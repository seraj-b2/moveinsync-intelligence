package com.moveinsync.intelligence.service;

import com.moveinsync.intelligence.dto.DashboardSummaryResponse;
import org.springframework.stereotype.Service;

@Service
public class DashboardService {

    public DashboardSummaryResponse getSummary() {
        return new DashboardSummaryResponse(
                1248,
                86,
                94.6,
                67,
                4380,
                82.4
        );
    }
}