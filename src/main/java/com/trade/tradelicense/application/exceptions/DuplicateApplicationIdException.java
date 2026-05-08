package com.trade.tradelicense.application.exceptions;

public class DuplicateApplicationIdException extends DuplicateResourceException {
    public DuplicateApplicationIdException(Object value) {
        super("Application id already exists: " + value);
    }
}
