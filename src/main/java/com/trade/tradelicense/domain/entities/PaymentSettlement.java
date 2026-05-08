package com.trade.tradelicense.domain.entities;

import com.trade.tradelicense.domain.enums.PaymentStatus;
import com.trade.tradelicense.domain.valueobjects.Money;
import com.trade.tradelicense.domain.valueobjects.PaymentReference;
import com.trade.tradelicense.domain.valueobjects.PaymentSettlementId;

import java.time.LocalDateTime;
import java.util.Objects;

public class PaymentSettlement {
    private final PaymentSettlementId id;
    private final Money money;
    private final PaymentReference paymentReference;
    private PaymentStatus status;
    private LocalDateTime settledAt;

    public PaymentSettlement(PaymentSettlementId id, Money money, PaymentReference paymentReference, PaymentStatus status, LocalDateTime settledAt) {
        this.id = Objects.requireNonNull(id, "Payment settlement id is required");
        this.money = Objects.requireNonNull(money, "Money is required");
        this.paymentReference = Objects.requireNonNull(paymentReference, "Payment reference is required");
        this.status = Objects.requireNonNull(status, "Payment status is required");
        this.settledAt = settledAt;
    }

    public static PaymentSettlement pending(Money money, PaymentReference paymentReference) {
        return new PaymentSettlement(PaymentSettlementId.newId(), money, paymentReference, PaymentStatus.PENDING, null);
    }

    public void settle() {
        this.status = PaymentStatus.SETTLED;
        this.settledAt = LocalDateTime.now();
    }

    public boolean isSettled() {
        return status == PaymentStatus.SETTLED;
    }

    public PaymentSettlementId id() {
        return id;
    }

    public Money money() {
        return money;
    }

    public PaymentReference paymentReference() {
        return paymentReference;
    }

    public PaymentStatus status() {
        return status;
    }

    public LocalDateTime settledAt() {
        return settledAt;
    }
}
