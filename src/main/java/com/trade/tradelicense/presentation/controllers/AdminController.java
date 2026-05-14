package com.trade.tradelicense.presentation.controllers;

import com.trade.tradelicense.application.common.TradeLicenseApplicationRepositoryPort;
import com.trade.tradelicense.domain.aggregates.TradeLicenseApplication;
import com.trade.tradelicense.domain.enums.ApplicationStatus;
import com.trade.tradelicense.infrastructure.security.AuthService;
import com.trade.tradelicense.infrastructure.services.LicenseTypeConfigService;
import com.trade.tradelicense.presentation.dto.ApplicationStatusSummaryResponse;
import com.trade.tradelicense.presentation.dto.CreateUserRequest;
import com.trade.tradelicense.presentation.dto.CurrentUserResponse;
import com.trade.tradelicense.presentation.dto.LicenseTypeConfigRequest;
import com.trade.tradelicense.presentation.dto.LicenseTypeConfigResponse;
import com.trade.tradelicense.presentation.dto.TradeLicenseApplicationResponse;
import com.trade.tradelicense.presentation.dto.UpdateUserRoleRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private final AuthService authService;
    private final TradeLicenseApplicationRepositoryPort applicationRepository;
    private final LicenseTypeConfigService licenseTypeConfigService;

    public AdminController(
            AuthService authService,
            TradeLicenseApplicationRepositoryPort applicationRepository,
            LicenseTypeConfigService licenseTypeConfigService
    ) {
        this.authService = authService;
        this.applicationRepository = applicationRepository;
        this.licenseTypeConfigService = licenseTypeConfigService;
    }

    @GetMapping("/users")
    public ResponseEntity<List<CurrentUserResponse>> listUsers() {
        return ResponseEntity.ok(authService.listUsers());
    }

    @PostMapping("/users")
    public ResponseEntity<CurrentUserResponse> createUser(@RequestBody CreateUserRequest request) {
        return ResponseEntity.ok(authService.createUser(
                request.fullName(),
                request.email(),
                request.password(),
                request.role()
        ));
    }

    @PutMapping("/users/{userId}/role")
    public ResponseEntity<CurrentUserResponse> updateUserRole(
            @PathVariable UUID userId,
            @RequestBody UpdateUserRoleRequest request
    ) {
        return ResponseEntity.ok(authService.updateRole(userId, request.role()));
    }

    @GetMapping("/applications")
    public ResponseEntity<List<TradeLicenseApplicationResponse>> applications(
            @RequestParam(required = false) ApplicationStatus status
    ) {
        List<TradeLicenseApplication> applications = status == null
                ? applicationRepository.findAll()
                : applicationRepository.findByStatus(status);
        return ResponseEntity.ok(applications.stream()
                .map(TradeLicenseApplicationResponse::fromDomain)
                .toList());
    }

    @GetMapping("/applications/status-summary")
    public ResponseEntity<List<ApplicationStatusSummaryResponse>> statusSummary() {
        return ResponseEntity.ok(Arrays.stream(ApplicationStatus.values())
                .map(status -> new ApplicationStatusSummaryResponse(status, applicationRepository.findByStatus(status).size()))
                .toList());
    }

    @GetMapping("/license-types")
    public ResponseEntity<List<LicenseTypeConfigResponse>> licenseTypes(
            @RequestParam(defaultValue = "false") boolean activeOnly
    ) {
        return ResponseEntity.ok(licenseTypeConfigService.list(activeOnly).stream()
                .map(LicenseTypeConfigResponse::fromEntity)
                .toList());
    }

    @PostMapping("/license-types")
    public ResponseEntity<LicenseTypeConfigResponse> createLicenseType(@RequestBody LicenseTypeConfigRequest request) {
        return ResponseEntity.ok(LicenseTypeConfigResponse.fromEntity(licenseTypeConfigService.create(
                request.code(),
                request.name(),
                request.requiredDocuments()
        )));
    }

    @PutMapping("/license-types/{licenseTypeId}")
    public ResponseEntity<LicenseTypeConfigResponse> updateLicenseType(
            @PathVariable UUID licenseTypeId,
            @RequestBody LicenseTypeConfigRequest request
    ) {
        return ResponseEntity.ok(LicenseTypeConfigResponse.fromEntity(licenseTypeConfigService.update(
                licenseTypeId,
                request.name(),
                request.requiredDocuments(),
                request.active() == null || request.active()
        )));
    }

    @GetMapping("/reports/applications.csv")
    public ResponseEntity<String> applicationsCsv() {
        StringBuilder csv = new StringBuilder("applicationId,applicant,licenseType,commodity,status\n");
        applicationRepository.findAll().forEach(application -> csv.append(application.id().value())
                .append(',')
                .append(escape(application.applicant().getFullName().value()))
                .append(',')
                .append(escape(application.licenseType().code()))
                .append(',')
                .append(escape(application.commodity().code()))
                .append(',')
                .append(application.status())
                .append('\n'));
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("text/csv"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"trade-license-applications.csv\"")
                .body(csv.toString());
    }

    private String escape(String value) {
        if (value == null) {
            return "";
        }
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }
}
