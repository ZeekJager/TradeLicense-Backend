package com.trade.tradelicense.domain.exceptions;

public class PaymentNotSettledException extends DomainException {
    public PaymentNotSettledException(String message) {
        super(message);
    }
}
