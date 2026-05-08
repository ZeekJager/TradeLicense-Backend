package com.trade.tradelicense.domain.valueobjects;

import java.util.Objects;
import java.util.UUID;

public record LicenseId(UUID value) {
    public LicenseId {
        Objects.requireNonNull(value, "License id is required");
    }

    public static LicenseId newId() {
        return new LicenseId(UUID.randomUUID());
    }
}
