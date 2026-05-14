package com.trade.tradelicense.presentation.controllers;

import com.trade.tradelicense.application.commands.CancelTradeLicenseCommand;
import com.trade.tradelicense.application.commands.RenewTradeLicenseCommand;
import com.trade.tradelicense.application.commands.UpdateTradeLicenseCommand;
import com.trade.tradelicense.application.commands.handlers.CancelTradeLicenseHandler;
import com.trade.tradelicense.application.commands.handlers.RenewTradeLicenseHandler;
import com.trade.tradelicense.application.commands.handlers.UpdateTradeLicenseHandler;
import com.trade.tradelicense.application.queries.GetTradeLicenseByIdQuery;
import com.trade.tradelicense.application.queries.GetTradeLicenseByApplicationIdQuery;
import com.trade.tradelicense.application.queries.GetTradeLicensesForCustomerQuery;
import com.trade.tradelicense.application.queries.handlers.GetTradeLicenseByApplicationIdHandler;
import com.trade.tradelicense.application.queries.handlers.GetTradeLicenseByIdHandler;
import com.trade.tradelicense.application.queries.handlers.GetTradeLicensesForCustomerHandler;
import com.trade.tradelicense.domain.aggregates.TradeLicense;
import com.trade.tradelicense.domain.enums.UserRole;
import com.trade.tradelicense.infrastructure.security.AuthenticatedUser;
import com.trade.tradelicense.presentation.dto.TradeLicenseResponse;
import com.trade.tradelicense.presentation.dto.UpdateTradeLicenseRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/trade-licenses")
public class TradeLicenseController {
    private final GetTradeLicensesForCustomerHandler getTradeLicensesForCustomerHandler;
    private final GetTradeLicenseByIdHandler getTradeLicenseByIdHandler;
    private final GetTradeLicenseByApplicationIdHandler getTradeLicenseByApplicationIdHandler;
    private final UpdateTradeLicenseHandler updateTradeLicenseHandler;
    private final CancelTradeLicenseHandler cancelTradeLicenseHandler;
    private final RenewTradeLicenseHandler renewTradeLicenseHandler;

    public TradeLicenseController(
            GetTradeLicensesForCustomerHandler getTradeLicensesForCustomerHandler,
            GetTradeLicenseByIdHandler getTradeLicenseByIdHandler,
            GetTradeLicenseByApplicationIdHandler getTradeLicenseByApplicationIdHandler,
            UpdateTradeLicenseHandler updateTradeLicenseHandler,
            CancelTradeLicenseHandler cancelTradeLicenseHandler,
            RenewTradeLicenseHandler renewTradeLicenseHandler
    ) {
        this.getTradeLicensesForCustomerHandler = getTradeLicensesForCustomerHandler;
        this.getTradeLicenseByIdHandler = getTradeLicenseByIdHandler;
        this.getTradeLicenseByApplicationIdHandler = getTradeLicenseByApplicationIdHandler;
        this.updateTradeLicenseHandler = updateTradeLicenseHandler;
        this.cancelTradeLicenseHandler = cancelTradeLicenseHandler;
        this.renewTradeLicenseHandler = renewTradeLicenseHandler;
    }

    @GetMapping("/mine")
    public ResponseEntity<List<TradeLicenseResponse>> myTradeLicenses(@AuthenticationPrincipal AuthenticatedUser user) {
        var licenses = getTradeLicensesForCustomerHandler.handle(new GetTradeLicensesForCustomerQuery(user.id()));
        return ResponseEntity.ok(licenses.stream()
                .map(TradeLicenseResponse::fromDomain)
                .toList());
    }

    @GetMapping("/verify/{licenseId}")
    public ResponseEntity<TradeLicenseResponse> verify(@PathVariable UUID licenseId) {
        var tradeLicense = getTradeLicenseByIdHandler.handle(new GetTradeLicenseByIdQuery(licenseId));
        return ResponseEntity.ok(TradeLicenseResponse.fromDomain(tradeLicense));
    }

    @GetMapping("/application/{applicationId}")
    public ResponseEntity<TradeLicenseResponse> getByApplicationId(
            @PathVariable UUID applicationId,
            @AuthenticationPrincipal AuthenticatedUser user
    ) {
        var tradeLicense = getTradeLicenseByApplicationIdHandler.handle(new GetTradeLicenseByApplicationIdQuery(applicationId));
        assertCanView(tradeLicense, user);
        return ResponseEntity.ok(TradeLicenseResponse.fromDomain(tradeLicense));
    }

    @PatchMapping("/{licenseId}")
    public ResponseEntity<TradeLicenseResponse> updateTradeLicense(
            @PathVariable UUID licenseId,
            @RequestBody UpdateTradeLicenseRequest request,
            @AuthenticationPrincipal AuthenticatedUser user
    ) {
        var result = updateTradeLicenseHandler.handle(new UpdateTradeLicenseCommand(
                licenseId,
                user.id(),
                user.role(),
                request.tradeLicenseType(),
                request.commodity()
        ));
        return ResponseEntity.ok(TradeLicenseResponse.fromDomain(result.data()));
    }

    @PatchMapping("/{licenseId}/cancel")
    public ResponseEntity<TradeLicenseResponse> cancelTradeLicense(
            @PathVariable UUID licenseId,
            @AuthenticationPrincipal AuthenticatedUser user
    ) {
        var result = cancelTradeLicenseHandler.handle(new CancelTradeLicenseCommand(licenseId, user.id(), user.role()));
        return ResponseEntity.ok(TradeLicenseResponse.fromDomain(result.data()));
    }

    @PatchMapping("/{licenseId}/renew")
    public ResponseEntity<TradeLicenseResponse> renewTradeLicense(
            @PathVariable UUID licenseId,
            @AuthenticationPrincipal AuthenticatedUser user
    ) {
        var result = renewTradeLicenseHandler.handle(new RenewTradeLicenseCommand(licenseId, user.id(), user.role()));
        return ResponseEntity.ok(TradeLicenseResponse.fromDomain(result.data()));
    }

    private void assertCanView(TradeLicense tradeLicense, AuthenticatedUser user) {
        if (user.role() == UserRole.ADMIN) {
            return;
        }
        if (!tradeLicense.licenseHolder().getUserId().value().equals(user.id())) {
            throw new AccessDeniedException("You can only view your own trade license");
        }
    }
}
