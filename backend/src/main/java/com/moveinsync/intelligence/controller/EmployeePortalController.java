package com.moveinsync.intelligence.controller;

import com.moveinsync.intelligence.dto.EmployeePortalResponse;
import com.moveinsync.intelligence.dto.EmployeePortalResponse.ActionResponse;
import com.moveinsync.intelligence.dto.EmployeePortalResponse.FeedbackSubmitRequest;
import com.moveinsync.intelligence.dto.EmployeePortalResponse.SosTriggerRequest;
import com.moveinsync.intelligence.service.EmployeePortalService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/employees")
@CrossOrigin(origins = "*")
public class EmployeePortalController {

    private final EmployeePortalService employeePortalService;

    public EmployeePortalController(EmployeePortalService employeePortalService) {
        this.employeePortalService = employeePortalService;
    }

    @GetMapping("/{companyName}/{stwid}")
    public ResponseEntity<EmployeePortalResponse> getEmployeeDashboardWithCompany(
            @PathVariable String companyName,
            @PathVariable String stwid,
            @RequestParam(required = false) String month
    ) {
        EmployeePortalResponse response = employeePortalService.getEmployeePortalData(companyName, stwid, month);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{stwid}")
    public ResponseEntity<EmployeePortalResponse> getEmployeeDashboard(
            @PathVariable String stwid,
            @RequestParam(required = false) String month
    ) {
        EmployeePortalResponse response = employeePortalService.getEmployeePortalData("catalyst-Sac", stwid, month);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/feedback")
    public ResponseEntity<ActionResponse> submitFeedback(@RequestBody FeedbackSubmitRequest request) {
        ActionResponse response = employeePortalService.submitFeedback(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/sos")
    public ResponseEntity<ActionResponse> triggerSos(@RequestBody SosTriggerRequest request) {
        ActionResponse response = employeePortalService.triggerSos(request);
        return ResponseEntity.ok(response);
    }
}
