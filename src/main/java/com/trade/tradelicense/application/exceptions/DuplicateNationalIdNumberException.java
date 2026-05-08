package com.trade.tradelicense.application.exceptions;

public class DuplicateNationalIdNumberException extends DuplicateResourceException {
    public DuplicateNationalIdNumberException(Object value) {
        super("National id number already exists: " + value);
    }
}
