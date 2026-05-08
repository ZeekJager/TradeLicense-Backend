package com.trade.tradelicense.domain.valueobjects;

public record PhoneNumber(String value) {
    public PhoneNumber {
        value = requireText(value, "Phone number is required");
    }

    private static String requireText(String value, String message) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
        return value.trim();
    }
}
