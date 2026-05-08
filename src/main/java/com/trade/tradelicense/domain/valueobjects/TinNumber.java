package com.trade.tradelicense.domain.valueobjects;

public record TinNumber(String value) {
    public TinNumber {
        value = requireText(value, "TIN number is required");
    }

    private static String requireText(String value, String message) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
        return value.trim();
    }
}
