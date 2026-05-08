package com.trade.tradelicense.application.exceptions;

public class DuplicatePhoneNumberException extends DuplicateResourceException {
    public DuplicatePhoneNumberException(Object value) {
        super("Phone number already exists: " + value);
    }
}
