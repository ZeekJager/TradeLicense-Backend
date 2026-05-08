package com.trade.tradelicense.domain.valueobjects;

import java.util.Objects;
import java.util.UUID;

public record UserId(UUID value) {
    public UserId {
        Objects.requireNonNull(value, "User id is required");
    }

    public static UserId newId() {
        return new UserId(UUID.randomUUID());
    }
}
