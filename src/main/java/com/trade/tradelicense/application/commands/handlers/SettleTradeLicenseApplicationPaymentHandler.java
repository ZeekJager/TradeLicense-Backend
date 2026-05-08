package com.trade.tradelicense.application.commands.handlers;

import com.trade.tradelicense.application.commands.SettleTradeLicenseApplicationPaymentCommand;
import com.trade.tradelicense.application.common.CommandHandler;
import com.trade.tradelicense.application.common.Result;
import com.trade.tradelicense.application.common.TradeLicenseApplicationRepositoryPort;
import com.trade.tradelicense.application.validators.SettlePaymentValidator;
import com.trade.tradelicense.domain.aggregates.TradeLicenseApplication;
import com.trade.tradelicense.domain.exceptions.DomainException;
import com.trade.tradelicense.domain.valueobjects.ApplicationId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
public class SettleTradeLicenseApplicationPaymentHandler implements CommandHandler<SettleTradeLicenseApplicationPaymentCommand, Result<TradeLicenseApplication>> {
    private final TradeLicenseApplicationRepositoryPort repository;
    private final SettlePaymentValidator validator;

    public SettleTradeLicenseApplicationPaymentHandler(
            TradeLicenseApplicationRepositoryPort repository,
            SettlePaymentValidator validator
    ) {
        this.repository = Objects.requireNonNull(repository, "Trade license application repository is required");
        this.validator = Objects.requireNonNull(validator, "Settle payment validator is required");
    }

    @Override
    @Transactional
    public Result<TradeLicenseApplication> handle(SettleTradeLicenseApplicationPaymentCommand command) {
        validator.validate(command);
        TradeLicenseApplication application = repository.findById(new ApplicationId(command.applicationId()))
                .orElseThrow(() -> new DomainException("Trade license application not found"));
        application.settlePayment();
        return Result.success("Payment settled", repository.save(application));
    }
}
