package com.trade.tradelicense.application.validators;

import com.trade.tradelicense.application.commands.ReviewTradeLicenseApplicationCommand;
import com.trade.tradelicense.application.common.TradeLicenseApplicationRepositoryPort;
import com.trade.tradelicense.application.exceptions.DuplicateReviewerIdException;
import com.trade.tradelicense.domain.valueobjects.ReviewerId;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class ReviewApplicationValidator {
    private final TradeLicenseApplicationRepositoryPort repository;

    public ReviewApplicationValidator(TradeLicenseApplicationRepositoryPort repository) {
        this.repository = Objects.requireNonNull(repository, "Trade license application repository is required");
    }

    public void validate(ReviewTradeLicenseApplicationCommand command) {
        Objects.requireNonNull(command, "Command is required");
        requireNonNull(command.applicationId(), "Application id is required");
        requireNonNull(command.reviewerId(), "Reviewer id is required");
        requireNonNull(command.role(), "Reviewer role is required");
        requireNonNull(command.decision(), "Review decision is required");
        requireText(command.comment(), "Review comment is required");
        validateReviewerIdDoesNotExist(new ReviewerId(command.reviewerId()));
    }

    public void validateReviewerIdDoesNotExist(ReviewerId reviewerId) {
        if (repository.existsByReviewerId(reviewerId)) {
            throw new DuplicateReviewerIdException(reviewerId.value());
        }
    }

    private void requireNonNull(Object value, String message) {
        if (value == null) {
            throw new IllegalArgumentException(message);
        }
    }

    private void requireText(String value, String message) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
    }
}
