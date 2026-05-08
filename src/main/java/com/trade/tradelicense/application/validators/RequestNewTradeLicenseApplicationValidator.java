package com.trade.tradelicense.application.validators;

import com.trade.tradelicense.application.commands.RequestNewTradeLicenseApplicationCommand;
import com.trade.tradelicense.application.common.TradeLicenseApplicationRepositoryPort;
import com.trade.tradelicense.application.exceptions.DuplicateApplicationIdException;
import com.trade.tradelicense.application.exceptions.DuplicateEmailAddressException;
import com.trade.tradelicense.application.exceptions.DuplicateFullNameException;
import com.trade.tradelicense.application.exceptions.DuplicateNationalIdNumberException;
import com.trade.tradelicense.application.exceptions.DuplicatePaymentSettlementIdException;
import com.trade.tradelicense.application.exceptions.DuplicatePhoneNumberException;
import com.trade.tradelicense.application.exceptions.DuplicateUserIdException;
import com.trade.tradelicense.domain.valueobjects.ApplicationId;
import com.trade.tradelicense.domain.valueobjects.EmailAddress;
import com.trade.tradelicense.domain.valueobjects.FullName;
import com.trade.tradelicense.domain.valueobjects.NationalIdNumber;
import com.trade.tradelicense.domain.valueobjects.PaymentSettlementId;
import com.trade.tradelicense.domain.valueobjects.PhoneNumber;
import com.trade.tradelicense.domain.valueobjects.UserId;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class RequestNewTradeLicenseApplicationValidator {
    private final TradeLicenseApplicationRepositoryPort repository;

    public RequestNewTradeLicenseApplicationValidator(TradeLicenseApplicationRepositoryPort repository) {
        this.repository = Objects.requireNonNull(repository, "Trade license application repository is required");
    }

    public void validate(RequestNewTradeLicenseApplicationCommand command) {
        Objects.requireNonNull(command, "Command is required");
        requireNonNull(command.applicantId(), "Applicant id is required");
        requireText(command.fullName(), "Full name is required");
        requireText(command.nationalIdNumber(), "National id number is required");
        requireText(command.email(), "Email is required");
        requireText(command.phoneNumber(), "Phone number is required");
        requireText(command.tradeLicenseType(), "Trade license type is required");
        requireText(command.commodity(), "Commodity is required");
        validateUserIdDoesNotExist(new UserId(command.applicantId()));
        validateFullNameDoesNotExist(new FullName(command.fullName()));
        validateNationalIdNumberDoesNotExist(new NationalIdNumber(command.nationalIdNumber()));
        validateEmailAddressDoesNotExist(new EmailAddress(command.email()));
        validatePhoneNumberDoesNotExist(new PhoneNumber(command.phoneNumber()));
    }

    public void validateApplicationIdDoesNotExist(ApplicationId applicationId) {
        if (repository.existsByApplicationId(applicationId)) {
            throw new DuplicateApplicationIdException(applicationId.value());
        }
    }

    public void validateUserIdDoesNotExist(UserId userId) {
        if (repository.existsByUserId(userId)) {
            throw new DuplicateUserIdException(userId.value());
        }
    }

    public void validateFullNameDoesNotExist(FullName fullName) {
        if (repository.existsByFullName(fullName)) {
            throw new DuplicateFullNameException(fullName.value());
        }
    }

    public void validateNationalIdNumberDoesNotExist(NationalIdNumber nationalIdNumber) {
        if (repository.existsByNationalIdNumber(nationalIdNumber)) {
            throw new DuplicateNationalIdNumberException(nationalIdNumber.value());
        }
    }

    public void validateEmailAddressDoesNotExist(EmailAddress emailAddress) {
        if (repository.existsByEmailAddress(emailAddress)) {
            throw new DuplicateEmailAddressException(emailAddress.value());
        }
    }

    public void validatePhoneNumberDoesNotExist(PhoneNumber phoneNumber) {
        if (repository.existsByPhoneNumber(phoneNumber)) {
            throw new DuplicatePhoneNumberException(phoneNumber.value());
        }
    }

    public void validatePaymentSettlementIdDoesNotExist(PaymentSettlementId paymentSettlementId) {
        if (repository.existsByPaymentSettlementId(paymentSettlementId)) {
            throw new DuplicatePaymentSettlementIdException(paymentSettlementId.value());
        }
    }

    private void requireNonNull(Object value, String message) {
        if (value == null) {
            throw new IllegalArgumentException(message);
        }
    }

    private void requireText(String value, String message) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
    }
}
