package com.trade.tradelicense.domain.valueobjects;

public record DocumentType(String value) {
    public DocumentType {
        value = requireText(value, "Document type is required");
    }

    private static String requireText(String value, String message) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
        return value.trim();
    }
}
