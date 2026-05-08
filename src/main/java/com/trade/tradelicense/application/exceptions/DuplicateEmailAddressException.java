package com.trade.tradelicense.application.exceptions;

public class DuplicateEmailAddressException extends DuplicateResourceException {
    public DuplicateEmailAddressException(Object value) {
        super("Email address already exists: " + value);
    }
}
