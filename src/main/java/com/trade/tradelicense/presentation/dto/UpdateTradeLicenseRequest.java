package com.trade.tradelicense.presentation.dto;

import com.trade.tradelicense.domain.enums.UserRole;

import java.util.UUID;

public record UpdateTradeLicenseRequest(
        UUID actorId,
        UserRole role,
        String tradeLicenseType,
        String commodity
) {
}
