package com.trade.tradelicense.presentation.dto;

import java.util.List;

public record LicenseTypeConfigRequest(
        String code,
        String name,
        List<String> requiredDocuments,
        Boolean active
) {
}
