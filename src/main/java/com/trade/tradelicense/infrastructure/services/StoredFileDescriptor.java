package com.trade.tradelicense.infrastructure.services;

public record StoredFileDescriptor(
        String originalFileName,
        String storedFileName,
        String contentType,
        long size
) {
}
