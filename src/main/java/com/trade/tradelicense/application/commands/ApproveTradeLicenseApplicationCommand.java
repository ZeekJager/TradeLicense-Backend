package com.trade.tradelicense.application.commands;

import com.trade.tradelicense.domain.enums.ApprovalDecision;
import com.trade.tradelicense.domain.enums.UserRole;

import java.util.UUID;

public record ApproveTradeLicenseApplicationCommand(
        UUID applicationId,
        UUID approverId,
        UserRole role,
        ApprovalDecision decision,
        String comment,
        String licenseNumber,
        String tinNumber,
        String licenseTypeToIssue
) {
}
