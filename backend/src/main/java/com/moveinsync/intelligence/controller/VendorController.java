package com.moveinsync.intelligence.controller;

import com.moveinsync.intelligence.dto.VendorScorecardResponse;
import com.moveinsync.intelligence.service.VendorService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public List<VendorScorecardResponse> getVendorScorecards() {
        return vendorService.getVendorScorecards();
    }
}
