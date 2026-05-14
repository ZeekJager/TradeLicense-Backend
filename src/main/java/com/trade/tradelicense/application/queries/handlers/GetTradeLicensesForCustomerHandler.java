package com.trade.tradelicense.application.queries.handlers;

import com.trade.tradelicense.application.common.QueryHandler;
import com.trade.tradelicense.application.common.TradeLicenseRepositoryPort;
import com.trade.tradelicense.application.queries.GetTradeLicensesForCustomerQuery;
import com.trade.tradelicense.domain.aggregates.TradeLicense;
import com.trade.tradelicense.domain.valueobjects.UserId;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class GetTradeLicensesForCustomerHandler implements QueryHandler<GetTradeLicensesForCustomerQuery, List<TradeLicense>> {
    private final TradeLicenseRepositoryPort tradeLicenseRepository;

    public GetTradeLicensesForCustomerHandler(TradeLicenseRepositoryPort tradeLicenseRepository) {
        this.tradeLicenseRepository = Objects.requireNonNull(tradeLicenseRepository, "Trade license repository is required");
    }

    @Override
    public List<TradeLicense> handle(GetTradeLicensesForCustomerQuery query) {
        Objects.requireNonNull(query, "Query is required");
        Objects.requireNonNull(query.customerId(), "Customer id is required");
        return tradeLicenseRepository.findByLicenseHolderId(new UserId(query.customerId()));
    }
}
