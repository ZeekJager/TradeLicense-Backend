package com.trade.tradelicense.application.queries.handlers;

import com.trade.tradelicense.domain.aggregates.TradeLicenseApplication;
import com.trade.tradelicense.domain.exceptions.DomainException;
import com.trade.tradelicense.domain.valueobjects.ApplicationId;
import com.trade.tradelicense.application.common.QueryHandler;
import com.trade.tradelicense.application.common.TradeLicenseApplicationRepositoryPort;
import com.trade.tradelicense.application.queries.GetTradeLicenseApplicationByIdQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
public class GetTradeLicenseApplicationByIdHandler implements QueryHandler<GetTradeLicenseApplicationByIdQuery, TradeLicenseApplication> {
    private final TradeLicenseApplicationRepositoryPort repository;

    public GetTradeLicenseApplicationByIdHandler(TradeLicenseApplicationRepositoryPort repository) {
        this.repository = Objects.requireNonNull(repository, "Trade license application repository is required");
    }

    @Override
    @Transactional(readOnly = true)
    public TradeLicenseApplication handle(GetTradeLicenseApplicationByIdQuery query) {
        Objects.requireNonNull(query, "Query is required");
        if (query.applicationId() == null) {
            throw new IllegalArgumentException("Application id is required");
        }
        return repository.findById(new ApplicationId(query.applicationId()))
                .orElseThrow(() -> new DomainException("Trade license application not found"));
    }
}
