package com.example.StoreManagement.service;

import com.example.StoreManagement.model.dtoRequest.OrderPaymentRequest;
import com.example.StoreManagement.model.dtoRequest.PaymentCreationRequest;
import com.example.StoreManagement.model.dtoResponse.StripeDtoResponse;
import com.example.StoreManagement.model.entity.enums.PaymentMethodType;
import com.example.StoreManagement.model.entity.enums.PaymentProvider;
import com.example.StoreManagement.model.repository.OrderRepository;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.*;
import com.stripe.net.Webhook;
import com.stripe.param.checkout.SessionCreateParams;
import com.stripe.model.checkout.Session;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@Service
public class StripeService {

    @Value("${stripe.webhook-key}")
    private String webhookKey;

    private final PaymentService paymentService;
    private final OrderRepository orderRepository;
    //private final OrderService orderService;

    public StripeDtoResponse createCheckoutPayment(OrderPaymentRequest orderPaymentRequest) {

        // Checkout Session params
        SessionCreateParams.LineItem.PriceData.ProductData productData =
                SessionCreateParams.LineItem.PriceData.ProductData.builder()
                        .setName(orderPaymentRequest.description())
                        .build();

        long amount = orderPaymentRequest.amount().multiply(BigDecimal.valueOf(100)).longValue();

        SessionCreateParams.LineItem.PriceData priceData = SessionCreateParams.LineItem.PriceData.builder()
                .setCurrency(orderPaymentRequest.currency())
                .setUnitAmount(amount)
                .setProductData(productData)
                .build();

        SessionCreateParams.LineItem lineItem =
                SessionCreateParams.LineItem.builder()
                        .setQuantity(1L)
                        .setPriceData(priceData)
                        .build();

        String orderIds = String.join(",", orderPaymentRequest.orderIds());

        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://localhost:8080/sucess")
                .setCancelUrl("http://localhost:8080/cancel")
                .addLineItem(lineItem)
                .putMetadata("orderIds", orderIds)
                .build();

        Session session = null;

        try {
            session = Session.create(params);
        } catch (StripeException e) {

            throw new RuntimeException(e);
        }

        StripeDtoResponse stripeResponse = new StripeDtoResponse(session.getId(), session.getUrl());
        return stripeResponse;
    }


    public String handleWebhookCheckout(String sigHeader, String payload) throws StripeException {

        Event event;

        try {
            event = Webhook.constructEvent(payload, sigHeader , webhookKey);
        } catch (SignatureVerificationException e) {
            throw new RuntimeException(e);
        }

        switch (event.getType()) {
            case ("checkout.session.completed") -> hadleCheckoutSessionCompleted(event);
        }

        return "OK";
    }

    private void hadleCheckoutSessionCompleted(Event event) throws StripeException {
        EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();

        StripeObject stripeObject = null;
        try {
            stripeObject = dataObjectDeserializer.getObject().orElseThrow(() -> new IllegalAccessException("Failed to deserialize event"));
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        Session session = (Session)  stripeObject;

        this.paymentService.createPayment(this.retrievePaymentData(session));
    }


    private PaymentCreationRequest retrievePaymentData(Session session) throws StripeException {

        String orderIds = session.getMetadata().get("orderIds");
        List<String> orderIdsList = Arrays.asList(orderIds.split(","));
        BigDecimal amount = BigDecimal.valueOf(session.getAmountTotal(), 2);

        String paymentIntentId = session.getPaymentIntent();
        PaymentIntent paymentIntent = PaymentIntent.retrieve(paymentIntentId);

        String chargeId = paymentIntent.getLatestCharge();
        Charge charge = Charge.retrieve(chargeId);

        String carBrand = charge.getPaymentMethodDetails().getCard().getBrand();
        String lastCardNumbers = charge.getPaymentMethodDetails().getCard().getLast4();

        String providerSessionId = session.getId();

        long createdAtUnixTime = session.getCreated();
        LocalDateTime createdAt = LocalDateTime.ofInstant(
                Instant.ofEpochSecond(createdAtUnixTime), ZoneId.systemDefault()
        );

        return new PaymentCreationRequest(
                orderIdsList,
                amount,
                session.getCurrency(),
                PaymentProvider.STRIPE,
                PaymentMethodType.CARD,
                carBrand,
                lastCardNumbers,
                paymentIntentId,
                providerSessionId,
                chargeId,
                createdAt
        );
    }

    public String payCheckoutSession(String id) throws StripeException {
        Session session = Session.retrieve(id);

        if ("open".equals(session.getStatus())) {
            return session.getUrl();
        } else {
            throw new RuntimeException("Session already completed or expired");
        }
    }

    /*
    public void expireCheckoutSession(String id) {

        Session session;
        try {
            session = Session.retrieve(id);
        } catch (StripeException e) {
            throw new RuntimeException("Não foi possível recuperar a sessão: " + id, e);
        }

        if(!"open".equals(session.getStatus()))
            throw new RuntimeException("Session already completed or expired");

        try {
            session.expire();
        } catch (StripeException e) {
            throw new RuntimeException("Session already completed or expired");
        }


        this.orderService.cancelOrder(id);
    }

     */
}
