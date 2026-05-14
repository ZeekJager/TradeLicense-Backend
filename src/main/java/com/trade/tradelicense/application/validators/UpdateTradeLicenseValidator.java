package com.trade.tradelicense.application.validators;

import com.trade.tradelicense.application.commands.UpdateTradeLicenseCommand;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class UpdateTradeLicenseValidator {
    public void validate(UpdateTradeLicenseCommand command) {
        Objects.requireNonNull(command, "Command is required");
        requireNonNull(command.licenseId(), "License id is required");
        requireNonNull(command.actorId(), "Actor id is required");
        requireNonNull(command.role(), "Actor role is required");
        requireText(command.tradeLicenseType(), "Trade license type is required");
        requireText(command.commodity(), "Commodity is required");
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
