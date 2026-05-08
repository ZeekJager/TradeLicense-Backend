package com.trade.tradelicense.domain.events;

import com.trade.tradelicense.domain.valueobjects.ApplicationId;
import com.trade.tradelicense.domain.valueobjects.RejectionReason;
import com.trade.tradelicense.domain.valueobjects.ReviewerId;

import java.time.LocalDateTime;
import java.util.Objects;

public record TradeLicenseApplicationRejectedByReviewer(ApplicationId applicationId, ReviewerId reviewerId, RejectionReason reason, LocalDateTime occurredAt) {
    public TradeLicenseApplicationRejectedByReviewer {
        Objects.requireNonNull(applicationId, "Application id is required");
        Objects.requireNonNull(reviewerId, "Reviewer id is required");
        Objects.requireNonNull(reason, "Rejection reason is required");
        Objects.requireNonNull(occurredAt, "Occurred date is required");
    }
}
