package com.trade.tradelicense.domain.aggregates;

import com.trade.tradelicense.domain.entities.ApplicationDocument;
import com.trade.tradelicense.domain.entities.ApprovalRecord;
import com.trade.tradelicense.domain.entities.DocumentPackage;
import com.trade.tradelicense.domain.entities.PaymentSettlement;
import com.trade.tradelicense.domain.entities.ReviewRecord;
import com.trade.tradelicense.domain.entities.User;
import com.trade.tradelicense.domain.enums.ApplicationStatus;
import com.trade.tradelicense.domain.enums.ApprovalDecision;
import com.trade.tradelicense.domain.enums.PaymentStatus;
import com.trade.tradelicense.domain.enums.ReviewDecision;
import com.trade.tradelicense.domain.exceptions.InvalidApplicationStateException;
import com.trade.tradelicense.domain.exceptions.MissingRequiredDocumentException;
import com.trade.tradelicense.domain.exceptions.PaymentNotSettledException;
import com.trade.tradelicense.domain.exceptions.UnauthorizedDomainActionException;
import com.trade.tradelicense.domain.valueobjects.Actor;
import com.trade.tradelicense.domain.valueobjects.ApplicationId;
import com.trade.tradelicense.domain.valueobjects.ApprovalComment;
import com.trade.tradelicense.domain.valueobjects.ApproverId;
import com.trade.tradelicense.domain.valueobjects.Commodity;
import com.trade.tradelicense.domain.valueobjects.DocumentReference;
import com.trade.tradelicense.domain.valueobjects.DocumentType;
import com.trade.tradelicense.domain.valueobjects.Money;
import com.trade.tradelicense.domain.valueobjects.PaymentReference;
import com.trade.tradelicense.domain.valueobjects.ReviewComment;
import com.trade.tradelicense.domain.valueobjects.ReviewerId;
import com.trade.tradelicense.domain.valueobjects.TradeLicenseType;

import java.math.BigDecimal;
import java.util.Objects;

public class TradeLicenseApplication {
    private final ApplicationId id;
    private final User applicant;
    private final TradeLicenseType licenseType;
    private final Commodity commodity;
    private final DocumentPackage documentPackage;
    private final PaymentSettlement paymentSettlement;
    private ReviewRecord reviewRecord;
    private ApprovalRecord approvalRecord;
    private ApplicationStatus status;

    private TradeLicenseApplication(
            ApplicationId id,
            User applicant,
            TradeLicenseType licenseType,
            Commodity commodity,
            DocumentPackage documentPackage,
            PaymentSettlement paymentSettlement,
            ReviewRecord reviewRecord,
            ApprovalRecord approvalRecord,
            ApplicationStatus status
    ) {
        this.id = Objects.requireNonNull(id, "Application id is required");
        this.applicant = Objects.requireNonNull(applicant, "Applicant is required");
        this.licenseType = Objects.requireNonNull(licenseType, "Trade license type is required");
        this.commodity = Objects.requireNonNull(commodity, "Commodity is required");
        this.documentPackage = Objects.requireNonNull(documentPackage, "Document package is required");
        this.paymentSettlement = Objects.requireNonNull(paymentSettlement, "Payment settlement is required");
        this.reviewRecord = reviewRecord;
        this.approvalRecord = approvalRecord;
        this.status = Objects.requireNonNull(status, "Application status is required");
    }

    public static TradeLicenseApplication createDraft(
            User applicant,
            TradeLicenseType licenseType,
            Commodity commodity,
            Money fee,
            PaymentReference paymentReference
    ) {
        return createDraft(applicant, licenseType, commodity, PaymentSettlement.pending(fee, paymentReference));
    }

    public static TradeLicenseApplication createDraft(
            User applicant,
            TradeLicenseType licenseType,
            Commodity commodity,
            PaymentSettlement paymentSettlement
    ) {
        return new TradeLicenseApplication(
                ApplicationId.newId(),
                applicant,
                licenseType,
                commodity,
                new DocumentPackage(),
                paymentSettlement,
                null,
                null,
                ApplicationStatus.DRAFT
        );
    }

    public static TradeLicenseApplication rehydrate(
            ApplicationId id,
            User applicant,
            TradeLicenseType licenseType,
            Commodity commodity,
            ApplicationStatus status
    ) {
        PaymentSettlement paymentSettlement = new PaymentSettlement(
                com.trade.tradelicense.domain.valueobjects.PaymentSettlementId.newId(),
                new Money(BigDecimal.ZERO, "USD"),
                new PaymentReference("REHYDRATED_PAYMENT"),
                status == ApplicationStatus.DRAFT ? PaymentStatus.PENDING : PaymentStatus.SETTLED,
                status == ApplicationStatus.DRAFT ? null : java.time.LocalDateTime.now()
        );
        DocumentPackage documentPackage = new DocumentPackage();
        if (status != ApplicationStatus.DRAFT && status != ApplicationStatus.CANCELLED) {
            documentPackage.addDocument(ApplicationDocument.upload(
                    new DocumentType("REHYDRATED_DOCUMENT"),
                    new DocumentReference("REHYDRATED_DOCUMENT")
            ));
        }
        return rehydrate(id, applicant, licenseType, commodity, documentPackage, paymentSettlement, status);
    }

    public static TradeLicenseApplication rehydrate(
            ApplicationId id,
            User applicant,
            TradeLicenseType licenseType,
            Commodity commodity,
            DocumentPackage documentPackage,
            PaymentSettlement paymentSettlement,
            ApplicationStatus status
    ) {
        return rehydrate(id, applicant, licenseType, commodity, documentPackage, paymentSettlement, null, null, status);
    }

    public static TradeLicenseApplication rehydrate(
            ApplicationId id,
            User applicant,
            TradeLicenseType licenseType,
            Commodity commodity,
            DocumentPackage documentPackage,
            PaymentSettlement paymentSettlement,
            ReviewRecord reviewRecord,
            ApprovalRecord approvalRecord,
            ApplicationStatus status
    ) {
        return new TradeLicenseApplication(
                id,
                applicant,
                licenseType,
                commodity,
                documentPackage,
                paymentSettlement,
                reviewRecord,
                approvalRecord,
                status
        );
    }

    public void attachDocument(DocumentType documentType, DocumentReference documentReference) {
        ensureStatus(ApplicationStatus.DRAFT, "Documents can only be attached while application is in draft");
        documentPackage.addDocument(ApplicationDocument.upload(documentType, documentReference));
    }

    public void settlePayment() {
        paymentSettlement.settle();
    }

    public void submit(Actor actor) {
        requireCustomerOrLicensee(actor, "Only a customer or licensee can submit an application");
        ensureStatus(ApplicationStatus.DRAFT, "Only draft applications can be submitted");
        if (!documentPackage.hasDocuments()) {
            throw new MissingRequiredDocumentException("At least one document is required before submission");
        }
        if (!paymentSettlement.isSettled()) {
            throw new PaymentNotSettledException("Payment must be settled before submission");
        }
        this.status = ApplicationStatus.PENDING_REVIEW;
    }

    public void cancel(Actor actor) {
        requireCustomerOrLicensee(actor, "Only a customer or licensee can cancel an application");
        ensureStatus(ApplicationStatus.DRAFT, "Only draft applications can be cancelled");
        this.status = ApplicationStatus.CANCELLED;
    }

    public void review(Actor actor, ReviewDecision decision, String comment) {
        requireReviewer(actor);
        ensureStatus(ApplicationStatus.PENDING_REVIEW, "Only applications pending review can be reviewed");
        Objects.requireNonNull(decision, "Review decision is required");
        this.reviewRecord = ReviewRecord.create(new ReviewerId(actor.userId().value()), decision, new ReviewComment(comment));
        this.approvalRecord = null;

        this.status = switch (decision) {
            case ACCEPT -> ApplicationStatus.PENDING_APPROVAL;
            case REJECT -> ApplicationStatus.REJECTED;
            case ADJUST -> ApplicationStatus.RETURNED_FOR_ADJUSTMENT;
        };
    }

    public void resubmitAfterAdjustment(Actor actor) {
        requireCustomerOrLicensee(actor, "Only a customer or licensee can resubmit an application");
        ensureStatus(ApplicationStatus.RETURNED_FOR_ADJUSTMENT, "Only applications returned for adjustment can be resubmitted");
        this.status = ApplicationStatus.PENDING_REVIEW;
    }

    public void approve(Actor actor, ApprovalDecision decision, String comment) {
        requireApprover(actor);
        ensureStatus(ApplicationStatus.PENDING_APPROVAL, "Only applications pending approval can be approved");
        Objects.requireNonNull(decision, "Approval decision is required");
        this.approvalRecord = ApprovalRecord.create(new ApproverId(actor.userId().value()), decision, new ApprovalComment(comment));

        this.status = switch (decision) {
            case APPROVE -> ApplicationStatus.APPROVED;
            case REJECT -> ApplicationStatus.REJECTED;
            case REREVIEW -> ApplicationStatus.PENDING_REVIEW;
        };
    }

    public void markLicenseIssued() {
        ensureStatus(ApplicationStatus.APPROVED, "Only approved applications can be marked as license issued");
        this.status = ApplicationStatus.LICENSE_ISSUED;
    }

    private void requireCustomerOrLicensee(Actor actor, String message) {
        Objects.requireNonNull(actor, "Actor is required");
        if (!actor.isCustomerOrLicensee()) {
            throw new UnauthorizedDomainActionException(message);
        }
    }

    private void requireReviewer(Actor actor) {
        Objects.requireNonNull(actor, "Actor is required");
        if (!actor.isReviewer()) {
            throw new UnauthorizedDomainActionException("Only a reviewer can review an application");
        }
    }

    private void requireApprover(Actor actor) {
        Objects.requireNonNull(actor, "Actor is required");
        if (!actor.isApprover()) {
            throw new UnauthorizedDomainActionException("Only an approver can approve an application");
        }
    }

    private void ensureStatus(ApplicationStatus expected, String message) {
        if (status != expected) {
            throw new InvalidApplicationStateException(message);
        }
    }

    public ApplicationId id() {
        return id;
    }

    public User applicant() {
        return applicant;
    }

    public TradeLicenseType licenseType() {
        return licenseType;
    }

    public Commodity commodity() {
        return commodity;
    }

    public DocumentPackage documentPackage() {
        return documentPackage;
    }

    public PaymentSettlement paymentSettlement() {
        return paymentSettlement;
    }

    public ReviewRecord reviewRecord() {
        return reviewRecord;
    }

    public ApprovalRecord approvalRecord() {
        return approvalRecord;
    }

    public ApplicationStatus status() {
        return status;
    }
}
