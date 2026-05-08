package com.trade.tradelicense.domain.valueobjects;

public record EmailAddress(String value) {
    public EmailAddress {
        value = requireText(value, "Email address is required");
        if (!value.contains("@")) {
            throw new IllegalArgumentException("Email address must contain @");
        }
    }

    private static String requireText(String value, String message) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
        return value.trim();
    }
}
