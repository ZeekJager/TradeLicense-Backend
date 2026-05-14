package com.trade.tradelicense.domain.valueobjects;

public record TradeName(String value) {
    public TradeName {
        value = requireText(value, "Trade name is required");
    }

    private static String requireText(String value, String message) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
        return value.trim();
    }
}
