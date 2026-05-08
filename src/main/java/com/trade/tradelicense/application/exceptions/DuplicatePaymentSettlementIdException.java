package com.trade.tradelicense.application.exceptions;

public class DuplicatePaymentSettlementIdException extends DuplicateResourceException {
    public DuplicatePaymentSettlementIdException(Object value) {
        super("Payment settlement id already exists: " + value);
    }
}
