package com.example.StoreManagement.service;

import com.example.StoreManagement.model.dtoRequest.CheckoutCreationRequest;
import com.example.StoreManagement.model.dtoResponse.ProviderCheckoutResponse;
import com.example.StoreManagement.model.entity.enums.PaymentMethodType;
import com.example.StoreManagement.model.entity.enums.PaymentProvider;
import com.example.StoreManagement.model.repository.OrderRepository;
import com.stripe.exception.StripeException;
import com.stripe.param.checkout.SessionCreateParams;
import com.stripe.model.checkout.Session;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;

@RequiredArgsConstructor
@Service
public class StripePaymentGateway implements PaymentGateway {

    private final OrderRepository orderRepository;

    @Override
    public PaymentProvider provider() {
        return PaymentProvider.STRIPE;
    }

    @Override
    public ProviderCheckoutResponse processPayment(CheckoutCreationRequest checkoutCreationRequest) {

        if (checkoutCreationRequest.paymentMethodType() == PaymentMethodType.CARD) {
            return this.createCheckout(checkoutCreationRequest);
        } else if (checkoutCreationRequest.paymentMethodType() == PaymentMethodType.PIX) {
            throw new RuntimeException("PIX is not supported yet");
        } else {
            return null;
        }
    }


    public ProviderCheckoutResponse createCheckout(CheckoutCreationRequest checkoutCreationRequest) {

        // Checkout Session params
        SessionCreateParams.LineItem.PriceData.ProductData productData =
                SessionCreateParams.LineItem.PriceData.ProductData.builder()
                        .setName(checkoutCreationRequest.description())
                        .build();

        BigDecimal amount = checkoutCreationRequest.amount();
        java.lang.String currency = checkoutCreationRequest.currency();

        SessionCreateParams.LineItem.PriceData priceData = SessionCreateParams.LineItem.PriceData.builder()
                .setCurrency(currency)
                .setUnitAmount(amount.multiply(BigDecimal.valueOf(100)).longValue())
                .setProductData(productData)
                .build();

        SessionCreateParams.LineItem lineItem =
                SessionCreateParams.LineItem.builder()
                        .setQuantity(1L)
                        .setPriceData(priceData)
                        .build();

        SessionCreateParams params = SessionCreateParams.builder()
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://localhost:8080/sucess")
                .setCancelUrl("http://localhost:8080/cancel")
                .addLineItem(lineItem)
                .putMetadata("checkoutId", checkoutCreationRequest.checkoutId().toString())
                .putMetadata("paymentMethodType", checkoutCreationRequest.paymentMethodType().toString())
                .setPaymentIntentData(SessionCreateParams.PaymentIntentData.builder()
                        .putMetadata("checkoutId", checkoutCreationRequest.checkoutId().toString())
                        .build()
                )
                .build();

        Session session = null;
        try {
            session = Session.create(params);
        } catch (StripeException e) {
            throw new RuntimeException(e);
        }

        ProviderCheckoutResponse checkoutResponse = new ProviderCheckoutResponse(
                checkoutCreationRequest.checkoutId(),
                amount,
                currency,
                checkoutCreationRequest.paymentMethodType(),
                this.provider(),
                session.getId(),
                convertToInstant(session.getCreated()),
                convertToInstant(session.getExpiresAt()),
                session.getUrl()
        );
        return checkoutResponse;
    }

    @Override
    public String getCheckoutUrl(String sessionId) {
        try {
            Session session = Session.retrieve(sessionId);
            return session.getUrl();

        } catch (StripeException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void expireSession(String sessionId) {

        Session session;
        try {
            session = Session.retrieve(sessionId);
        } catch (StripeException e) {
            throw new RuntimeException("Cannot recover the session" + sessionId, e);
        }

        if(!"open".equals(session.getStatus()))
            throw new RuntimeException("Session already completed or expired");

        try {
            session.expire();
        } catch (StripeException e) {
            throw new RuntimeException("Session already completed or expired");
        }
    }

    private Instant convertToInstant(Long timestamp) {

        if (timestamp != null)
            return Instant.ofEpochSecond(timestamp);

        return null;
    }
}
