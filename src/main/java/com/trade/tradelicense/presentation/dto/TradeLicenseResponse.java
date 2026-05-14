package com.trade.tradelicense.presentation.dto;

import com.trade.tradelicense.domain.aggregates.TradeLicense;
import com.trade.tradelicense.domain.entities.User;
import com.trade.tradelicense.domain.enums.LicenseStatus;

import java.time.LocalDate;
import java.util.UUID;

public record TradeLicenseResponse(
        UUID licenseId,
        String licenseNumber,
        UUID sourceApplicationId,
        UUID licenseHolderId,
        String fullName,
        String nationalIdNumber,
        String email,
        String phoneNumber,
        String tinNumber,
        String tradeName,
        String tradeLicenseType,
        String commodity,
        LocalDate validFrom,
        LocalDate validTo,
        LocalDate issuedDate,
        LicenseStatus status
) {
    public static TradeLicenseResponse fromDomain(TradeLicense tradeLicense) {
        User holder = tradeLicense.licenseHolder();
        return new TradeLicenseResponse(
                tradeLicense.id().value(),
                tradeLicense.licenseNumber().value(),
                tradeLicense.sourceApplicationId().value(),
                holder.getUserId().value(),
                holder.getFullName().value(),
                holder.getNationalIdNumber().value(),
                holder.getEmailAddress().value(),
                holder.getPhoneNumber().value(),
                tradeLicense.tinNumber().value(),
                tradeLicense.tradeName().value(),
                tradeLicense.licenseType().code(),
                tradeLicense.commodity().code(),
                tradeLicense.licensePeriod().startsOn(),
                tradeLicense.licensePeriod().endsOn(),
                tradeLicense.issuedDate(),
                tradeLicense.status()
        );
    }
}
