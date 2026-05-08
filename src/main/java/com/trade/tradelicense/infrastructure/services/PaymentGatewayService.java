package com.trade.tradelicense.infrastructure.services;

import com.trade.tradelicense.domain.valueobjects.PaymentReference;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PaymentGatewayService {
    public PaymentReference settlePayment() {
        return new PaymentReference("PAY-" + UUID.randomUUID());
    }
}
