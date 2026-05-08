package com.trade.tradelicense.application.validators;

import com.trade.tradelicense.application.commands.AttachTradeLicenseApplicationDocumentCommand;
import com.trade.tradelicense.application.common.TradeLicenseApplicationRepositoryPort;
import com.trade.tradelicense.application.exceptions.DuplicateDocumentIdException;
import com.trade.tradelicense.domain.valueobjects.DocumentId;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class AttachDocumentValidator {
    private final TradeLicenseApplicationRepositoryPort repository;

    public AttachDocumentValidator(TradeLicenseApplicationRepositoryPort repository) {
        this.repository = Objects.requireNonNull(repository, "Trade license application repository is required");
    }

    public void validate(AttachTradeLicenseApplicationDocumentCommand command) {
        Objects.requireNonNull(command, "Command is required");
        if (command.applicationId() == null) {
            throw new IllegalArgumentException("Application id is required");
        }
        requireText(command.documentType(), "Document type is required");
        requireText(command.documentReference(), "Document reference is required");
    }

    public void validateDocumentIdDoesNotExist(DocumentId documentId) {
        if (repository.existsByDocumentId(documentId)) {
            throw new DuplicateDocumentIdException(documentId.value());
        }
    }

    private void requireText(String value, String message) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
    }
}
