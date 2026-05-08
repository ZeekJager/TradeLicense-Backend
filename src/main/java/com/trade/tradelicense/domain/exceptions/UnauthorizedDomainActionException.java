package com.trade.tradelicense.domain.exceptions;

public class UnauthorizedDomainActionException extends DomainException {
    public UnauthorizedDomainActionException(String message) {
        super(message);
    }
}
