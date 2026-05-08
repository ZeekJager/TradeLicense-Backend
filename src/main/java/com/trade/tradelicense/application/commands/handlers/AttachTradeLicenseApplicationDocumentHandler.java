package com.trade.tradelicense.application.commands.handlers;

import com.trade.tradelicense.application.commands.AttachTradeLicenseApplicationDocumentCommand;
import com.trade.tradelicense.application.common.CommandHandler;
import com.trade.tradelicense.application.common.Result;
import com.trade.tradelicense.application.common.TradeLicenseApplicationRepositoryPort;
import com.trade.tradelicense.application.validators.AttachDocumentValidator;
import com.trade.tradelicense.domain.aggregates.TradeLicenseApplication;
import com.trade.tradelicense.domain.exceptions.DomainException;
import com.trade.tradelicense.domain.valueobjects.ApplicationId;
import com.trade.tradelicense.domain.valueobjects.DocumentReference;
import com.trade.tradelicense.domain.valueobjects.DocumentType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
public class AttachTradeLicenseApplicationDocumentHandler implements CommandHandler<AttachTradeLicenseApplicationDocumentCommand, Result<TradeLicenseApplication>> {
    private final TradeLicenseApplicationRepositoryPort repository;
    private final AttachDocumentValidator validator;

    public AttachTradeLicenseApplicationDocumentHandler(
            TradeLicenseApplicationRepositoryPort repository,
            AttachDocumentValidator validator
    ) {
        this.repository = Objects.requireNonNull(repository, "Trade license application repository is required");
        this.validator = Objects.requireNonNull(validator, "Attach document validator is required");
    }

    @Override
    @Transactional
    public Result<TradeLicenseApplication> handle(AttachTradeLicenseApplicationDocumentCommand command) {
        validator.validate(command);
        TradeLicenseApplication application = repository.findById(new ApplicationId(command.applicationId()))
                .orElseThrow(() -> new DomainException("Trade license application not found"));
        application.attachDocument(new DocumentType(command.documentType()), new DocumentReference(command.documentReference()));
        var documents = application.documentPackage().documents();
        validator.validateDocumentIdDoesNotExist(documents.get(documents.size() - 1).id());
        return Result.success("Document attached", repository.save(application));
    }
}
