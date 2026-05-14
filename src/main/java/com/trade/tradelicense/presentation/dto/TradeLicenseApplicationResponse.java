package com.trade.tradelicense.presentation.dto;

import com.trade.tradelicense.domain.aggregates.TradeLicenseApplication;
import com.trade.tradelicense.domain.entities.User;
import com.trade.tradelicense.domain.enums.ApplicationStatus;

import java.util.UUID;

public record TradeLicenseApplicationResponse(
        UUID applicationId,
        UUID applicantId,
        String fullName,
        String tradeName,
        String nationalIdNumber,
        String email,
        String phoneNumber,
        String tradeLicenseType,
        String commodity,
        String bankAccountNumber,
        ApplicationStatus status
) {
    public static TradeLicenseApplicationResponse fromDomain(TradeLicenseApplication application) {
        User applicant = application.applicant();
        return new TradeLicenseApplicationResponse(
                application.id().value(),
                applicant.getUserId().value(),
                applicant.getFullName().value(),
                application.tradeName().value(),
                applicant.getNationalIdNumber().value(),
                applicant.getEmailAddress().value(),
                applicant.getPhoneNumber().value(),
                application.licenseType().code(),
                application.commodity().code(),
                application.tradeBankAccountNumber().value(),
                application.status()
        );
    }
}
