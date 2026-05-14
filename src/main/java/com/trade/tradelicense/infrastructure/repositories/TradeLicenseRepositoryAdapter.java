package com.trade.tradelicense.infrastructure.repositories;

import com.trade.tradelicense.domain.aggregates.TradeLicense;
import com.trade.tradelicense.domain.entities.User;
import com.trade.tradelicense.domain.enums.LicenseStatus;
import com.trade.tradelicense.domain.valueobjects.ApplicationId;
import com.trade.tradelicense.domain.valueobjects.Commodity;
import com.trade.tradelicense.domain.valueobjects.EmailAddress;
import com.trade.tradelicense.domain.valueobjects.FullName;
import com.trade.tradelicense.domain.valueobjects.LicenseId;
import com.trade.tradelicense.domain.valueobjects.LicenseNumber;
import com.trade.tradelicense.domain.valueobjects.LicensePeriod;
import com.trade.tradelicense.domain.valueobjects.NationalIdNumber;
import com.trade.tradelicense.domain.valueobjects.PhoneNumber;
import com.trade.tradelicense.domain.valueobjects.TinNumber;
import com.trade.tradelicense.domain.valueobjects.TradeLicenseType;
import com.trade.tradelicense.domain.valueobjects.TradeName;
import com.trade.tradelicense.domain.valueobjects.UserId;
import com.trade.tradelicense.application.common.TradeLicenseRepositoryPort;
import org.springframework.stereotype.Repository;
import com.trade.tradelicense.infrastructure.persistence.JpaTradeLicenseEntity;

import java.util.List;
import java.util.Optional;

@Repository
public class TradeLicenseRepositoryAdapter implements TradeLicenseRepositoryPort {
    private final SpringDataTradeLicenseRepository repository;

    public TradeLicenseRepositoryAdapter(SpringDataTradeLicenseRepository repository) {
        this.repository = repository;
    }

    @Override
    public TradeLicense save(TradeLicense tradeLicense) {
        repository.save(toEntity(tradeLicense));
        return tradeLicense;
    }

    private JpaTradeLicenseEntity toEntity(TradeLicense tradeLicense) {
        User licenseHolder = tradeLicense.licenseHolder();
        return new JpaTradeLicenseEntity(
                tradeLicense.id().value(),
                tradeLicense.licenseNumber().value(),
                tradeLicense.sourceApplicationId().value(),
                licenseHolder.getUserId().value(),
                licenseHolder.getRole(),
                licenseHolder.getFullName().value(),
                licenseHolder.getNationalIdNumber().value(),
                licenseHolder.getEmailAddress().value(),
                licenseHolder.getPhoneNumber().value(),
                tradeLicense.tinNumber().value(),
                tradeLicense.tradeName().value(),
                tradeLicense.licenseType().code(),
                tradeLicense.commodity().code(),
                tradeLicense.licensePeriod().startsOn(),
                tradeLicense.licensePeriod().endsOn(),
                tradeLicense.issuedDate(),
                tradeLicense.status()
        );
    }

    private TradeLicense toDomain(JpaTradeLicenseEntity entity) {
        User licenseHolder = new User(
                new UserId(entity.getApplicantId()),
                entity.getApplicantRole(),
                new FullName(entity.getFullName()),
                new NationalIdNumber(entity.getNationalIdNumber()),
                new EmailAddress(entity.getEmail()),
                new PhoneNumber(entity.getPhoneNumber())
        );
        return new TradeLicense(
                new LicenseId(entity.getId()),
                new LicenseNumber(entity.getLicenseNumber()),
                new ApplicationId(entity.getSourceApplicationId()),
                licenseHolder,
                new TinNumber(entity.getTinNumber()),
                new TradeName(defaultText(entity.getTradeName(), entity.getFullName())),
                new TradeLicenseType(entity.getTradeLicenseType(), entity.getTradeLicenseType()),
                new Commodity(entity.getCommodity(), entity.getCommodity()),
                new LicensePeriod(entity.getLicensePeriodStart(), entity.getLicensePeriodEnd()),
                entity.getIssuedDate(),
                entity.getStatus() == null ? LicenseStatus.ACTIVE : entity.getStatus()
        );
    }

    @Override
    public Optional<TradeLicense> findById(LicenseId licenseId) {
        return repository.findById(licenseId.value()).map(this::toDomain);
    }

    @Override
    public Optional<TradeLicense> findBySourceApplicationId(ApplicationId applicationId) {
        return repository.findBySourceApplicationId(applicationId.value()).map(this::toDomain);
    }

    @Override
    public List<TradeLicense> findByLicenseHolderId(UserId userId) {
        return repository.findByApplicantId(userId.value()).stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public boolean existsByLicenseId(LicenseId licenseId) {
        return repository.existsById(licenseId.value());
    }

    @Override
    public boolean existsByLicenseNumber(LicenseNumber licenseNumber) {
        return repository.existsByLicenseNumber(licenseNumber.value());
    }

    @Override
    public boolean existsByTinNumber(TinNumber tinNumber) {
        return repository.existsByTinNumber(tinNumber.value());
    }

    private String defaultText(String value, String defaultValue) {
        if (value == null || value.isBlank()) {
            return defaultValue;
        }
        return value;
    }
}
