package com.trade.tradelicense.domain.events;

import com.trade.tradelicense.domain.valueobjects.ApplicationId;
import com.trade.tradelicense.domain.valueobjects.ApproverId;
import com.trade.tradelicense.domain.valueobjects.RejectionReason;

import java.time.LocalDateTime;
import java.util.Objects;

public record ReviewedTradeLicenseApplicationRejected(ApplicationId applicationId, ApproverId approverId, RejectionReason reason, LocalDateTime occurredAt) {
    public ReviewedTradeLicenseApplicationRejected {
        Objects.requireNonNull(applicationId, "Application id is required");
        Objects.requireNonNull(approverId, "Approver id is required");
        Objects.requireNonNull(reason, "Rejection reason is required");
        Objects.requireNonNull(occurredAt, "Occurred date is required");
    }
}
