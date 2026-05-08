package com.trade.tradelicense.domain.valueobjects;

import java.util.Objects;
import java.util.UUID;

public record DocumentId(UUID value) {
    public DocumentId {
        Objects.requireNonNull(value, "Document id is required");
    }

    public static DocumentId newId() {
        return new DocumentId(UUID.randomUUID());
    }
}
