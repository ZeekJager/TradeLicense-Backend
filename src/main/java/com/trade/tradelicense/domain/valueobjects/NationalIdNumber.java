package com.trade.tradelicense.domain.valueobjects;

public record NationalIdNumber(String value) {
    public NationalIdNumber {
        value = requireText(value, "National id number is required");
    }

    private static String requireText(String value, String message) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
        return value.trim();
    }
}
