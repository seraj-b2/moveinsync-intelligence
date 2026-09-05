package com.moveinsync.intelligence.controller;

import com.moveinsync.intelligence.dto.ManagerDashboardResponse;
import com.moveinsync.intelligence.dto.ManagerDashboardResponse.*;
import com.moveinsync.intelligence.service.ManagerService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/managers")
@CrossOrigin(origins = "http://localhost:4200")
public class ManagerController {

    private final ManagerService managerService;

    public ManagerController(ManagerService managerService) {
        this.managerService = managerService;
    }

    @GetMapping("/profiles")
    public List<ManagerProfile> getManagerProfiles(@RequestParam(required = false) String companyName) {
        return managerService.getAllManagers(companyName);
    }

    @GetMapping("/{companyName}/{managerId}")
    public ManagerDashboardResponse getManagerDashboard(
            @PathVariable String companyName,
            @PathVariable String managerId,
            @RequestParam(required = false) String month
    ) {
        return managerService.getManagerDashboard(companyName, managerId, month);
    }

    @PostMapping("/{companyName}/{managerId}/delay-notifications/acknowledge")
    public Map<String, Object> acknowledgeDelayNotification(
            @PathVariable String companyName,
            @PathVariable String managerId,
            @RequestBody AcknowledgeDelayRequest request
    ) {
        boolean success = managerService.acknowledgeNotification(managerId, request.notificationId());
        return Map.of("success", success, "notificationId", request.notificationId());
    }

    @PostMapping("/{companyName}/{managerId}/delay-notifications/simulate")
    public EmployeeDelayNotification simulateDelayNotification(
            @PathVariable String companyName,
            @PathVariable String managerId,
            @RequestBody SimulateDelayRequest request
    ) {
        return managerService.simulateDelay(managerId, request);
    }
}
