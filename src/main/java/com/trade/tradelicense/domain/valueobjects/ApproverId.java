package com.trade.tradelicense.domain.valueobjects;

import java.util.Objects;
import java.util.UUID;

public record ApproverId(UUID value) {
    public ApproverId {
        Objects.requireNonNull(value, "Approver id is required");
    }

    public static ApproverId newId() {
        return new ApproverId(UUID.randomUUID());
    }
}
