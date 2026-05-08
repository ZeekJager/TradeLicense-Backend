package com.trade.tradelicense.domain.valueobjects;

public record AdjustmentReason(String value) {
    public AdjustmentReason {
        value = requireText(value, "Adjustment reason is required");
    }

    private static String requireText(String value, String message) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
        return value.trim();
    }
}
