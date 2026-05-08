package com.trade.tradelicense.application.exceptions;

public class DuplicateFullNameException extends DuplicateResourceException {
    public DuplicateFullNameException(Object value) {
        super("Full name already exists: " + value);
    }
}
