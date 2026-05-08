package com.trade.tradelicense.domain.aggregates;

import com.trade.tradelicense.domain.entities.User;
import com.trade.tradelicense.domain.valueobjects.ApplicationId;
import com.trade.tradelicense.domain.valueobjects.Commodity;
import com.trade.tradelicense.domain.valueobjects.LicenseId;
import com.trade.tradelicense.domain.valueobjects.LicenseNumber;
import com.trade.tradelicense.domain.valueobjects.LicensePeriod;
import com.trade.tradelicense.domain.valueobjects.TinNumber;
import com.trade.tradelicense.domain.valueobjects.TradeLicenseType;

import java.time.LocalDate;
import java.util.Objects;

public class TradeLicense {
    private final LicenseId id;
    private final LicenseNumber licenseNumber;
    private final ApplicationId sourceApplicationId;
    private final User licenseHolder;
    private final TinNumber tinNumber;
    private final TradeLicenseType licenseType;
    private final Commodity commodity;
    private final LicensePeriod licensePeriod;
    private final LocalDate issuedDate;

    public TradeLicense(
            LicenseId id,
            LicenseNumber licenseNumber,
            ApplicationId sourceApplicationId,
            User licenseHolder,
            TinNumber tinNumber,
            TradeLicenseType licenseType,
            Commodity commodity,
            LicensePeriod licensePeriod,
            LocalDate issuedDate
    ) {
        this.id = Objects.requireNonNull(id, "License id is required");
        this.licenseNumber = Objects.requireNonNull(licenseNumber, "License number is required");
        this.sourceApplicationId = Objects.requireNonNull(sourceApplicationId, "Source application id is required");
        this.licenseHolder = Objects.requireNonNull(licenseHolder, "License holder is required");
        this.tinNumber = Objects.requireNonNull(tinNumber, "TIN number is required");
        this.licenseType = Objects.requireNonNull(licenseType, "Trade license type is required");
        this.commodity = Objects.requireNonNull(commodity, "Commodity is required");
        this.licensePeriod = Objects.requireNonNull(licensePeriod, "License period is required");
        this.issuedDate = Objects.requireNonNull(issuedDate, "Issued date is required");
    }

    public static TradeLicense issue(
            TradeLicenseApplication application,
            LicenseNumber licenseNumber,
            TinNumber tinNumber,
            TradeLicenseType licenseTypeToIssue,
            LicensePeriod licensePeriod
    ) {
        Objects.requireNonNull(application, "Application is required");
        return new TradeLicense(
                LicenseId.newId(),
                licenseNumber,
                application.id(),
                application.applicant(),
                tinNumber,
                licenseTypeToIssue,
                application.commodity(),
                licensePeriod,
                LocalDate.now()
        );
    }

    public LicenseId id() {
        return id;
    }

    public LicenseNumber licenseNumber() {
        return licenseNumber;
    }

    public ApplicationId sourceApplicationId() {
        return sourceApplicationId;
    }

    public User licenseHolder() {
        return licenseHolder;
    }

    public TinNumber tinNumber() {
        return tinNumber;
    }

    public TradeLicenseType licenseType() {
        return licenseType;
    }

    public Commodity commodity() {
        return commodity;
    }

    public LicensePeriod licensePeriod() {
        return licensePeriod;
    }

    public LocalDate issuedDate() {
        return issuedDate;
    }
}
