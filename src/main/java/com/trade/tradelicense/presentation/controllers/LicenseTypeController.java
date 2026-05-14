package com.trade.tradelicense.presentation.controllers;

import com.trade.tradelicense.infrastructure.services.LicenseTypeConfigService;
import com.trade.tradelicense.presentation.dto.LicenseTypeConfigResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/license-types")
public class LicenseTypeController {
    private final LicenseTypeConfigService licenseTypeConfigService;

    public LicenseTypeController(LicenseTypeConfigService licenseTypeConfigService) {
        this.licenseTypeConfigService = licenseTypeConfigService;
    }

    @GetMapping
    public ResponseEntity<List<LicenseTypeConfigResponse>> licenseTypes() {
        return ResponseEntity.ok(licenseTypeConfigService.list(true).stream()
                .map(LicenseTypeConfigResponse::fromEntity)
                .toList());
    }
}
