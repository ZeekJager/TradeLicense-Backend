package com.trade.tradelicense.application.exceptions;

public class DuplicateLicenseIdException extends DuplicateResourceException {
    public DuplicateLicenseIdException(Object value) {
        super("License id already exists: " + value);
    }
}
