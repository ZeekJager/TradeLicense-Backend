package com.trade.tradelicense.application.exceptions;

public class DuplicateApproverIdException extends DuplicateResourceException {
    public DuplicateApproverIdException(Object value) {
        super("Approver id already exists: " + value);
    }
}
