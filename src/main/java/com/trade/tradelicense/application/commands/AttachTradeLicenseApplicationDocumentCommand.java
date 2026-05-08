package com.trade.tradelicense.application.commands;

import java.util.UUID;

public record AttachTradeLicenseApplicationDocumentCommand(
        UUID applicationId,
        String documentType,
        String documentReference
) {
}
