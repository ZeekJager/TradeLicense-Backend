package com.trade.tradelicense.domain.valueobjects;

import java.util.Objects;
import java.util.UUID;

public record PaymentSettlementId(UUID value) {
    public PaymentSettlementId {
        Objects.requireNonNull(value, "Payment settlement id is required");
    }

    public static PaymentSettlementId newId() {
        return new PaymentSettlementId(UUID.randomUUID());
    }
}
