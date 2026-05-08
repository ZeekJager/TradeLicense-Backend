package com.trade.tradelicense.infrastructure.services;

import com.trade.tradelicense.domain.valueobjects.LicenseNumber;
import org.springframework.stereotype.Service;

import java.time.Year;
import java.util.UUID;

@Service
public class LicenseNumberGeneratorService {
    public LicenseNumber generate() {
        String suffix = UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase();
        return new LicenseNumber("TL-" + Year.now().getValue() + "-" + suffix);
    }
}
