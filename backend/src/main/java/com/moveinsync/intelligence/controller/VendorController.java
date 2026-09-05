package com.moveinsync.intelligence.controller;

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
}
