package com.trade.tradelicense.application.validators;

import com.trade.tradelicense.application.commands.CancelTradeLicenseApplicationCommand;
import com.trade.tradelicense.application.commands.SubmitTradeLicenseApplicationCommand;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class SubmitApplicationValidator {
    public void validate(SubmitTradeLicenseApplicationCommand command) {
        Objects.requireNonNull(command, "Command is required");
        requireNonNull(command.applicationId(), "Application id is required");
        requireNonNull(command.actorId(), "Actor id is required");
        requireNonNull(command.role(), "Actor role is required");
    }

    public void validate(CancelTradeLicenseApplicationCommand command) {
        Objects.requireNonNull(command, "Command is required");
        requireNonNull(command.applicationId(), "Application id is required");
        requireNonNull(command.actorId(), "Actor id is required");
        requireNonNull(command.role(), "Actor role is required");
    }

    private void requireNonNull(Object value, String message) {
        if (value == null) {
            throw new IllegalArgumentException(message);
        }
    }
}
