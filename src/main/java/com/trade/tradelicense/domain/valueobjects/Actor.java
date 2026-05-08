package com.trade.tradelicense.domain.valueobjects;

import com.trade.tradelicense.domain.enums.UserRole;

import java.util.Objects;

public record Actor(UserId userId, UserRole role) {
    public Actor {
        Objects.requireNonNull(userId, "Actor user id is required");
        Objects.requireNonNull(role, "Actor role is required");
    }

    public boolean isCustomerOrLicensee() {
        return role == UserRole.CUSTOMER || role == UserRole.LICENSEE;
    }

    public boolean isReviewer() {
        return role == UserRole.REVIEWER;
    }

    public boolean isApprover() {
        return role == UserRole.APPROVER;
    }
}
