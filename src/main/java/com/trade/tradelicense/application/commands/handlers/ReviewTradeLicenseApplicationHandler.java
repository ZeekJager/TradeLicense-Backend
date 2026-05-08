package com.trade.tradelicense.application.commands.handlers;

import com.trade.tradelicense.domain.aggregates.TradeLicenseApplication;
import com.trade.tradelicense.domain.exceptions.DomainException;
import com.trade.tradelicense.domain.valueobjects.Actor;
import com.trade.tradelicense.domain.valueobjects.ApplicationId;
import com.trade.tradelicense.domain.valueobjects.UserId;
import com.trade.tradelicense.application.commands.ReviewTradeLicenseApplicationCommand;
import com.trade.tradelicense.application.common.CommandHandler;
import com.trade.tradelicense.application.common.Result;
import com.trade.tradelicense.application.common.TradeLicenseApplicationRepositoryPort;
import com.trade.tradelicense.application.validators.ReviewApplicationValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
public class ReviewTradeLicenseApplicationHandler implements CommandHandler<ReviewTradeLicenseApplicationCommand, Result<TradeLicenseApplication>> {
    private final TradeLicenseApplicationRepositoryPort repository;
    private final ReviewApplicationValidator validator;

    public ReviewTradeLicenseApplicationHandler(
            TradeLicenseApplicationRepositoryPort repository,
            ReviewApplicationValidator validator
    ) {
        this.repository = Objects.requireNonNull(repository, "Trade license application repository is required");
        this.validator = Objects.requireNonNull(validator, "Review validator is required");
    }

    @Override
    @Transactional
    public Result<TradeLicenseApplication> handle(ReviewTradeLicenseApplicationCommand command) {
        validator.validate(command);
        TradeLicenseApplication application = repository.findById(new ApplicationId(command.applicationId()))
                .orElseThrow(() -> new DomainException("Trade license application not found"));
        application.review(new Actor(new UserId(command.reviewerId()), command.role()), command.decision(), command.comment());
        return Result.success("Trade license application reviewed", repository.save(application));
    }
}
