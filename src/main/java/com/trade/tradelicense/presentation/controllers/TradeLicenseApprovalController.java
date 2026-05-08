package com.trade.tradelicense.presentation.controllers;

import com.trade.tradelicense.application.commands.ApproveTradeLicenseApplicationCommand;
import com.trade.tradelicense.application.commands.handlers.ApproveTradeLicenseApplicationHandler;
import com.trade.tradelicense.application.queries.GetPendingApprovalApplicationsQuery;
import com.trade.tradelicense.application.queries.handlers.GetPendingApprovalApplicationsHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.trade.tradelicense.presentation.dto.ApproveTradeLicenseApplicationRequest;
import com.trade.tradelicense.presentation.dto.TradeLicenseApplicationResponse;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/trade-license-approvals")
public class TradeLicenseApprovalController {
    private final ApproveTradeLicenseApplicationHandler approveHandler;
    private final GetPendingApprovalApplicationsHandler getPendingApprovalHandler;

    public TradeLicenseApprovalController(
            ApproveTradeLicenseApplicationHandler approveHandler,
            GetPendingApprovalApplicationsHandler getPendingApprovalHandler
    ) {
        this.approveHandler = approveHandler;
        this.getPendingApprovalHandler = getPendingApprovalHandler;
    }

    @GetMapping("/pending")
    public ResponseEntity<List<TradeLicenseApplicationResponse>> getPendingApprovals() {
        var applications = getPendingApprovalHandler.handle(new GetPendingApprovalApplicationsQuery());
        return ResponseEntity.ok(applications.stream()
                .map(TradeLicenseApplicationResponse::fromDomain)
                .toList());
    }

    @PostMapping("/{applicationId}")
    public ResponseEntity<TradeLicenseApplicationResponse> approveApplication(
            @PathVariable UUID applicationId,
            @RequestBody ApproveTradeLicenseApplicationRequest request
    ) {
        var result = approveHandler.handle(new ApproveTradeLicenseApplicationCommand(
                applicationId,
                request.approverId(),
                request.role(),
                request.decision(),
                request.comment(),
                request.licenseNumber(),
                request.tinNumber(),
                request.licenseTypeToIssue()
        ));
        return ResponseEntity.ok(TradeLicenseApplicationResponse.fromDomain(result.data()));
    }
}
