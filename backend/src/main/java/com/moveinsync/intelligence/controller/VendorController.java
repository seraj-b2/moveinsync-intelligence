package com.moveinsync.intelligence.controller;

import com.moveinsync.intelligence.dto.ClaimEvaluationRequest;
import com.moveinsync.intelligence.dto.ClaimEvaluationResponse;
import com.moveinsync.intelligence.dto.SlaShieldRequest;
import com.moveinsync.intelligence.dto.SlaShieldResponse;
import com.moveinsync.intelligence.dto.VendorScorecardResponse;
import com.moveinsync.intelligence.service.VendorService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vendors")
@CrossOrigin(origins = "http://localhost:4200")
public class VendorController {

    private final VendorService vendorService;

    public VendorController(VendorService vendorService) {
        this.vendorService = vendorService;
    }

    @GetMapping("/scorecards")
    public List<VendorScorecardResponse> getVendorScorecards(
            @RequestParam(required = false) String month,
            @RequestParam(required = false) String businessUnit
    ) {
        return vendorService.getVendorScorecards(month, businessUnit);
    }

    @GetMapping("/business-units")
    public List<String> getBusinessUnits() {
        return vendorService.getBusinessUnits();
    }

    @GetMapping("/months")
    public List<String> getAvailableMonths() {
        return vendorService.getAvailableMonths();
    }

    @PostMapping("/evaluate-claim")
    public ClaimEvaluationResponse evaluateClaim(@RequestBody ClaimEvaluationRequest request) {
        return vendorService.evaluateClaim(request);
    }

    @PostMapping("/sla-shield/analyze")
    public SlaShieldResponse analyzeSlaShield(@RequestBody SlaShieldRequest request) {
        return vendorService.analyzeSlaShield(request);
    }

    @GetMapping("/disputes")
    public List<com.moveinsync.intelligence.dto.VendorDisputeItem> getVendorDisputes(
            @RequestParam(required = false) String month,
            @RequestParam(required = false) String businessUnit,
            @RequestParam(required = false) String vendorName,
            @RequestParam(required = false) String status
    ) {
        return vendorService.getVendorDisputes(month, businessUnit, vendorName, status);
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/disputes")
    public com.moveinsync.intelligence.dto.VendorDisputeItem submitVendorDispute(
            @RequestBody com.moveinsync.intelligence.dto.VendorDisputeItem dispute
    ) {
        return vendorService.submitDispute(dispute);
    }

    @CrossOrigin(origins = "*")
    @RequestMapping(value = "/disputes/{disputeId}/status", method = {RequestMethod.PUT, RequestMethod.POST})
    public com.moveinsync.intelligence.dto.VendorDisputeItem updateDisputeStatus(
            @PathVariable String disputeId,
            @RequestParam String status
    ) {
        return vendorService.updateDisputeStatus(disputeId, status);
    }
}
