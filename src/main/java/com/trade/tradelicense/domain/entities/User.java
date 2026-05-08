package com.trade.tradelicense.domain.entities;

import com.trade.tradelicense.domain.enums.UserRole;
import com.trade.tradelicense.domain.valueobjects.EmailAddress;
import com.trade.tradelicense.domain.valueobjects.FullName;
import com.trade.tradelicense.domain.valueobjects.NationalIdNumber;
import com.trade.tradelicense.domain.valueobjects.PhoneNumber;
import com.trade.tradelicense.domain.valueobjects.UserId;

import java.util.Objects;

public class User {
    private final UserId userId;
    private final UserRole role;
    private final FullName fullName;
    private final NationalIdNumber nationalIdNumber;
    private final EmailAddress emailAddress;
    private final PhoneNumber phoneNumber;

    public User(
            UserId userId,
            UserRole role,
            FullName fullName,
            NationalIdNumber nationalIdNumber,
            EmailAddress emailAddress,
            PhoneNumber phoneNumber
    ) {
        this.userId = Objects.requireNonNull(userId, "User id is required");
        this.role = Objects.requireNonNull(role, "User role is required");
        this.fullName = Objects.requireNonNull(fullName, "Full name is required");
        this.nationalIdNumber = Objects.requireNonNull(nationalIdNumber, "National id number is required");
        this.emailAddress = Objects.requireNonNull(emailAddress, "Email address is required");
        this.phoneNumber = Objects.requireNonNull(phoneNumber, "Phone number is required");
    }

    public UserId getUserId() {
        return userId;
    }

    public UserRole getRole() {
        return role;
    }

    public FullName getFullName() {
        return fullName;
    }

    public NationalIdNumber getNationalIdNumber() {
        return nationalIdNumber;
    }

    public EmailAddress getEmailAddress() {
        return emailAddress;
    }

    public PhoneNumber getPhoneNumber() {
        return phoneNumber;
    }

    public boolean isCustomer() {
        return role == UserRole.CUSTOMER;
    }

    public boolean isReviewer() {
        return role == UserRole.REVIEWER;
    }

    public boolean isApprover() {
        return role == UserRole.APPROVER;
    }
}
