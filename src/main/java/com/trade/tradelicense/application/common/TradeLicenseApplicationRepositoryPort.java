package com.trade.tradelicense.application.common;

import com.trade.tradelicense.domain.aggregates.TradeLicenseApplication;
import com.trade.tradelicense.domain.enums.ApplicationStatus;
import com.trade.tradelicense.domain.valueobjects.ApplicationId;
import com.trade.tradelicense.domain.valueobjects.ApproverId;
import com.trade.tradelicense.domain.valueobjects.DocumentId;
import com.trade.tradelicense.domain.valueobjects.EmailAddress;
import com.trade.tradelicense.domain.valueobjects.FullName;
import com.trade.tradelicense.domain.valueobjects.NationalIdNumber;
import com.trade.tradelicense.domain.valueobjects.PaymentSettlementId;
import com.trade.tradelicense.domain.valueobjects.PhoneNumber;
import com.trade.tradelicense.domain.valueobjects.ReviewerId;
import com.trade.tradelicense.domain.valueobjects.UserId;

import java.util.List;
import java.util.Optional;

public interface TradeLicenseApplicationRepositoryPort {
    TradeLicenseApplication save(TradeLicenseApplication application);

    Optional<TradeLicenseApplication> findById(ApplicationId applicationId);

    List<TradeLicenseApplication> findByStatus(ApplicationStatus status);

    boolean existsByApplicationId(ApplicationId applicationId);

    boolean existsByUserId(UserId userId);

    boolean existsByReviewerId(ReviewerId reviewerId);

    boolean existsByApproverId(ApproverId approverId);

    boolean existsByDocumentId(DocumentId documentId);

    boolean existsByPaymentSettlementId(PaymentSettlementId paymentSettlementId);

    boolean existsByFullName(FullName fullName);

    boolean existsByNationalIdNumber(NationalIdNumber nationalIdNumber);

    boolean existsByEmailAddress(EmailAddress emailAddress);

    boolean existsByPhoneNumber(PhoneNumber phoneNumber);
}
