package com.trade.tradelicense.presentation.dto;

import com.trade.tradelicense.domain.enums.ReviewDecision;
import com.trade.tradelicense.domain.enums.UserRole;

import java.util.UUID;

public record ReviewTradeLicenseApplicationRequest(
        UUID reviewerId,
        UserRole role,
        ReviewDecision decision,
        String comment
) {
}
