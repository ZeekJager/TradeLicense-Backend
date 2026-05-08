package com.trade.tradelicense.domain.factories;

import com.trade.tradelicense.domain.entities.DocumentPackage;
import com.trade.tradelicense.domain.valueobjects.TradeLicenseType;

import java.util.Objects;

public class RequiredDocumentPackageFactory {
    public DocumentPackage createFor(TradeLicenseType licenseType) {
        Objects.requireNonNull(licenseType, "Trade license type is required");
        return new DocumentPackage();
    }
}
