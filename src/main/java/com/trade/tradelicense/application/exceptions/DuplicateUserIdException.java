package com.trade.tradelicense.application.exceptions;

public class DuplicateUserIdException extends DuplicateResourceException {
    public DuplicateUserIdException(Object value) {
        super("User id already exists: " + value);
    }
}
