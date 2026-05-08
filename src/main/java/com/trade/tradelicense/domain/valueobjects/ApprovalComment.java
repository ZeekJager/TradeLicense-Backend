package com.trade.tradelicense.domain.valueobjects;

public record ApprovalComment(String value) {
    public ApprovalComment {
        value = requireText(value, "Approval comment is required");
    }

    private static String requireText(String value, String message) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
        return value.trim();
    }
}
