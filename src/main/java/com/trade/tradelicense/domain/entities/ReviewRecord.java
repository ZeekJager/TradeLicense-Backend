package com.trade.tradelicense.domain.entities;

import com.trade.tradelicense.domain.enums.ReviewDecision;
import com.trade.tradelicense.domain.valueobjects.ReviewComment;
import com.trade.tradelicense.domain.valueobjects.ReviewerId;

import java.time.LocalDateTime;
import java.util.Objects;

public record ReviewRecord(ReviewerId reviewer, ReviewDecision decision, ReviewComment comment, LocalDateTime reviewedAt) {
    public ReviewRecord {
        Objects.requireNonNull(reviewer, "Reviewer is required");
        Objects.requireNonNull(decision, "Review decision is required");
        Objects.requireNonNull(comment, "Review comment is required");
        Objects.requireNonNull(reviewedAt, "Reviewed date is required");
    }

    public static ReviewRecord create(ReviewerId reviewer, ReviewDecision decision, ReviewComment comment) {
        return new ReviewRecord(reviewer, decision, comment, LocalDateTime.now());
    }
}
