package com.trade.tradelicense.domain.exceptions;

public class MissingRequiredDocumentException extends DomainException {
    public MissingRequiredDocumentException(String message) {
        super(message);
    }
}
