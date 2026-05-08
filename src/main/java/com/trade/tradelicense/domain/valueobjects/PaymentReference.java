package com.trade.tradelicense.domain.valueobjects;

public record PaymentReference(String value) {
    public PaymentReference {
        value = requireText(value, "Payment reference is required");
    }

    private static String requireText(String value, String message) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
        return value.trim();
    }
}
