package com.trade.tradelicense.application.commands.handlers;

import com.trade.tradelicense.domain.aggregates.TradeLicenseApplication;
import com.trade.tradelicense.domain.exceptions.DomainException;
import com.trade.tradelicense.domain.valueobjects.Actor;
import com.trade.tradelicense.domain.valueobjects.ApplicationId;
import com.trade.tradelicense.domain.valueobjects.UserId;
import com.trade.tradelicense.application.commands.SubmitTradeLicenseApplicationCommand;
import com.trade.tradelicense.application.common.CommandHandler;
import com.trade.tradelicense.application.common.Result;
import com.trade.tradelicense.application.common.TradeLicenseApplicationRepositoryPort;
import com.trade.tradelicense.application.validators.SubmitApplicationValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
public class SubmitTradeLicenseApplicationHandler implements CommandHandler<SubmitTradeLicenseApplicationCommand, Result<TradeLicenseApplication>> {
    private final TradeLicenseApplicationRepositoryPort repository;
    private final SubmitApplicationValidator validator;

    public SubmitTradeLicenseApplicationHandler(
            TradeLicenseApplicationRepositoryPort repository,
            SubmitApplicationValidator validator
    ) {
        this.repository = Objects.requireNonNull(repository, "Trade license application repository is required");
        this.validator = Objects.requireNonNull(validator, "Submit validator is required");
    }

    @Override
    @Transactional
    public Result<TradeLicenseApplication> handle(SubmitTradeLicenseApplicationCommand command) {
        validator.validate(command);
        TradeLicenseApplication application = repository.findById(new ApplicationId(command.applicationId()))
                .orElseThrow(() -> new DomainException("Trade license application not found"));
        application.submit(new Actor(new UserId(command.actorId()), command.role()));
        return Result.success("Trade license application submitted", repository.save(application));
    }
}
