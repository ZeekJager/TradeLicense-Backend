package com.trade.tradelicense.infrastructure.services;

import org.springframework.stereotype.Service;

@Service
public class FileStorageService {
    public String store(String fileName) {
        return "uploads/" + fileName;
    }
}
