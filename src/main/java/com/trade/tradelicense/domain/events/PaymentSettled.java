package com.trade.tradelicense.domain.events;

import com.trade.tradelicense.domain.valueobjects.ApplicationId;
import com.trade.tradelicense.domain.valueobjects.PaymentSettlementId;

import java.time.LocalDateTime;
import java.util.Objects;

public record PaymentSettled(ApplicationId applicationId, PaymentSettlementId paymentSettlementId, LocalDateTime occurredAt) {
    public PaymentSettled {
        Objects.requireNonNull(applicationId, "Application id is required");
        Objects.requireNonNull(paymentSettlementId, "Payment settlement id is required");
        Objects.requireNonNull(occurredAt, "Occurred date is required");
    }
}
