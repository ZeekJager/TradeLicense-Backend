package com.trade.tradelicense.domain.factories;

import com.trade.tradelicense.domain.aggregates.TradeLicenseApplication;
import com.trade.tradelicense.domain.entities.PaymentSettlement;
import com.trade.tradelicense.domain.entities.User;
import com.trade.tradelicense.domain.valueobjects.Commodity;
import com.trade.tradelicense.domain.valueobjects.Money;
import com.trade.tradelicense.domain.valueobjects.PaymentReference;
import com.trade.tradelicense.domain.valueobjects.TradeLicenseType;

public class TradeLicenseApplicationFactory {
    public TradeLicenseApplication createDraftApplication(
            User applicant,
            TradeLicenseType licenseType,
            Commodity commodity,
            Money fee,
            PaymentReference paymentReference
    ) {
        return TradeLicenseApplication.createDraft(applicant, licenseType, commodity, fee, paymentReference);
    }

    public TradeLicenseApplication createDraftApplication(
            User applicant,
            TradeLicenseType licenseType,
            Commodity commodity,
            PaymentSettlement paymentSettlement
    ) {
        return TradeLicenseApplication.createDraft(applicant, licenseType, commodity, paymentSettlement);
    }
}
