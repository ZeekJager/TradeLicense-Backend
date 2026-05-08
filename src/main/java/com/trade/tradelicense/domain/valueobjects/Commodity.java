package com.trade.tradelicense.domain.valueobjects;

public record Commodity(String code, String name) {
    public Commodity {
        code = requireText(code, "Commodity code is required");
        name = requireText(name, "Commodity name is required");
    }

    private static String requireText(String value, String message) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
        return value.trim();
    }
}
