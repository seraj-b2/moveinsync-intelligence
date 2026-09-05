package com.moveinsync.intelligence.controller;

import com.moveinsync.intelligence.dto.ManagerDashboardResponse;
import com.moveinsync.intelligence.dto.ManagerDashboardResponse.ManagerProfile;
import com.moveinsync.intelligence.service.ManagerService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
}
