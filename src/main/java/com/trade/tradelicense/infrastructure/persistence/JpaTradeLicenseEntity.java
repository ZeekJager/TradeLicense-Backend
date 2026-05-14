package com.trade.tradelicense.infrastructure.persistence;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import com.trade.tradelicense.domain.enums.LicenseStatus;
import com.trade.tradelicense.domain.enums.UserRole;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "trade_licenses")
public class JpaTradeLicenseEntity {
    @Id
    private UUID id;
    @Column(unique = true)
    private String licenseNumber;
    private UUID sourceApplicationId;
    private UUID applicantId;
    @Enumerated(EnumType.STRING)
    private UserRole applicantRole;
    private String fullName;
    private String nationalIdNumber;
    private String email;
    private String phoneNumber;
    @Column(unique = true)
    private String tinNumber;
    private String tradeName;
    private String tradeLicenseType;
    private String commodity;
    private LocalDate licensePeriodStart;
    private LocalDate licensePeriodEnd;
    private LocalDate issuedDate;
    @Enumerated(EnumType.STRING)
    private LicenseStatus status;

    protected JpaTradeLicenseEntity() {
    }

    public JpaTradeLicenseEntity(
            UUID id,
            String licenseNumber,
            UUID sourceApplicationId,
            UUID applicantId,
            UserRole applicantRole,
            String fullName,
            String nationalIdNumber,
            String email,
            String phoneNumber,
            String tinNumber,
            String tradeName,
            String tradeLicenseType,
            String commodity,
            LocalDate licensePeriodStart,
            LocalDate licensePeriodEnd,
            LocalDate issuedDate,
            LicenseStatus status
    ) {
        this.id = id;
        this.licenseNumber = licenseNumber;
        this.sourceApplicationId = sourceApplicationId;
        this.applicantId = applicantId;
        this.applicantRole = applicantRole;
        this.fullName = fullName;
        this.nationalIdNumber = nationalIdNumber;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.tinNumber = tinNumber;
        this.tradeName = tradeName;
        this.tradeLicenseType = tradeLicenseType;
        this.commodity = commodity;
        this.licensePeriodStart = licensePeriodStart;
        this.licensePeriodEnd = licensePeriodEnd;
        this.issuedDate = issuedDate;
        this.status = status;
    }

    public UUID getId() {
        return id;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public UUID getSourceApplicationId() {
        return sourceApplicationId;
    }

    public UUID getApplicantId() {
        return applicantId;
    }

    public UserRole getApplicantRole() {
        return applicantRole;
    }

    public String getFullName() {
        return fullName;
    }

    public String getNationalIdNumber() {
        return nationalIdNumber;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getTinNumber() {
        return tinNumber;
    }

    public String getTradeName() {
        return tradeName;
    }

    public String getTradeLicenseType() {
        return tradeLicenseType;
    }

    public String getCommodity() {
        return commodity;
    }

    public LocalDate getLicensePeriodStart() {
        return licensePeriodStart;
    }

    public LocalDate getLicensePeriodEnd() {
        return licensePeriodEnd;
    }

    public LocalDate getIssuedDate() {
        return issuedDate;
    }

    public LicenseStatus getStatus() {
        return status;
    }
}
