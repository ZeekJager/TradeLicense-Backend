package com.trade.tradelicense.application.common;

import com.trade.tradelicense.domain.aggregates.TradeLicense;
import com.trade.tradelicense.domain.valueobjects.LicenseId;
import com.trade.tradelicense.domain.valueobjects.LicenseNumber;
import com.trade.tradelicense.domain.valueobjects.TinNumber;

public interface TradeLicenseRepositoryPort {
    TradeLicense save(TradeLicense tradeLicense);

    boolean existsByLicenseId(LicenseId licenseId);

    boolean existsByLicenseNumber(LicenseNumber licenseNumber);

    boolean existsByTinNumber(TinNumber tinNumber);
}
