package com.trade.tradelicense.application.commands.handlers;

import com.trade.tradelicense.application.commands.UpdateTradeLicenseCommand;
import com.trade.tradelicense.application.common.CommandHandler;
import com.trade.tradelicense.application.common.Result;
import com.trade.tradelicense.application.common.TradeLicenseRepositoryPort;
import com.trade.tradelicense.application.validators.UpdateTradeLicenseValidator;
import com.trade.tradelicense.domain.aggregates.TradeLicense;
import com.trade.tradelicense.domain.exceptions.DomainException;
import com.trade.tradelicense.domain.valueobjects.Actor;
import com.trade.tradelicense.domain.valueobjects.Commodity;
import com.trade.tradelicense.domain.valueobjects.LicenseId;
import com.trade.tradelicense.domain.valueobjects.TradeLicenseType;
import com.trade.tradelicense.domain.valueobjects.UserId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
public class UpdateTradeLicenseHandler implements CommandHandler<UpdateTradeLicenseCommand, Result<TradeLicense>> {
    private final TradeLicenseRepositoryPort tradeLicenseRepository;
    private final UpdateTradeLicenseValidator validator;

    public UpdateTradeLicenseHandler(
            TradeLicenseRepositoryPort tradeLicenseRepository,
            UpdateTradeLicenseValidator validator
    ) {
        this.tradeLicenseRepository = Objects.requireNonNull(tradeLicenseRepository, "Trade license repository is required");
        this.validator = Objects.requireNonNull(validator, "Update trade license validator is required");
    }

    @Override
    @Transactional
    public Result<TradeLicense> handle(UpdateTradeLicenseCommand command) {
        validator.validate(command);
        TradeLicense tradeLicense = tradeLicenseRepository.findById(new LicenseId(command.licenseId()))
                .orElseThrow(() -> new DomainException("Trade license not found"));
        tradeLicense.updateDetails(
                new Actor(new UserId(command.actorId()), command.role()),
                new TradeLicenseType(command.tradeLicenseType(), command.tradeLicenseType()),
                new Commodity(command.commodity(), command.commodity())
        );
        return Result.success("Trade license updated", tradeLicenseRepository.save(tradeLicense));
    }
}
