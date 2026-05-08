package com.trade.tradelicense.domain.factories;

import com.trade.tradelicense.domain.aggregates.TradeLicense;
import com.trade.tradelicense.domain.aggregates.TradeLicenseApplication;
import com.trade.tradelicense.domain.enums.ApplicationStatus;
import com.trade.tradelicense.domain.exceptions.InvalidApplicationStateException;
import com.trade.tradelicense.domain.valueobjects.LicenseNumber;
import com.trade.tradelicense.domain.valueobjects.LicensePeriod;
import com.trade.tradelicense.domain.valueobjects.TinNumber;
import com.trade.tradelicense.domain.valueobjects.TradeLicenseType;

import java.util.Objects;

public class TradeLicenseFactory {
    public TradeLicense issueLicense(
            TradeLicenseApplication application,
            LicenseNumber licenseNumber,
            TinNumber tinNumber,
            TradeLicenseType licenseTypeToIssue,
            LicensePeriod licensePeriod
    ) {
        Objects.requireNonNull(application, "Application is required");
        Objects.requireNonNull(tinNumber, "TIN number is required");
        Objects.requireNonNull(licenseTypeToIssue, "License type to issue is required");
        if (application.status() != ApplicationStatus.APPROVED) {
            throw new InvalidApplicationStateException("Trade license can only be issued from an approved application");
        }
        TradeLicense tradeLicense = TradeLicense.issue(application, licenseNumber, tinNumber, licenseTypeToIssue, licensePeriod);
        application.markLicenseIssued();
        return tradeLicense;
    }
}
