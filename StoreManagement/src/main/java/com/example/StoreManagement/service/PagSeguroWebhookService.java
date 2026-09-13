package com.example.StoreManagement.service;

import com.example.StoreManagement.model.dtoRequest.PaymentCompletedData;
import com.example.StoreManagement.model.dtoResponse.PagBankPaymentWebhook;
import com.example.StoreManagement.model.entity.enums.PaymentMethodType;
import com.example.StoreManagement.model.entity.enums.PaymentProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tools.jackson.databind.json.JsonMapper;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class PagSeguroWebhookService {

    @Autowired
    private JsonMapper jsonMapper;
    private final PaymentService paymentService;

    public PaymentProvider provider() {
        return PaymentProvider.PAG_SEGURO;
    }

    public void handleWebhook(String payload, String productId) {
        PagBankPaymentWebhook webhook =
                jsonMapper.readValue(
                        payload,
                        PagBankPaymentWebhook.class
                );

        switch (webhook.charges().get(0).status()) {
            case "PAID", "AUTHORIZED" -> handleCheckoutCompleted(webhook);
            case "DECLINED" -> handleCheckoutPaymentFailed(webhook);
            default -> throw new RuntimeException("An error occurred while processing Payment Webhook");
        }
    }

    private void handleCheckoutCompleted(PagBankPaymentWebhook paymentWebhook) {
        PaymentCompletedData paymentCompletedData = this.processPaymentCompleted(paymentWebhook);
        this.paymentService.processPaymentSucceeded(paymentCompletedData);
    }

    private void handleCheckoutPaymentFailed(PagBankPaymentWebhook paymentWebhook) {
        PaymentCompletedData paymentCompletedData = this.processPaymentCompleted(paymentWebhook);
        this.paymentService.processPaymentFailed(paymentCompletedData);
    }

    private PaymentCompletedData processPaymentCompleted(PagBankPaymentWebhook paymentWebhook) {
        if (paymentWebhook.charges().isEmpty())
            throw new IllegalStateException("PagBank webhook contains no charges");

        PagBankPaymentWebhook.PagBankCharge charge = paymentWebhook.charges().getFirst();

        UUID checkoutId = UUID.fromString(paymentWebhook.items().getFirst().referenceId());
        BigDecimal amount = BigDecimal.valueOf(charge.amount().value(), 2);
        String currency = charge.amount().currency();
        String paymentMethodType = charge.paymentMethod().type();
        Instant createdAt = OffsetDateTime.parse(charge.createdAt()).toInstant();
        String paymentMessage = charge.paymentResponse().message();
        PaymentMethodType methodType = switch (paymentMethodType) {
            case "CREDIT_CARD" -> PaymentMethodType.CARD;
            default -> throw new IllegalStateException("Payment Method does not exist: " + paymentMethodType);
        };

        String cardBrand = null;
        String cardLastDigits = null;

        if (methodType == PaymentMethodType.CARD && charge.paymentMethod().card() != null) {
            cardBrand = charge.paymentMethod().card().brand();
            cardLastDigits = charge.paymentMethod().card().lastDigits();
        }

        PaymentCompletedData paymentCompletedData = new PaymentCompletedData(
                checkoutId,
                this.provider(),
                methodType,
                checkoutId.toString(),
                paymentWebhook.id(),
                charge.id(),
                amount,
                currency,
                cardBrand,
                cardLastDigits,
                createdAt,
                paymentMessage
        );
        return paymentCompletedData;
    }

}
