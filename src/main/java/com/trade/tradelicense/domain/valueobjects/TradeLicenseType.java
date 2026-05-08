package com.trade.tradelicense.domain.valueobjects;

public record TradeLicenseType(String code, String name) {
    public TradeLicenseType {
        code = requireText(code, "Trade license type code is required");
        name = requireText(name, "Trade license type name is required");
    }

    private static String requireText(String value, String message) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
        return value.trim();
    }
}
