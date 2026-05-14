package com.trade.tradelicense.presentation.controllers;

import com.trade.tradelicense.application.commands.CancelTradeLicenseApplicationCommand;
import com.trade.tradelicense.application.commands.RequestNewTradeLicenseApplicationCommand;
import com.trade.tradelicense.application.commands.AttachTradeLicenseApplicationDocumentCommand;
import com.trade.tradelicense.application.commands.ResubmitTradeLicenseApplicationCommand;
import com.trade.tradelicense.application.commands.SettleTradeLicenseApplicationPaymentCommand;
import com.trade.tradelicense.application.commands.SubmitTradeLicenseApplicationCommand;
import com.trade.tradelicense.application.commands.handlers.AttachTradeLicenseApplicationDocumentHandler;
import com.trade.tradelicense.application.commands.handlers.CancelTradeLicenseApplicationHandler;
import com.trade.tradelicense.application.commands.handlers.ResubmitTradeLicenseApplicationHandler;
import com.trade.tradelicense.application.commands.handlers.RequestNewTradeLicenseApplicationHandler;
import com.trade.tradelicense.application.commands.handlers.SettleTradeLicenseApplicationPaymentHandler;
import com.trade.tradelicense.application.commands.handlers.SubmitTradeLicenseApplicationHandler;
import com.trade.tradelicense.application.common.TradeLicenseApplicationRepositoryPort;
import com.trade.tradelicense.application.queries.GetTradeLicenseApplicationByIdQuery;
import com.trade.tradelicense.application.queries.handlers.GetTradeLicenseApplicationByIdHandler;
import com.trade.tradelicense.domain.enums.ApplicationStatus;
import com.trade.tradelicense.infrastructure.security.AuthenticatedUser;
import com.trade.tradelicense.infrastructure.services.ApplicationAuditService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.trade.tradelicense.presentation.dto.AttachTradeLicenseApplicationDocumentRequest;
import com.trade.tradelicense.presentation.dto.AuditEventResponse;
import com.trade.tradelicense.presentation.dto.CancelTradeLicenseApplicationRequest;
import com.trade.tradelicense.presentation.dto.RequestNewTradeLicenseApplicationRequest;
import com.trade.tradelicense.presentation.dto.SubmitTradeLicenseApplicationRequest;
import com.trade.tradelicense.presentation.dto.TradeLicenseApplicationResponse;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/trade-license-applications")
public class TradeLicenseApplicationController {
    private final RequestNewTradeLicenseApplicationHandler requestHandler;
    private final AttachTradeLicenseApplicationDocumentHandler attachDocumentHandler;
    private final SettleTradeLicenseApplicationPaymentHandler settlePaymentHandler;
    private final SubmitTradeLicenseApplicationHandler submitHandler;
    private final ResubmitTradeLicenseApplicationHandler resubmitHandler;
    private final CancelTradeLicenseApplicationHandler cancelHandler;
    private final GetTradeLicenseApplicationByIdHandler getByIdHandler;
    private final TradeLicenseApplicationRepositoryPort applicationRepository;
    private final ApplicationAuditService auditService;

    public TradeLicenseApplicationController(
            RequestNewTradeLicenseApplicationHandler requestHandler,
            AttachTradeLicenseApplicationDocumentHandler attachDocumentHandler,
            SettleTradeLicenseApplicationPaymentHandler settlePaymentHandler,
            SubmitTradeLicenseApplicationHandler submitHandler,
            ResubmitTradeLicenseApplicationHandler resubmitHandler,
            CancelTradeLicenseApplicationHandler cancelHandler,
            GetTradeLicenseApplicationByIdHandler getByIdHandler,
            TradeLicenseApplicationRepositoryPort applicationRepository,
            ApplicationAuditService auditService
    ) {
        this.requestHandler = requestHandler;
        this.attachDocumentHandler = attachDocumentHandler;
        this.settlePaymentHandler = settlePaymentHandler;
        this.submitHandler = submitHandler;
        this.resubmitHandler = resubmitHandler;
        this.cancelHandler = cancelHandler;
        this.getByIdHandler = getByIdHandler;
        this.applicationRepository = applicationRepository;
        this.auditService = auditService;
    }

    @GetMapping
    public ResponseEntity<List<TradeLicenseApplicationResponse>> listApplications(
            @RequestParam(required = false) ApplicationStatus status
    ) {
        var applications = status == null ? applicationRepository.findAll() : applicationRepository.findByStatus(status);
        return ResponseEntity.ok(applications.stream()
                .map(TradeLicenseApplicationResponse::fromDomain)
                .toList());
    }

    @PostMapping
    public ResponseEntity<TradeLicenseApplicationResponse> requestNewApplication(
            @RequestBody RequestNewTradeLicenseApplicationRequest request,
            @AuthenticationPrincipal AuthenticatedUser user
    ) {
        var result = requestHandler.handle(new RequestNewTradeLicenseApplicationCommand(
                request.applicantId(),
                request.fullName(),
                request.tradeName(),
                request.nationalIdNumber(),
                request.email(),
                request.phoneNumber(),
                request.tradeLicenseType(),
                request.commodity(),
                request.bankAccountNumber()
        ));
        var application = result.data();
        auditService.record(
                application.id().value(),
                "APPLICATION_CREATED",
                application.status(),
                user == null ? request.applicantId() : user.id(),
                user == null ? null : user.role(),
                "Draft application created"
        );
        return ResponseEntity.ok(TradeLicenseApplicationResponse.fromDomain(application));
    }

    @PostMapping("/{applicationId}/documents")
    public ResponseEntity<TradeLicenseApplicationResponse> attachDocument(
            @PathVariable UUID applicationId,
            @RequestBody AttachTradeLicenseApplicationDocumentRequest request
    ) {
        var result = attachDocumentHandler.handle(new AttachTradeLicenseApplicationDocumentCommand(
                applicationId,
                request.documentType(),
                request.documentReference()
        ));
        auditService.record(applicationId, "DOCUMENT_ATTACHED", result.data().status(), null, null, request.documentType());
        return ResponseEntity.ok(TradeLicenseApplicationResponse.fromDomain(result.data()));
    }

    @PostMapping("/{applicationId}/payment/settle")
    public ResponseEntity<TradeLicenseApplicationResponse> settlePayment(@PathVariable UUID applicationId) {
        var result = settlePaymentHandler.handle(new SettleTradeLicenseApplicationPaymentCommand(applicationId));
        auditService.record(applicationId, "PAYMENT_SETTLED", result.data().status(), null, null, "Payment settled");
        return ResponseEntity.ok(TradeLicenseApplicationResponse.fromDomain(result.data()));
    }

    @PostMapping("/{applicationId}/submit")
    public ResponseEntity<TradeLicenseApplicationResponse> submitApplication(
            @PathVariable UUID applicationId,
            @RequestBody SubmitTradeLicenseApplicationRequest request
    ) {
        var result = submitHandler.handle(new SubmitTradeLicenseApplicationCommand(
                applicationId,
                request.actorId(),
                request.role()
        ));
        auditService.record(applicationId, "APPLICATION_SUBMITTED", result.data().status(), request.actorId(), request.role(), "Application submitted");
        return ResponseEntity.ok(TradeLicenseApplicationResponse.fromDomain(result.data()));
    }

    @PostMapping("/{applicationId}/resubmit")
    public ResponseEntity<TradeLicenseApplicationResponse> resubmitApplication(
            @PathVariable UUID applicationId,
            @RequestBody SubmitTradeLicenseApplicationRequest request
    ) {
        var result = resubmitHandler.handle(new ResubmitTradeLicenseApplicationCommand(
                applicationId,
                request.actorId(),
                request.role()
        ));
        auditService.record(applicationId, "APPLICATION_RESUBMITTED_AFTER_ADJUSTMENT", result.data().status(), request.actorId(), request.role(), "Application resubmitted after adjustment");
        return ResponseEntity.ok(TradeLicenseApplicationResponse.fromDomain(result.data()));
    }

    @PostMapping("/{applicationId}/cancel")
    public ResponseEntity<TradeLicenseApplicationResponse> cancelApplication(
            @PathVariable UUID applicationId,
            @RequestBody CancelTradeLicenseApplicationRequest request
    ) {
        var result = cancelHandler.handle(new CancelTradeLicenseApplicationCommand(
                applicationId,
                request.actorId(),
                request.role()
        ));
        auditService.record(applicationId, "APPLICATION_CANCELLED", result.data().status(), request.actorId(), request.role(), "Application cancelled");
        return ResponseEntity.ok(TradeLicenseApplicationResponse.fromDomain(result.data()));
    }

    @GetMapping("/{applicationId}")
    public ResponseEntity<TradeLicenseApplicationResponse> getApplication(@PathVariable UUID applicationId) {
        var application = getByIdHandler.handle(new GetTradeLicenseApplicationByIdQuery(applicationId));
        return ResponseEntity.ok(TradeLicenseApplicationResponse.fromDomain(application));
    }

    @GetMapping("/{applicationId}/timeline")
    public ResponseEntity<List<AuditEventResponse>> timeline(@PathVariable UUID applicationId) {
        return ResponseEntity.ok(auditService.timeline(applicationId).stream()
                .map(AuditEventResponse::fromEntity)
                .toList());
    }
}
