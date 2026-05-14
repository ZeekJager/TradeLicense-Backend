package com.trade.tradelicense.domain.aggregates;

import com.trade.tradelicense.domain.entities.User;
import com.trade.tradelicense.domain.enums.LicenseStatus;
import com.trade.tradelicense.domain.enums.UserRole;
import com.trade.tradelicense.domain.exceptions.InvalidApplicationStateException;
import com.trade.tradelicense.domain.exceptions.UnauthorizedDomainActionException;
import com.trade.tradelicense.domain.valueobjects.Actor;
import com.trade.tradelicense.domain.valueobjects.ApplicationId;
import com.trade.tradelicense.domain.valueobjects.Commodity;
import com.trade.tradelicense.domain.valueobjects.LicenseId;
import com.trade.tradelicense.domain.valueobjects.LicenseNumber;
import com.trade.tradelicense.domain.valueobjects.LicensePeriod;
import com.trade.tradelicense.domain.valueobjects.TinNumber;
import com.trade.tradelicense.domain.valueobjects.TradeLicenseType;
import com.trade.tradelicense.domain.valueobjects.TradeName;

import java.time.LocalDate;
import java.util.Objects;

public class TradeLicense {
    private final LicenseId id;
    private final LicenseNumber licenseNumber;
    private final ApplicationId sourceApplicationId;
    private final User licenseHolder;
    private final TinNumber tinNumber;
    private final TradeName tradeName;
    private TradeLicenseType licenseType;
    private Commodity commodity;
    private LicensePeriod licensePeriod;
    private final LocalDate issuedDate;
    private LicenseStatus status;

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
        this(id, licenseNumber, sourceApplicationId, licenseHolder, tinNumber, defaultTradeName(licenseHolder), licenseType, commodity, licensePeriod, issuedDate, LicenseStatus.ACTIVE);
    }

    public TradeLicense(
            LicenseId id,
            LicenseNumber licenseNumber,
            ApplicationId sourceApplicationId,
            User licenseHolder,
            TinNumber tinNumber,
            TradeLicenseType licenseType,
            Commodity commodity,
            LicensePeriod licensePeriod,
            LocalDate issuedDate,
            LicenseStatus status
    ) {
        this(id, licenseNumber, sourceApplicationId, licenseHolder, tinNumber, defaultTradeName(licenseHolder), licenseType, commodity, licensePeriod, issuedDate, status);
    }

    public TradeLicense(
            LicenseId id,
            LicenseNumber licenseNumber,
            ApplicationId sourceApplicationId,
            User licenseHolder,
            TinNumber tinNumber,
            TradeName tradeName,
            TradeLicenseType licenseType,
            Commodity commodity,
            LicensePeriod licensePeriod,
            LocalDate issuedDate,
            LicenseStatus status
    ) {
        this.id = Objects.requireNonNull(id, "License id is required");
        this.licenseNumber = Objects.requireNonNull(licenseNumber, "License number is required");
        this.sourceApplicationId = Objects.requireNonNull(sourceApplicationId, "Source application id is required");
        this.licenseHolder = Objects.requireNonNull(licenseHolder, "License holder is required");
        this.tinNumber = Objects.requireNonNull(tinNumber, "TIN number is required");
        this.tradeName = Objects.requireNonNull(tradeName, "Trade name is required");
        this.licenseType = Objects.requireNonNull(licenseType, "Trade license type is required");
        this.commodity = Objects.requireNonNull(commodity, "Commodity is required");
        this.licensePeriod = Objects.requireNonNull(licensePeriod, "License period is required");
        this.issuedDate = Objects.requireNonNull(issuedDate, "Issued date is required");
        this.status = Objects.requireNonNull(status, "License status is required");
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
                application.tradeName(),
                licenseTypeToIssue,
                application.commodity(),
                licensePeriod,
                LocalDate.now(),
                LicenseStatus.ACTIVE
        );
    }

    private static TradeName defaultTradeName(User licenseHolder) {
        return new TradeName(Objects.requireNonNull(licenseHolder, "License holder is required").getFullName().value());
    }

    public void updateDetails(Actor actor, TradeLicenseType licenseType, Commodity commodity) {
        Objects.requireNonNull(actor, "Actor is required");
        if (!canUpdate(actor)) {
            throw new UnauthorizedDomainActionException("Only the license holder or admin can update this trade license");
        }
        this.licenseType = Objects.requireNonNull(licenseType, "Trade license type is required");
        this.commodity = Objects.requireNonNull(commodity, "Commodity is required");
    }

    public void cancel(Actor actor) {
        Objects.requireNonNull(actor, "Actor is required");
        if (!canUpdate(actor)) {
            throw new UnauthorizedDomainActionException("Only the license holder or admin can cancel this trade license");
        }
        if (status == LicenseStatus.CANCELLED) {
            throw new InvalidApplicationStateException("Trade license is already cancelled");
        }
        this.status = LicenseStatus.CANCELLED;
    }

    public void renew(Actor actor, LicensePeriod renewalPeriod) {
        Objects.requireNonNull(actor, "Actor is required");
        if (!canUpdate(actor)) {
            throw new UnauthorizedDomainActionException("Only the license holder or admin can renew this trade license");
        }
        if (status == LicenseStatus.CANCELLED) {
            throw new InvalidApplicationStateException("Cancelled trade licenses cannot be renewed");
        }
        this.licensePeriod = Objects.requireNonNull(renewalPeriod, "Renewal period is required");
        this.status = LicenseStatus.ACTIVE;
    }

    private boolean canUpdate(Actor actor) {
        boolean holder = licenseHolder.getUserId().equals(actor.userId())
                && (actor.isCustomerOrLicensee());
        return holder || actor.role() == UserRole.ADMIN;
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

    public TradeName tradeName() {
        return tradeName;
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

    public LicenseStatus status() {
        return status;
    }
}
