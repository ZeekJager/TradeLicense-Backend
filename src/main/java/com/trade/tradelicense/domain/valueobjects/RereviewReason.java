package com.trade.tradelicense.domain.valueobjects;

public record RereviewReason(String value) {
    public RereviewReason {
        value = requireText(value, "Rereview reason is required");
    }

    private static String requireText(String value, String message) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
        return value.trim();
    }
}
