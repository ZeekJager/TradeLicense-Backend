package com.trade.tradelicense.infrastructure.persistence;

import com.trade.tradelicense.domain.enums.UserRole;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "user_accounts")
public class JpaUserAccountEntity {
    @Id
    private UUID id;

    private String fullName;

    @Column(unique = true)
    private String tinNumber;

    @Column(unique = true, nullable = false)
    private String email;

    private String region;

    private String businessAddress;

    private String legalCondition;

    @Column(unique = true)
    private String bankAccountNumber;

    @Column(nullable = false)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    private boolean active;
    private LocalDateTime createdAt;

    protected JpaUserAccountEntity() {
    }

    public JpaUserAccountEntity(
            UUID id,
            String fullName,
            String email,
            String passwordHash,
            UserRole role,
            boolean active,
            LocalDateTime createdAt
    ) {
        this(id, fullName, null, email, null, null, null, null, passwordHash, role, active, createdAt);
    }

    public JpaUserAccountEntity(
            UUID id,
            String fullName,
            String tinNumber,
            String email,
            String region,
            String businessAddress,
            String legalCondition,
            String bankAccountNumber,
            String passwordHash,
            UserRole role,
            boolean active,
            LocalDateTime createdAt
    ) {
        this.id = id;
        this.fullName = fullName;
        this.tinNumber = tinNumber;
        this.email = email;
        this.region = region;
        this.businessAddress = businessAddress;
        this.legalCondition = legalCondition;
        this.bankAccountNumber = bankAccountNumber;
        this.passwordHash = passwordHash;
        this.role = role;
        this.active = active;
        this.createdAt = createdAt;
    }

    public UUID getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getTinNumber() {
        return tinNumber;
    }

    public String getEmail() {
        return email;
    }

    public String getRegion() {
        return region;
    }

    public String getBusinessAddress() {
        return businessAddress;
    }

    public String getLegalCondition() {
        return legalCondition;
    }

    public String getBankAccountNumber() {
        return bankAccountNumber;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public UserRole getRole() {
        return role;
    }

    public boolean isActive() {
        return active;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void updateRole(UserRole role) {
        this.role = role;
    }

    public void updateActive(boolean active) {
        this.active = active;
    }
}
