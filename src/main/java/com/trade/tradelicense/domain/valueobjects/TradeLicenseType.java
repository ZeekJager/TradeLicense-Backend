package com.trade.tradelicense.domain.valueobjects;

import java.util.List;

public record TradeLicenseType(String code, String name) {
    public static final String LLP = "LLP";
    public static final String NGO = "NGO";
    public static final String ONE_MAN_PLC = "ONE_MAN_PLC";
    public static final String PARTNERSHIP = "PARTNERSHIP";
    public static final String COOPERATIVE_ASSOCIATION = "COOPERATIVE_ASSOCIATION";
    public static final String PUBLIC_ENTERPRISE = "PUBLIC_ENTERPRISE";
    public static final String PRIVATE = "PRIVATE";
    public static final String PLC = "PLC";
    public static final String SHARE_COMPANY = "SHARE_COMPANY";
    public static final String BORDER_TALE = "BORDER_TALE";

    public TradeLicenseType {
        code = requireText(code, "Trade license type code is required");
        name = requireText(name, "Trade license type name is required");
    }

    public static List<TradeLicenseType> supportedTypes() {
        return List.of(
                new TradeLicenseType(LLP, "LLP"),
                new TradeLicenseType(NGO, "NGO"),
                new TradeLicenseType(ONE_MAN_PLC, "One man PLC"),
                new TradeLicenseType(PARTNERSHIP, "Partnership"),
                new TradeLicenseType(COOPERATIVE_ASSOCIATION, "Cooperative Association"),
                new TradeLicenseType(PUBLIC_ENTERPRISE, "Public Enterprise"),
                new TradeLicenseType(PRIVATE, "Private"),
                new TradeLicenseType(PLC, "PLC"),
                new TradeLicenseType(SHARE_COMPANY, "Share Company"),
                new TradeLicenseType(BORDER_TALE, "Border Tale")
        );
    }

    private static String requireText(String value, String message) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
        return value.trim();
    }
}
