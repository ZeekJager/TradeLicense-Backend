package com.trade.tradelicense.presentation.controllers;

import com.trade.tradelicense.application.commands.CancelTradeLicenseApplicationCommand;
import com.trade.tradelicense.application.commands.RequestNewTradeLicenseApplicationCommand;
import com.trade.tradelicense.application.commands.AttachTradeLicenseApplicationDocumentCommand;
import com.trade.tradelicense.application.commands.SettleTradeLicenseApplicationPaymentCommand;
import com.trade.tradelicense.application.commands.SubmitTradeLicenseApplicationCommand;
import com.trade.tradelicense.application.commands.handlers.AttachTradeLicenseApplicationDocumentHandler;
import com.trade.tradelicense.application.commands.handlers.CancelTradeLicenseApplicationHandler;
import com.trade.tradelicense.application.commands.handlers.RequestNewTradeLicenseApplicationHandler;
import com.trade.tradelicense.application.commands.handlers.SettleTradeLicenseApplicationPaymentHandler;
import com.trade.tradelicense.application.commands.handlers.SubmitTradeLicenseApplicationHandler;
import com.trade.tradelicense.application.queries.GetTradeLicenseApplicationByIdQuery;
import com.trade.tradelicense.application.queries.handlers.GetTradeLicenseApplicationByIdHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.trade.tradelicense.presentation.dto.AttachTradeLicenseApplicationDocumentRequest;
import com.trade.tradelicense.presentation.dto.CancelTradeLicenseApplicationRequest;
import com.trade.tradelicense.presentation.dto.RequestNewTradeLicenseApplicationRequest;
import com.trade.tradelicense.presentation.dto.SubmitTradeLicenseApplicationRequest;
import com.trade.tradelicense.presentation.dto.TradeLicenseApplicationResponse;

import java.util.UUID;

@RestController
@RequestMapping("/api/trade-license-applications")
public class TradeLicenseApplicationController {
    private final RequestNewTradeLicenseApplicationHandler requestHandler;
    private final AttachTradeLicenseApplicationDocumentHandler attachDocumentHandler;
    private final SettleTradeLicenseApplicationPaymentHandler settlePaymentHandler;
    private final SubmitTradeLicenseApplicationHandler submitHandler;
    private final CancelTradeLicenseApplicationHandler cancelHandler;
    private final GetTradeLicenseApplicationByIdHandler getByIdHandler;

    public TradeLicenseApplicationController(
            RequestNewTradeLicenseApplicationHandler requestHandler,
            AttachTradeLicenseApplicationDocumentHandler attachDocumentHandler,
            SettleTradeLicenseApplicationPaymentHandler settlePaymentHandler,
            SubmitTradeLicenseApplicationHandler submitHandler,
            CancelTradeLicenseApplicationHandler cancelHandler,
            GetTradeLicenseApplicationByIdHandler getByIdHandler
    ) {
        this.requestHandler = requestHandler;
        this.attachDocumentHandler = attachDocumentHandler;
        this.settlePaymentHandler = settlePaymentHandler;
        this.submitHandler = submitHandler;
        this.cancelHandler = cancelHandler;
        this.getByIdHandler = getByIdHandler;
    }

    @PostMapping
    public ResponseEntity<TradeLicenseApplicationResponse> requestNewApplication(
            @RequestBody RequestNewTradeLicenseApplicationRequest request
    ) {
        var result = requestHandler.handle(new RequestNewTradeLicenseApplicationCommand(
                request.applicantId(),
                request.fullName(),
                request.nationalIdNumber(),
                request.email(),
                request.phoneNumber(),
                request.tradeLicenseType(),
                request.commodity()
        ));
        return ResponseEntity.ok(TradeLicenseApplicationResponse.fromDomain(result.data()));
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
        return ResponseEntity.ok(TradeLicenseApplicationResponse.fromDomain(result.data()));
    }

    @PostMapping("/{applicationId}/payment/settle")
    public ResponseEntity<TradeLicenseApplicationResponse> settlePayment(@PathVariable UUID applicationId) {
        var result = settlePaymentHandler.handle(new SettleTradeLicenseApplicationPaymentCommand(applicationId));
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
        return ResponseEntity.ok(TradeLicenseApplicationResponse.fromDomain(result.data()));
    }

    @GetMapping("/{applicationId}")
    public ResponseEntity<TradeLicenseApplicationResponse> getApplication(@PathVariable UUID applicationId) {
        var application = getByIdHandler.handle(new GetTradeLicenseApplicationByIdQuery(applicationId));
        return ResponseEntity.ok(TradeLicenseApplicationResponse.fromDomain(application));
    }
}
