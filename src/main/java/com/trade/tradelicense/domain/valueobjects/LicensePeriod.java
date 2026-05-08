package com.trade.tradelicense.domain.valueobjects;

import java.time.LocalDate;
import java.util.Objects;

public record LicensePeriod(LocalDate startsOn, LocalDate endsOn) {
    public LicensePeriod {
        Objects.requireNonNull(startsOn, "License period start date is required");
        Objects.requireNonNull(endsOn, "License period end date is required");
        if (endsOn.isBefore(startsOn)) {
            throw new IllegalArgumentException("License period end date cannot be before start date");
        }
    }
}
