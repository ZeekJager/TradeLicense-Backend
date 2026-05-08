package com.trade.tradelicense.domain.entities;

import com.trade.tradelicense.domain.enums.ApprovalDecision;
import com.trade.tradelicense.domain.valueobjects.ApprovalComment;
import com.trade.tradelicense.domain.valueobjects.ApproverId;

import java.time.LocalDateTime;
import java.util.Objects;

public record ApprovalRecord(ApproverId approver, ApprovalDecision decision, ApprovalComment comment, LocalDateTime approvedAt) {
    public ApprovalRecord {
        Objects.requireNonNull(approver, "Approver is required");
        Objects.requireNonNull(decision, "Approval decision is required");
        Objects.requireNonNull(comment, "Approval comment is required");
        Objects.requireNonNull(approvedAt, "Approved date is required");
    }

    public static ApprovalRecord create(ApproverId approver, ApprovalDecision decision, ApprovalComment comment) {
        return new ApprovalRecord(approver, decision, comment, LocalDateTime.now());
    }
}
