package com.trade.tradelicense.domain.exceptions;

public class InvalidApplicationStateException extends DomainException {
    public InvalidApplicationStateException(String message) {
        super(message);
    }
}
