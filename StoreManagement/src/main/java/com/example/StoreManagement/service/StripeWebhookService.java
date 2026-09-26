package com.example.StoreManagement.service;

import com.example.StoreManagement.Exception.payment.EventDeserializationException;
import com.example.StoreManagement.dtos.dtoRequest.PaymentCompletedData;
import com.example.StoreManagement.enums.PaymentMethodType;
import com.example.StoreManagement.enums.PaymentProvider;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.*;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class StripeWebhookService {

    private final PaymentService paymentService;

    @Value("${stripe.webhook-key}")
    private String webhookKey;

    public PaymentProvider provider() {
        return PaymentProvider.STRIPE;
    }

    private void handleCheckoutSessionCompleted(Event event) throws StripeException {
        EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();
        StripeObject stripeObject = null;

        stripeObject = dataObjectDeserializer.getObject()
                    .orElseThrow(() -> new EventDeserializationException("Failed to deserialize event"));


        Session session = (Session)  stripeObject;

        UUID checkoutId = UUID.fromString(session.getMetadata().get("checkoutId"));
        String providerPaymentId = session.getPaymentIntent();
        String providerSessionId = session.getId();
        PaymentIntent paymentIntent = PaymentIntent.retrieve(providerPaymentId);
        String providerChargeId = paymentIntent.getLatestCharge();
        Charge charge = Charge.retrieve(providerChargeId);

        String cardBrand = charge.getPaymentMethodDetails().getCard().getBrand();
        String lastCardNumbers = charge.getPaymentMethodDetails().getCard().getLast4();
        BigDecimal amount = BigDecimal.valueOf(session.getAmountTotal(), 2);

        long createdAtUnixTime = session.getCreated();
        LocalDateTime createdAt = LocalDateTime.ofInstant(
                Instant.ofEpochSecond(createdAtUnixTime), ZoneId.systemDefault()
        );

        PaymentCompletedData paymentCompletedData = new PaymentCompletedData(
                checkoutId,
                provider(),
                PaymentMethodType.CARD,
                providerPaymentId,
                providerSessionId,
                providerChargeId,
                amount,
                session.getCurrency(),
                cardBrand,
                lastCardNumbers,
                Instant.now(),
                null
        );

        paymentService.processPaymentSucceeded(paymentCompletedData);
    }


    public String handleWebhook(String sigHeader, String payload) throws StripeException {
        Event event;

        try {
            event = Webhook.constructEvent(payload, sigHeader , webhookKey);
        } catch (SignatureVerificationException e) {
            throw new RuntimeException(e);
        }

        switch (event.getType()) {
            case ("checkout.session.completed") -> handleCheckoutSessionCompleted(event);
            case ("payment_intent.payment_failed") -> handleAsyncPaymentFailed(event);
        }

        return "OK";
    }

    private void handleAsyncPaymentFailed(Event event) throws StripeException {
        EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();
        StripeObject stripeObject = null;

        stripeObject = dataObjectDeserializer.getObject()
                    .orElseThrow(() -> new EventDeserializationException("Failed to deserialize event"));

        PaymentIntent paymentIntent = (PaymentIntent) stripeObject;
        String providerChargeId = paymentIntent.getLatestCharge();
        Charge charge = Charge.retrieve(providerChargeId);
        String chargeId = charge.getId();
        String cardBrand = charge.getPaymentMethodDetails().getCard().getBrand();
        String lastCardNumbers = charge.getPaymentMethodDetails().getCard().getLast4();

        StripeError error = paymentIntent.getLastPaymentError();

        switch(error.getType()) {
            case "card_error":
                processPaymentFailed(paymentIntent, charge, "card_error: " + error.getMessage());
                break;
            case "invalid_request":
                processPaymentFailed(paymentIntent, charge, "invalid_request: " + error.getMessage());
                break;
            default:
                processPaymentFailed(paymentIntent, charge, error.getMessage());
                break;
        }
    }


    private void processPaymentFailed(PaymentIntent paymentIntent, Charge charge, String error) {

        paymentService.processPaymentFailed(
                new PaymentCompletedData(
                        UUID.fromString(paymentIntent.getMetadata().get("checkoutId")),
                        this.provider(),
                        PaymentMethodType.CARD,
                        paymentIntent.getId(),
                        null,
                        charge.getId(),
                        BigDecimal.valueOf(paymentIntent.getAmount(), 2),
                        paymentIntent.getCurrency(),
                        charge.getPaymentMethodDetails().getCard().getBrand(),
                        charge.getPaymentMethodDetails().getCard().getLast4(),
                        Instant.now(),
                        error
                )
        );
    }
}
