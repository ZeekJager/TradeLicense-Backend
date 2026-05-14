package com.trade.tradelicense.application.commands;

import com.trade.tradelicense.domain.enums.UserRole;

import java.util.UUID;

public record UpdateTradeLicenseCommand(
        UUID licenseId,
        UUID actorId,
        UserRole role,
        String tradeLicenseType,
        String commodity
) {
}
