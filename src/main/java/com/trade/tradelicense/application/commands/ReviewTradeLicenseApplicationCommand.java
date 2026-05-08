package com.trade.tradelicense.application.commands;

import com.trade.tradelicense.domain.enums.ReviewDecision;
import com.trade.tradelicense.domain.enums.UserRole;

import java.util.UUID;

public record ReviewTradeLicenseApplicationCommand(
        UUID applicationId,
        UUID reviewerId,
        UserRole role,
        ReviewDecision decision,
        String comment
) {
}
