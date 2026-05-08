package com.trade.tradelicense.domain.events;

import com.trade.tradelicense.domain.valueobjects.ApplicationId;
import com.trade.tradelicense.domain.valueobjects.ApproverId;
import com.trade.tradelicense.domain.valueobjects.RereviewReason;

import java.time.LocalDateTime;
import java.util.Objects;

public record TradeLicenseApplicationSentForRereview(ApplicationId applicationId, ApproverId approverId, RereviewReason reason, LocalDateTime occurredAt) {
    public TradeLicenseApplicationSentForRereview {
        Objects.requireNonNull(applicationId, "Application id is required");
        Objects.requireNonNull(approverId, "Approver id is required");
        Objects.requireNonNull(reason, "Rereview reason is required");
        Objects.requireNonNull(occurredAt, "Occurred date is required");
    }
}
