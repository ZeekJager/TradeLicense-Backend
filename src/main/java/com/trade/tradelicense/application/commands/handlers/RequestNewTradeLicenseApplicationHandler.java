package com.trade.tradelicense.application.commands.handlers;

import com.trade.tradelicense.domain.aggregates.TradeLicenseApplication;
import com.trade.tradelicense.domain.entities.User;
import com.trade.tradelicense.domain.enums.UserRole;
import com.trade.tradelicense.domain.factories.TradeLicenseApplicationFactory;
import com.trade.tradelicense.domain.valueobjects.Commodity;
import com.trade.tradelicense.domain.valueobjects.EmailAddress;
import com.trade.tradelicense.domain.valueobjects.FullName;
import com.trade.tradelicense.domain.valueobjects.Money;
import com.trade.tradelicense.domain.valueobjects.NationalIdNumber;
import com.trade.tradelicense.domain.valueobjects.PaymentReference;
import com.trade.tradelicense.domain.valueobjects.PhoneNumber;
import com.trade.tradelicense.domain.valueobjects.TradeLicenseType;
import com.trade.tradelicense.domain.valueobjects.UserId;
import com.trade.tradelicense.application.commands.RequestNewTradeLicenseApplicationCommand;
import com.trade.tradelicense.application.common.CommandHandler;
import com.trade.tradelicense.application.common.Result;
import com.trade.tradelicense.application.common.TradeLicenseApplicationRepositoryPort;
import com.trade.tradelicense.application.validators.RequestNewTradeLicenseApplicationValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Objects;

@Service
public class RequestNewTradeLicenseApplicationHandler implements CommandHandler<RequestNewTradeLicenseApplicationCommand, Result<TradeLicenseApplication>> {
    private final TradeLicenseApplicationFactory factory;
    private final TradeLicenseApplicationRepositoryPort repository;
    private final RequestNewTradeLicenseApplicationValidator validator;

    public RequestNewTradeLicenseApplicationHandler(
            TradeLicenseApplicationFactory factory,
            TradeLicenseApplicationRepositoryPort repository,
            RequestNewTradeLicenseApplicationValidator validator
    ) {
        this.factory = Objects.requireNonNull(factory, "Trade license application factory is required");
        this.repository = Objects.requireNonNull(repository, "Trade license application repository is required");
        this.validator = Objects.requireNonNull(validator, "Request validator is required");
    }

    @Override
    @Transactional
    public Result<TradeLicenseApplication> handle(RequestNewTradeLicenseApplicationCommand command) {
        validator.validate(command);

        User applicant = new User(
                new UserId(command.applicantId()),
                UserRole.CUSTOMER,
                new FullName(command.fullName()),
                new NationalIdNumber(command.nationalIdNumber()),
                new EmailAddress(command.email()),
                new PhoneNumber(command.phoneNumber())
        );

        TradeLicenseApplication application = factory.createDraftApplication(
                applicant,
                new TradeLicenseType(command.tradeLicenseType(), command.tradeLicenseType()),
                new Commodity(command.commodity(), command.commodity()),
                new Money(BigDecimal.ZERO, "USD"),
                new PaymentReference("PENDING_PAYMENT")
        );
        validator.validateApplicationIdDoesNotExist(application.id());
        validator.validatePaymentSettlementIdDoesNotExist(application.paymentSettlement().id());

        return Result.success("Trade license application draft created", repository.save(application));
    }
}
