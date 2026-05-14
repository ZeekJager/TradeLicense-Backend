package com.trade.tradelicense.application.commands.handlers;

import com.trade.tradelicense.application.commands.RenewTradeLicenseCommand;
import com.trade.tradelicense.application.common.CommandHandler;
import com.trade.tradelicense.application.common.Result;
import com.trade.tradelicense.application.common.TradeLicenseRepositoryPort;
import com.trade.tradelicense.domain.aggregates.TradeLicense;
import com.trade.tradelicense.domain.exceptions.DomainException;
import com.trade.tradelicense.domain.valueobjects.Actor;
import com.trade.tradelicense.domain.valueobjects.LicenseId;
import com.trade.tradelicense.domain.valueobjects.LicensePeriod;
import com.trade.tradelicense.domain.valueobjects.UserId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
public class RenewTradeLicenseHandler implements CommandHandler<RenewTradeLicenseCommand, Result<TradeLicense>> {
    private final TradeLicenseRepositoryPort tradeLicenseRepository;

    public RenewTradeLicenseHandler(TradeLicenseRepositoryPort tradeLicenseRepository) {
        this.tradeLicenseRepository = Objects.requireNonNull(tradeLicenseRepository, "Trade license repository is required");
    }

    @Override
    @Transactional
    public Result<TradeLicense> handle(RenewTradeLicenseCommand command) {
        Objects.requireNonNull(command, "Command is required");
        Objects.requireNonNull(command.licenseId(), "License id is required");
        Objects.requireNonNull(command.actorId(), "Actor id is required");
        Objects.requireNonNull(command.role(), "Actor role is required");
        TradeLicense tradeLicense = tradeLicenseRepository.findById(new LicenseId(command.licenseId()))
                .orElseThrow(() -> new DomainException("Trade license not found"));
        var currentPeriod = tradeLicense.licensePeriod();
        var renewalPeriod = new LicensePeriod(currentPeriod.endsOn().plusDays(1), currentPeriod.endsOn().plusYears(1));
        tradeLicense.renew(new Actor(new UserId(command.actorId()), command.role()), renewalPeriod);
        return Result.success("Trade license renewed", tradeLicenseRepository.save(tradeLicense));
    }
}
