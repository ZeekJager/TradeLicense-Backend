package com.trade.tradelicense.application.commands.handlers;

import com.trade.tradelicense.application.commands.ResubmitTradeLicenseApplicationCommand;
import com.trade.tradelicense.application.common.CommandHandler;
import com.trade.tradelicense.application.common.Result;
import com.trade.tradelicense.application.common.TradeLicenseApplicationRepositoryPort;
import com.trade.tradelicense.application.validators.SubmitApplicationValidator;
import com.trade.tradelicense.domain.aggregates.TradeLicenseApplication;
import com.trade.tradelicense.domain.exceptions.DomainException;
import com.trade.tradelicense.domain.valueobjects.Actor;
import com.trade.tradelicense.domain.valueobjects.ApplicationId;
import com.trade.tradelicense.domain.valueobjects.UserId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
public class ResubmitTradeLicenseApplicationHandler implements CommandHandler<ResubmitTradeLicenseApplicationCommand, Result<TradeLicenseApplication>> {
    private final TradeLicenseApplicationRepositoryPort repository;
    private final SubmitApplicationValidator validator;

    public ResubmitTradeLicenseApplicationHandler(
            TradeLicenseApplicationRepositoryPort repository,
            SubmitApplicationValidator validator
    ) {
        this.repository = Objects.requireNonNull(repository, "Trade license application repository is required");
        this.validator = Objects.requireNonNull(validator, "Resubmit validator is required");
    }

    @Override
    @Transactional
    public Result<TradeLicenseApplication> handle(ResubmitTradeLicenseApplicationCommand command) {
        validator.validate(command);
        TradeLicenseApplication application = repository.findById(new ApplicationId(command.applicationId()))
                .orElseThrow(() -> new DomainException("Trade license application not found"));
        application.resubmitAfterAdjustment(new Actor(new UserId(command.actorId()), command.role()));
        return Result.success("Trade license application resubmitted for review", repository.save(application));
    }
}
