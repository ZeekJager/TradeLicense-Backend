package com.trade.tradelicense.infrastructure.repositories;

import com.trade.tradelicense.domain.aggregates.TradeLicenseApplication;
import com.trade.tradelicense.domain.entities.ApplicationDocument;
import com.trade.tradelicense.domain.entities.ApprovalRecord;
import com.trade.tradelicense.domain.entities.DocumentPackage;
import com.trade.tradelicense.domain.entities.PaymentSettlement;
import com.trade.tradelicense.domain.entities.ReviewRecord;
import com.trade.tradelicense.domain.entities.User;
import com.trade.tradelicense.domain.enums.ApplicationStatus;
import com.trade.tradelicense.domain.enums.DocumentStatus;
import com.trade.tradelicense.domain.enums.PaymentStatus;
import com.trade.tradelicense.domain.enums.UserRole;
import com.trade.tradelicense.domain.valueobjects.ApplicationId;
import com.trade.tradelicense.domain.valueobjects.ApprovalComment;
import com.trade.tradelicense.domain.valueobjects.ApproverId;
import com.trade.tradelicense.domain.valueobjects.Commodity;
import com.trade.tradelicense.domain.valueobjects.DocumentId;
import com.trade.tradelicense.domain.valueobjects.DocumentReference;
import com.trade.tradelicense.domain.valueobjects.DocumentType;
import com.trade.tradelicense.domain.valueobjects.EmailAddress;
import com.trade.tradelicense.domain.valueobjects.FullName;
import com.trade.tradelicense.domain.valueobjects.Money;
import com.trade.tradelicense.domain.valueobjects.NationalIdNumber;
import com.trade.tradelicense.domain.valueobjects.PaymentReference;
import com.trade.tradelicense.domain.valueobjects.PaymentSettlementId;
import com.trade.tradelicense.domain.valueobjects.PhoneNumber;
import com.trade.tradelicense.domain.valueobjects.ReviewComment;
import com.trade.tradelicense.domain.valueobjects.ReviewerId;
import com.trade.tradelicense.domain.valueobjects.TradeLicenseType;
import com.trade.tradelicense.domain.valueobjects.UserId;
import com.trade.tradelicense.application.common.TradeLicenseApplicationRepositoryPort;
import org.springframework.stereotype.Repository;
import com.trade.tradelicense.infrastructure.persistence.JpaTradeLicenseApplicationEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class TradeLicenseApplicationRepositoryAdapter implements TradeLicenseApplicationRepositoryPort {
    private final SpringDataTradeLicenseApplicationRepository repository;

    public TradeLicenseApplicationRepositoryAdapter(SpringDataTradeLicenseApplicationRepository repository) {
        this.repository = repository;
    }

    @Override
    public TradeLicenseApplication save(TradeLicenseApplication application) {
        repository.save(toEntity(application));
        return application;
    }

    @Override
    public Optional<TradeLicenseApplication> findById(ApplicationId applicationId) {
        return repository.findById(applicationId.value()).map(this::toDomain);
    }

    @Override
    public List<TradeLicenseApplication> findByStatus(ApplicationStatus status) {
        return repository.findByStatus(status).stream()
                .map(this::toDomain)
                .toList();
    }

    private JpaTradeLicenseApplicationEntity toEntity(TradeLicenseApplication application) {
        User applicant = application.applicant();
        ApplicationDocument document = application.documentPackage().documents().stream()
                .findFirst()
                .orElse(null);
        PaymentSettlement paymentSettlement = application.paymentSettlement();
        ReviewRecord reviewRecord = application.reviewRecord();
        ApprovalRecord approvalRecord = application.approvalRecord();
        return new JpaTradeLicenseApplicationEntity(
                application.id().value(),
                applicant.getUserId().value(),
                applicant.getRole(),
                applicant.getFullName().value(),
                applicant.getNationalIdNumber().value(),
                applicant.getEmailAddress().value(),
                applicant.getPhoneNumber().value(),
                application.licenseType().code(),
                application.commodity().code(),
                document == null ? null : document.id().value(),
                document == null ? null : document.documentType().value(),
                document == null ? null : document.documentReference().value(),
                document == null ? null : document.status(),
                document == null ? null : document.uploadedAt(),
                paymentSettlement.id().value(),
                paymentSettlement.money().amount(),
                paymentSettlement.money().currency(),
                paymentSettlement.paymentReference().value(),
                paymentSettlement.status(),
                paymentSettlement.settledAt(),
                reviewRecord == null ? null : reviewRecord.reviewer().value(),
                reviewRecord == null ? null : reviewRecord.decision(),
                reviewRecord == null ? null : reviewRecord.comment().value(),
                reviewRecord == null ? null : reviewRecord.reviewedAt(),
                approvalRecord == null ? null : approvalRecord.approver().value(),
                approvalRecord == null ? null : approvalRecord.decision(),
                approvalRecord == null ? null : approvalRecord.comment().value(),
                approvalRecord == null ? null : approvalRecord.approvedAt(),
                application.status()
        );
    }

    private TradeLicenseApplication toDomain(JpaTradeLicenseApplicationEntity entity) {
        User applicant = new User(
                new UserId(entity.getApplicantId()),
                entity.getApplicantRole() == null ? UserRole.CUSTOMER : entity.getApplicantRole(),
                new FullName(entity.getFullName()),
                new NationalIdNumber(entity.getNationalIdNumber()),
                new EmailAddress(entity.getEmail()),
                new PhoneNumber(entity.getPhoneNumber())
        );
        TradeLicenseType licenseType = new TradeLicenseType(entity.getTradeLicenseType(), entity.getTradeLicenseType());
        Commodity commodity = new Commodity(entity.getCommodity(), entity.getCommodity());
        DocumentPackage documentPackage = new DocumentPackage();
        if (entity.getDocumentId() != null) {
            documentPackage.addDocument(new ApplicationDocument(
                    new DocumentId(entity.getDocumentId()),
                    new DocumentType(defaultText(entity.getDocumentType(), "UPLOADED_DOCUMENT")),
                    new DocumentReference(defaultText(entity.getDocumentReference(), "UPLOADED_DOCUMENT")),
                    entity.getDocumentStatus() == null ? DocumentStatus.UPLOADED : entity.getDocumentStatus(),
                    entity.getDocumentUploadedAt() == null ? LocalDateTime.now() : entity.getDocumentUploadedAt()
            ));
        }

        PaymentSettlement paymentSettlement = new PaymentSettlement(
                entity.getPaymentSettlementId() == null ? PaymentSettlementId.newId() : new PaymentSettlementId(entity.getPaymentSettlementId()),
                new Money(entity.getPaymentAmount() == null ? BigDecimal.ZERO : entity.getPaymentAmount(), defaultText(entity.getPaymentCurrency(), "USD")),
                new PaymentReference(defaultText(entity.getPaymentReference(), "PENDING_PAYMENT")),
                entity.getPaymentStatus() == null ? PaymentStatus.PENDING : entity.getPaymentStatus(),
                entity.getPaymentSettledAt()
        );
        ReviewRecord reviewRecord = null;
        if (entity.getReviewerId() != null
                && entity.getReviewDecision() != null
                && entity.getReviewComment() != null
                && entity.getReviewedAt() != null) {
            reviewRecord = new ReviewRecord(
                    new ReviewerId(entity.getReviewerId()),
                    entity.getReviewDecision(),
                    new ReviewComment(entity.getReviewComment()),
                    entity.getReviewedAt()
            );
        }

        ApprovalRecord approvalRecord = null;
        if (entity.getApproverId() != null
                && entity.getApprovalDecision() != null
                && entity.getApprovalComment() != null
                && entity.getApprovedAt() != null) {
            approvalRecord = new ApprovalRecord(
                    new ApproverId(entity.getApproverId()),
                    entity.getApprovalDecision(),
                    new ApprovalComment(entity.getApprovalComment()),
                    entity.getApprovedAt()
            );
        }

        return TradeLicenseApplication.rehydrate(
                new ApplicationId(entity.getId()),
                applicant,
                licenseType,
                commodity,
                documentPackage,
                paymentSettlement,
                reviewRecord,
                approvalRecord,
                entity.getStatus()
        );
    }

    @Override
    public boolean existsByApplicationId(ApplicationId applicationId) {
        return repository.existsById(applicationId.value());
    }

    @Override
    public boolean existsByUserId(UserId userId) {
        return repository.existsByApplicantId(userId.value());
    }

    @Override
    public boolean existsByReviewerId(ReviewerId reviewerId) {
        return repository.existsByReviewerId(reviewerId.value());
    }

    @Override
    public boolean existsByApproverId(ApproverId approverId) {
        return repository.existsByApproverId(approverId.value());
    }

    @Override
    public boolean existsByDocumentId(DocumentId documentId) {
        return repository.existsByDocumentId(documentId.value());
    }

    @Override
    public boolean existsByPaymentSettlementId(PaymentSettlementId paymentSettlementId) {
        return repository.existsByPaymentSettlementId(paymentSettlementId.value());
    }

    @Override
    public boolean existsByFullName(FullName fullName) {
        return repository.existsByFullName(fullName.value());
    }

    @Override
    public boolean existsByNationalIdNumber(NationalIdNumber nationalIdNumber) {
        return repository.existsByNationalIdNumber(nationalIdNumber.value());
    }

    @Override
    public boolean existsByEmailAddress(EmailAddress emailAddress) {
        return repository.existsByEmail(emailAddress.value());
    }

    @Override
    public boolean existsByPhoneNumber(PhoneNumber phoneNumber) {
        return repository.existsByPhoneNumber(phoneNumber.value());
    }

    private String defaultText(String value, String defaultValue) {
        if (value == null || value.isBlank()) {
            return defaultValue;
        }
        return value;
    }
}
