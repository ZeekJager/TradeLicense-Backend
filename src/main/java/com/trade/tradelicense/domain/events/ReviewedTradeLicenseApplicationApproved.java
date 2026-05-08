package com.trade.tradelicense.domain.events;

import com.trade.tradelicense.domain.valueobjects.ApplicationId;
import com.trade.tradelicense.domain.valueobjects.ApproverId;

import java.time.LocalDateTime;
import java.util.Objects;

public record ReviewedTradeLicenseApplicationApproved(ApplicationId applicationId, ApproverId approverId, LocalDateTime occurredAt) {
    public ReviewedTradeLicenseApplicationApproved {
        Objects.requireNonNull(applicationId, "Application id is required");
        Objects.requireNonNull(approverId, "Approver id is required");
        Objects.requireNonNull(occurredAt, "Occurred date is required");
    }
}
