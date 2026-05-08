package com.trade.tradelicense.application.exceptions;

public class DuplicateLicenseNumberException extends DuplicateResourceException {
    public DuplicateLicenseNumberException(Object value) {
        super("License number already exists: " + value);
    }
}
