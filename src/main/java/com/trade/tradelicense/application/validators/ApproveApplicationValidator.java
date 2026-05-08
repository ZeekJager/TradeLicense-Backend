package com.trade.tradelicense.application.validators;

import com.trade.tradelicense.domain.enums.ApprovalDecision;
import com.trade.tradelicense.application.commands.ApproveTradeLicenseApplicationCommand;
import com.trade.tradelicense.application.common.TradeLicenseApplicationRepositoryPort;
import com.trade.tradelicense.application.common.TradeLicenseRepositoryPort;
import com.trade.tradelicense.application.exceptions.DuplicateApproverIdException;
import com.trade.tradelicense.application.exceptions.DuplicateLicenseIdException;
import com.trade.tradelicense.application.exceptions.DuplicateLicenseNumberException;
import com.trade.tradelicense.application.exceptions.DuplicateTinNumberException;
import com.trade.tradelicense.domain.valueobjects.ApproverId;
import com.trade.tradelicense.domain.valueobjects.LicenseId;
import com.trade.tradelicense.domain.valueobjects.LicenseNumber;
import com.trade.tradelicense.domain.valueobjects.TinNumber;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class ApproveApplicationValidator {
    private final TradeLicenseApplicationRepositoryPort applicationRepository;
    private final TradeLicenseRepositoryPort tradeLicenseRepository;

    public ApproveApplicationValidator(
            TradeLicenseApplicationRepositoryPort applicationRepository,
            TradeLicenseRepositoryPort tradeLicenseRepository
    ) {
        this.applicationRepository = Objects.requireNonNull(applicationRepository, "Trade license application repository is required");
        this.tradeLicenseRepository = Objects.requireNonNull(tradeLicenseRepository, "Trade license repository is required");
    }

    public void validate(ApproveTradeLicenseApplicationCommand command) {
        Objects.requireNonNull(command, "Command is required");
        requireNonNull(command.applicationId(), "Application id is required");
        requireNonNull(command.approverId(), "Approver id is required");
        requireNonNull(command.role(), "Approver role is required");
        requireNonNull(command.decision(), "Approval decision is required");
        requireText(command.comment(), "Approval comment is required");
        validateApproverIdDoesNotExist(new ApproverId(command.approverId()));
        if (command.decision() == ApprovalDecision.APPROVE) {
            requireText(command.licenseNumber(), "License number is required when approving an application");
            requireText(command.tinNumber(), "TIN number is required when approving an application");
            requireText(command.licenseTypeToIssue(), "License type to issue is required when approving an application");
            validateLicenseNumberDoesNotExist(new LicenseNumber(command.licenseNumber()));
            validateTinNumberDoesNotExist(new TinNumber(command.tinNumber()));
        }
    }

    public void validateApproverIdDoesNotExist(ApproverId approverId) {
        if (applicationRepository.existsByApproverId(approverId)) {
            throw new DuplicateApproverIdException(approverId.value());
        }
    }

    public void validateLicenseIdDoesNotExist(LicenseId licenseId) {
        if (tradeLicenseRepository.existsByLicenseId(licenseId)) {
            throw new DuplicateLicenseIdException(licenseId.value());
        }
    }

    public void validateLicenseNumberDoesNotExist(LicenseNumber licenseNumber) {
        if (tradeLicenseRepository.existsByLicenseNumber(licenseNumber)) {
            throw new DuplicateLicenseNumberException(licenseNumber.value());
        }
    }

    public void validateTinNumberDoesNotExist(TinNumber tinNumber) {
        if (tradeLicenseRepository.existsByTinNumber(tinNumber)) {
            throw new DuplicateTinNumberException(tinNumber.value());
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
