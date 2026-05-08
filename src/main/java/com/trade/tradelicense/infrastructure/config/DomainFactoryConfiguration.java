package com.trade.tradelicense.infrastructure.config;

import com.trade.tradelicense.domain.factories.RequiredDocumentPackageFactory;
import com.trade.tradelicense.domain.factories.TradeLicenseApplicationFactory;
import com.trade.tradelicense.domain.factories.TradeLicenseFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DomainFactoryConfiguration {
    @Bean
    public TradeLicenseApplicationFactory tradeLicenseApplicationFactory() {
        return new TradeLicenseApplicationFactory();
    }

    @Bean
    public RequiredDocumentPackageFactory requiredDocumentPackageFactory() {
        return new RequiredDocumentPackageFactory();
    }

    @Bean
    public TradeLicenseFactory tradeLicenseFactory() {
        return new TradeLicenseFactory();
    }
}
