package com.trade.tradelicense.domain.valueobjects;

import java.util.Objects;
import java.util.UUID;

public record ReviewerId(UUID value) {
    public ReviewerId {
        Objects.requireNonNull(value, "Reviewer id is required");
    }

    public static ReviewerId newId() {
        return new ReviewerId(UUID.randomUUID());
    }
}
