package com.trade.tradelicense.application.common;

import com.trade.tradelicense.domain.aggregates.TradeLicense;
import com.trade.tradelicense.domain.valueobjects.ApplicationId;
import com.trade.tradelicense.domain.valueobjects.LicenseId;
import com.trade.tradelicense.domain.valueobjects.LicenseNumber;
import com.trade.tradelicense.domain.valueobjects.TinNumber;
import com.trade.tradelicense.domain.valueobjects.UserId;

import java.util.List;
import java.util.Optional;

public interface TradeLicenseRepositoryPort {
    TradeLicense save(TradeLicense tradeLicense);

    Optional<TradeLicense> findById(LicenseId licenseId);

    Optional<TradeLicense> findBySourceApplicationId(ApplicationId applicationId);

    List<TradeLicense> findByLicenseHolderId(UserId userId);

    boolean existsByLicenseId(LicenseId licenseId);

    boolean existsByLicenseNumber(LicenseNumber licenseNumber);

    boolean existsByTinNumber(TinNumber tinNumber);
}
