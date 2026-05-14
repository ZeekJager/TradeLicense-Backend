package com.trade.tradelicense.application.queries.handlers;

import com.trade.tradelicense.application.common.QueryHandler;
import com.trade.tradelicense.application.common.TradeLicenseRepositoryPort;
import com.trade.tradelicense.application.queries.GetTradeLicenseByApplicationIdQuery;
import com.trade.tradelicense.domain.aggregates.TradeLicense;
import com.trade.tradelicense.domain.exceptions.DomainException;
import com.trade.tradelicense.domain.valueobjects.ApplicationId;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class GetTradeLicenseByApplicationIdHandler implements QueryHandler<GetTradeLicenseByApplicationIdQuery, TradeLicense> {
    private final TradeLicenseRepositoryPort tradeLicenseRepository;

    public GetTradeLicenseByApplicationIdHandler(TradeLicenseRepositoryPort tradeLicenseRepository) {
        this.tradeLicenseRepository = Objects.requireNonNull(tradeLicenseRepository, "Trade license repository is required");
    }

    @Override
    public TradeLicense handle(GetTradeLicenseByApplicationIdQuery query) {
        Objects.requireNonNull(query, "Query is required");
        Objects.requireNonNull(query.applicationId(), "Application id is required");
        return tradeLicenseRepository.findBySourceApplicationId(new ApplicationId(query.applicationId()))
                .orElseThrow(() -> new DomainException("Trade license not found for application"));
    }
}
