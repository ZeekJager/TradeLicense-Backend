package com.trade.tradelicense.domain.events;

import com.trade.tradelicense.domain.valueobjects.AdjustmentReason;
import com.trade.tradelicense.domain.valueobjects.ApplicationId;
import com.trade.tradelicense.domain.valueobjects.ReviewerId;

import java.time.LocalDateTime;
import java.util.Objects;

public record TradeLicenseApplicationReturnedForAdjustment(ApplicationId applicationId, ReviewerId reviewerId, AdjustmentReason reason, LocalDateTime occurredAt) {
    public TradeLicenseApplicationReturnedForAdjustment {
        Objects.requireNonNull(applicationId, "Application id is required");
        Objects.requireNonNull(reviewerId, "Reviewer id is required");
        Objects.requireNonNull(reason, "Adjustment reason is required");
        Objects.requireNonNull(occurredAt, "Occurred date is required");
    }
}
