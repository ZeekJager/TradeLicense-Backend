package com.trade.tradelicense.domain.valueobjects;

import java.util.Objects;
import java.util.UUID;

public record ApplicationId(UUID value) {
    public ApplicationId {
        Objects.requireNonNull(value, "Application id is required");
    }

    public static ApplicationId newId() {
        return new ApplicationId(UUID.randomUUID());
    }
}
