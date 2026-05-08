package com.trade.tradelicense.application.exceptions;

public class DuplicateReviewerIdException extends DuplicateResourceException {
    public DuplicateReviewerIdException(Object value) {
        super("Reviewer id already exists: " + value);
    }
}
