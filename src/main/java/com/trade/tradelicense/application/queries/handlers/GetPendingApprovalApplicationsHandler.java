package com.trade.tradelicense.application.queries.handlers;

import com.trade.tradelicense.domain.aggregates.TradeLicenseApplication;
import com.trade.tradelicense.domain.enums.ApplicationStatus;
import com.trade.tradelicense.application.common.QueryHandler;
import com.trade.tradelicense.application.common.TradeLicenseApplicationRepositoryPort;
import com.trade.tradelicense.application.queries.GetPendingApprovalApplicationsQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class GetPendingApprovalApplicationsHandler implements QueryHandler<GetPendingApprovalApplicationsQuery, List<TradeLicenseApplication>> {
    private final TradeLicenseApplicationRepositoryPort repository;

    public GetPendingApprovalApplicationsHandler(TradeLicenseApplicationRepositoryPort repository) {
        this.repository = Objects.requireNonNull(repository, "Trade license application repository is required");
    }

    @Override
    @Transactional(readOnly = true)
    public List<TradeLicenseApplication> handle(GetPendingApprovalApplicationsQuery query) {
        Objects.requireNonNull(query, "Query is required");
        return repository.findByStatus(ApplicationStatus.PENDING_APPROVAL);
    }
}
