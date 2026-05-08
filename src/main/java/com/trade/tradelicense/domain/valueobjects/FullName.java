package com.trade.tradelicense.domain.valueobjects;

public record FullName(String value) {
    public FullName {
        value = requireText(value, "Full name is required");
    }

    private static String requireText(String value, String message) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
        return value.trim();
    }
}
