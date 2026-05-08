package com.trade.tradelicense.domain.events;

import com.trade.tradelicense.domain.valueobjects.ApplicationId;

import java.time.LocalDateTime;
import java.util.Objects;

public record TradeLicenseApplicationCancelled(ApplicationId applicationId, LocalDateTime occurredAt) {
    public TradeLicenseApplicationCancelled {
        Objects.requireNonNull(applicationId, "Application id is required");
        Objects.requireNonNull(occurredAt, "Occurred date is required");
    }
}
