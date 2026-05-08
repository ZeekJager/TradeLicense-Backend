package com.trade.tradelicense.application.validators;

import com.trade.tradelicense.application.commands.SettleTradeLicenseApplicationPaymentCommand;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class SettlePaymentValidator {
    public void validate(SettleTradeLicenseApplicationPaymentCommand command) {
        Objects.requireNonNull(command, "Command is required");
        if (command.applicationId() == null) {
            throw new IllegalArgumentException("Application id is required");
        }
    }
}
