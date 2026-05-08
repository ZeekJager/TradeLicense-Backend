package com.trade.tradelicense.application.commands.handlers;

import com.trade.tradelicense.domain.aggregates.TradeLicense;
import com.trade.tradelicense.domain.aggregates.TradeLicenseApplication;
import com.trade.tradelicense.domain.enums.ApprovalDecision;
import com.trade.tradelicense.domain.exceptions.DomainException;
import com.trade.tradelicense.domain.factories.TradeLicenseFactory;
import com.trade.tradelicense.domain.valueobjects.Actor;
import com.trade.tradelicense.domain.valueobjects.ApplicationId;
import com.trade.tradelicense.domain.valueobjects.LicenseNumber;
import com.trade.tradelicense.domain.valueobjects.LicensePeriod;
import com.trade.tradelicense.domain.valueobjects.TinNumber;
import com.trade.tradelicense.domain.valueobjects.TradeLicenseType;
import com.trade.tradelicense.domain.valueobjects.UserId;
import com.trade.tradelicense.application.commands.ApproveTradeLicenseApplicationCommand;
import com.trade.tradelicense.application.common.CommandHandler;
import com.trade.tradelicense.application.common.Result;
import com.trade.tradelicense.application.common.TradeLicenseApplicationRepositoryPort;
import com.trade.tradelicense.application.common.TradeLicenseRepositoryPort;
import com.trade.tradelicense.application.validators.ApproveApplicationValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Objects;

@Service
public class ApproveTradeLicenseApplicationHandler implements CommandHandler<ApproveTradeLicenseApplicationCommand, Result<TradeLicenseApplication>> {
    private final TradeLicenseApplicationRepositoryPort applicationRepository;
    private final TradeLicenseRepositoryPort tradeLicenseRepository;
    private final TradeLicenseFactory tradeLicenseFactory;
    private final ApproveApplicationValidator validator;

    public ApproveTradeLicenseApplicationHandler(
            TradeLicenseApplicationRepositoryPort applicationRepository,
            TradeLicenseRepositoryPort tradeLicenseRepository,
            TradeLicenseFactory tradeLicenseFactory,
            ApproveApplicationValidator validator
    ) {
        this.applicationRepository = Objects.requireNonNull(applicationRepository, "Trade license application repository is required");
        this.tradeLicenseRepository = Objects.requireNonNull(tradeLicenseRepository, "Trade license repository is required");
        this.tradeLicenseFactory = Objects.requireNonNull(tradeLicenseFactory, "Trade license factory is required");
        this.validator = Objects.requireNonNull(validator, "Approve validator is required");
    }

    @Override
    @Transactional
    public Result<TradeLicenseApplication> handle(ApproveTradeLicenseApplicationCommand command) {
        validator.validate(command);
        TradeLicenseApplication application = applicationRepository.findById(new ApplicationId(command.applicationId()))
                .orElseThrow(() -> new DomainException("Trade license application not found"));

        application.approve(new Actor(new UserId(command.approverId()), command.role()), command.decision(), command.comment());
        if (command.decision() == ApprovalDecision.APPROVE) {
            LocalDate issuedOn = LocalDate.now();
            TradeLicense tradeLicense = tradeLicenseFactory.issueLicense(
                    application,
                    new LicenseNumber(command.licenseNumber()),
                    new TinNumber(command.tinNumber()),
                    new TradeLicenseType(command.licenseTypeToIssue(), command.licenseTypeToIssue()),
                    new LicensePeriod(issuedOn, issuedOn.plusYears(1))
            );
            validator.validateLicenseIdDoesNotExist(tradeLicense.id());
            tradeLicenseRepository.save(tradeLicense);
        }

        return Result.success("Trade license application approval processed", applicationRepository.save(application));
    }
}
