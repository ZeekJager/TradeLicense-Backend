package com.trade.tradelicense.application.exceptions;

public class DuplicateDocumentIdException extends DuplicateResourceException {
    public DuplicateDocumentIdException(Object value) {
        super("Document id already exists: " + value);
    }
}
