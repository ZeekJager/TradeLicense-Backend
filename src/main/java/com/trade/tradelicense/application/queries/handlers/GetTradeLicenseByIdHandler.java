package com.trade.tradelicense.application.queries.handlers;

import com.trade.tradelicense.application.common.QueryHandler;
import com.trade.tradelicense.application.common.TradeLicenseRepositoryPort;
import com.trade.tradelicense.application.queries.GetTradeLicenseByIdQuery;
import com.trade.tradelicense.domain.aggregates.TradeLicense;
import com.trade.tradelicense.domain.exceptions.DomainException;
import com.trade.tradelicense.domain.valueobjects.LicenseId;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class GetTradeLicenseByIdHandler implements QueryHandler<GetTradeLicenseByIdQuery, TradeLicense> {
    private final TradeLicenseRepositoryPort tradeLicenseRepository;

    public GetTradeLicenseByIdHandler(TradeLicenseRepositoryPort tradeLicenseRepository) {
        this.tradeLicenseRepository = Objects.requireNonNull(tradeLicenseRepository, "Trade license repository is required");
    }

    @Override
    public TradeLicense handle(GetTradeLicenseByIdQuery query) {
        Objects.requireNonNull(query, "Query is required");
        Objects.requireNonNull(query.licenseId(), "License id is required");
        return tradeLicenseRepository.findById(new LicenseId(query.licenseId()))
                .orElseThrow(() -> new DomainException("Trade license not found"));
    }
}
