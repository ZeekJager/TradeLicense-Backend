package com.trade.tradelicense.presentation.dto;

import com.trade.tradelicense.domain.enums.ApprovalDecision;
import com.trade.tradelicense.domain.enums.UserRole;

import java.util.UUID;

public record ApproveTradeLicenseApplicationRequest(
        UUID approverId,
        UserRole role,
        ApprovalDecision decision,
        String comment,
        String licenseNumber,
        String tinNumber,
        String licenseTypeToIssue
) {
}
