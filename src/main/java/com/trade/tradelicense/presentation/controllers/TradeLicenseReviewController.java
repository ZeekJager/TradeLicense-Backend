package com.trade.tradelicense.presentation.controllers;

import com.trade.tradelicense.application.commands.ReviewTradeLicenseApplicationCommand;
import com.trade.tradelicense.application.commands.handlers.ReviewTradeLicenseApplicationHandler;
import com.trade.tradelicense.application.queries.GetPendingReviewApplicationsQuery;
import com.trade.tradelicense.application.queries.handlers.GetPendingReviewApplicationsHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.trade.tradelicense.presentation.dto.ReviewTradeLicenseApplicationRequest;
import com.trade.tradelicense.presentation.dto.TradeLicenseApplicationResponse;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/trade-license-reviews")
public class TradeLicenseReviewController {
    private final ReviewTradeLicenseApplicationHandler reviewHandler;
    private final GetPendingReviewApplicationsHandler getPendingReviewHandler;

    public TradeLicenseReviewController(
            ReviewTradeLicenseApplicationHandler reviewHandler,
            GetPendingReviewApplicationsHandler getPendingReviewHandler
    ) {
        this.reviewHandler = reviewHandler;
        this.getPendingReviewHandler = getPendingReviewHandler;
    }

    @GetMapping("/pending")
    public ResponseEntity<List<TradeLicenseApplicationResponse>> getPendingReviews() {
        var applications = getPendingReviewHandler.handle(new GetPendingReviewApplicationsQuery());
        return ResponseEntity.ok(applications.stream()
                .map(TradeLicenseApplicationResponse::fromDomain)
                .toList());
    }

    @PostMapping("/{applicationId}")
    public ResponseEntity<TradeLicenseApplicationResponse> reviewApplication(
            @PathVariable UUID applicationId,
            @RequestBody ReviewTradeLicenseApplicationRequest request
    ) {
        var result = reviewHandler.handle(new ReviewTradeLicenseApplicationCommand(
                applicationId,
                request.reviewerId(),
                request.role(),
                request.decision(),
                request.comment()
        ));
        return ResponseEntity.ok(TradeLicenseApplicationResponse.fromDomain(result.data()));
    }
}
