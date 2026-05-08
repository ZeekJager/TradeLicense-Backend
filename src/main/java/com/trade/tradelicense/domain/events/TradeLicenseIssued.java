package com.trade.tradelicense.domain.events;

import com.trade.tradelicense.domain.valueobjects.ApplicationId;
import com.trade.tradelicense.domain.valueobjects.LicenseId;
import com.trade.tradelicense.domain.valueobjects.LicenseNumber;

import java.time.LocalDateTime;
import java.util.Objects;

public record TradeLicenseIssued(ApplicationId applicationId, LicenseId licenseId, LicenseNumber licenseNumber, LocalDateTime occurredAt) {
    public TradeLicenseIssued {
        Objects.requireNonNull(applicationId, "Application id is required");
        Objects.requireNonNull(licenseId, "License id is required");
        Objects.requireNonNull(licenseNumber, "License number is required");
        Objects.requireNonNull(occurredAt, "Occurred date is required");
    }
}
