package com.trade.tradelicense.domain.events;

import com.trade.tradelicense.domain.valueobjects.ApplicationId;
import com.trade.tradelicense.domain.valueobjects.ReviewerId;

import java.time.LocalDateTime;
import java.util.Objects;

public record TradeLicenseApplicationAcceptedByReviewer(ApplicationId applicationId, ReviewerId reviewerId, LocalDateTime occurredAt) {
    public TradeLicenseApplicationAcceptedByReviewer {
        Objects.requireNonNull(applicationId, "Application id is required");
        Objects.requireNonNull(reviewerId, "Reviewer id is required");
        Objects.requireNonNull(occurredAt, "Occurred date is required");
    }
}
