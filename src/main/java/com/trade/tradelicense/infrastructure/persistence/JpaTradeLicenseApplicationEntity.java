package com.trade.tradelicense.infrastructure.persistence;

import com.trade.tradelicense.domain.enums.ApplicationStatus;
import com.trade.tradelicense.domain.enums.ApprovalDecision;
import com.trade.tradelicense.domain.enums.DocumentStatus;
import com.trade.tradelicense.domain.enums.PaymentStatus;
import com.trade.tradelicense.domain.enums.ReviewDecision;
import com.trade.tradelicense.domain.enums.UserRole;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "trade_license_applications")
public class JpaTradeLicenseApplicationEntity {
    @Id
    private UUID id;
    @Column(unique = true)
    private UUID applicantId;

    @Enumerated(EnumType.STRING)
    private UserRole applicantRole;

    @Column(unique = true)
    private String fullName;
    @Column(unique = true)
    private String nationalIdNumber;
    @Column(unique = true)
    private String email;
    @Column(unique = true)
    private String phoneNumber;
    private String tradeLicenseType;
    private String commodity;
    @Column(unique = true)
    private UUID documentId;
    private String documentType;
    private String documentReference;

    @Enumerated(EnumType.STRING)
    private DocumentStatus documentStatus;

    private LocalDateTime documentUploadedAt;
    @Column(unique = true)
    private UUID paymentSettlementId;
    private BigDecimal paymentAmount;
    private String paymentCurrency;
    private String paymentReference;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    private LocalDateTime paymentSettledAt;
    @Column(unique = true)
    private UUID reviewerId;

    @Enumerated(EnumType.STRING)
    private ReviewDecision reviewDecision;

    private String reviewComment;
    private LocalDateTime reviewedAt;
    @Column(unique = true)
    private UUID approverId;

    @Enumerated(EnumType.STRING)
    private ApprovalDecision approvalDecision;

    private String approvalComment;
    private LocalDateTime approvedAt;

    @Enumerated(EnumType.STRING)
    private ApplicationStatus status;

    protected JpaTradeLicenseApplicationEntity() {
    }

    public JpaTradeLicenseApplicationEntity(
            UUID id,
            UUID applicantId,
            UserRole applicantRole,
            String fullName,
            String nationalIdNumber,
            String email,
            String phoneNumber,
            String tradeLicenseType,
            String commodity,
            UUID documentId,
            String documentType,
            String documentReference,
            DocumentStatus documentStatus,
            LocalDateTime documentUploadedAt,
            UUID paymentSettlementId,
            BigDecimal paymentAmount,
            String paymentCurrency,
            String paymentReference,
            PaymentStatus paymentStatus,
            LocalDateTime paymentSettledAt,
            UUID reviewerId,
            ReviewDecision reviewDecision,
            String reviewComment,
            LocalDateTime reviewedAt,
            UUID approverId,
            ApprovalDecision approvalDecision,
            String approvalComment,
            LocalDateTime approvedAt,
            ApplicationStatus status
    ) {
        this.id = id;
        this.applicantId = applicantId;
        this.applicantRole = applicantRole;
        this.fullName = fullName;
        this.nationalIdNumber = nationalIdNumber;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.tradeLicenseType = tradeLicenseType;
        this.commodity = commodity;
        this.documentId = documentId;
        this.documentType = documentType;
        this.documentReference = documentReference;
        this.documentStatus = documentStatus;
        this.documentUploadedAt = documentUploadedAt;
        this.paymentSettlementId = paymentSettlementId;
        this.paymentAmount = paymentAmount;
        this.paymentCurrency = paymentCurrency;
        this.paymentReference = paymentReference;
        this.paymentStatus = paymentStatus;
        this.paymentSettledAt = paymentSettledAt;
        this.reviewerId = reviewerId;
        this.reviewDecision = reviewDecision;
        this.reviewComment = reviewComment;
        this.reviewedAt = reviewedAt;
        this.approverId = approverId;
        this.approvalDecision = approvalDecision;
        this.approvalComment = approvalComment;
        this.approvedAt = approvedAt;
        this.status = status;
    }

    public UUID getId() {
        return id;
    }

    public UUID getApplicantId() {
        return applicantId;
    }

    public UserRole getApplicantRole() {
        return applicantRole;
    }

    public String getFullName() {
        return fullName;
    }

    public String getNationalIdNumber() {
        return nationalIdNumber;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getTradeLicenseType() {
        return tradeLicenseType;
    }

    public String getCommodity() {
        return commodity;
    }

    public UUID getDocumentId() {
        return documentId;
    }

    public String getDocumentType() {
        return documentType;
    }

    public String getDocumentReference() {
        return documentReference;
    }

    public DocumentStatus getDocumentStatus() {
        return documentStatus;
    }

    public LocalDateTime getDocumentUploadedAt() {
        return documentUploadedAt;
    }

    public UUID getPaymentSettlementId() {
        return paymentSettlementId;
    }

    public BigDecimal getPaymentAmount() {
        return paymentAmount;
    }

    public String getPaymentCurrency() {
        return paymentCurrency;
    }

    public String getPaymentReference() {
        return paymentReference;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public LocalDateTime getPaymentSettledAt() {
        return paymentSettledAt;
    }

    public UUID getReviewerId() {
        return reviewerId;
    }

    public ReviewDecision getReviewDecision() {
        return reviewDecision;
    }

    public String getReviewComment() {
        return reviewComment;
    }

    public LocalDateTime getReviewedAt() {
        return reviewedAt;
    }

    public UUID getApproverId() {
        return approverId;
    }

    public ApprovalDecision getApprovalDecision() {
        return approvalDecision;
    }

    public String getApprovalComment() {
        return approvalComment;
    }

    public LocalDateTime getApprovedAt() {
        return approvedAt;
    }

    public ApplicationStatus getStatus() {
        return status;
    }
}
