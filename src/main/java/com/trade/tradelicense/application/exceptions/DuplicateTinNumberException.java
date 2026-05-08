package com.trade.tradelicense.application.exceptions;

public class DuplicateTinNumberException extends DuplicateResourceException {
    public DuplicateTinNumberException(Object value) {
        super("TIN number already exists: " + value);
    }
}
