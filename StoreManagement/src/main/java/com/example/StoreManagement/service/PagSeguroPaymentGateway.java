package com.example.StoreManagement.service;
import com.example.StoreManagement.Exception.ResourceNotFoundException;
import com.example.StoreManagement.Exception.payment.InvalidCheckoutResponseException;
import com.example.StoreManagement.Exception.payment.InvalidSessionStateException;
import com.example.StoreManagement.Exception.payment.InvalidWebhookPayloadException;
import com.example.StoreManagement.Exception.payment.UnsupportedPaymentMethodException;
import com.example.StoreManagement.dtos.dtoRequest.CheckoutCreationRequest;
import com.example.StoreManagement.dtos.dtoRequest.PagBankCheckoutRequest;
import com.example.StoreManagement.dtos.dtoResponse.PagBankCheckoutResponse;
import com.example.StoreManagement.dtos.dtoResponse.ProviderCheckoutResponse;
import com.example.StoreManagement.enums.PaymentMethodType;
import com.example.StoreManagement.enums.PaymentProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import tools.jackson.databind.json.JsonMapper;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;


@Service
public class PagSeguroPaymentGateway implements PaymentGateway {

    @Autowired
    private JsonMapper jsonMapper;
    private final RestClient restClient;

    private final String notificationCheckoutUrl;
    private final String notificationPaymentUrl;
    private final String redirectUrl;
    private final String returnUrl;

    public PagSeguroPaymentGateway(
            RestClient pagBankRestClient,
            @Value("${pagbank.notification-checkout-url}") String notificationUrl,
            @Value("${pagbank.notification-payment-url}") String notificationPaymentUrl,
            @Value("${pagbank.redirect-url}") String redirectUrl,
            @Value("${pagbank.return-url}") String returnUrl
    ) {
        this.restClient = pagBankRestClient;
        this.notificationCheckoutUrl = notificationUrl;
        this.notificationPaymentUrl = notificationPaymentUrl;
        this.redirectUrl = redirectUrl;
        this.returnUrl = returnUrl;
    }

    @Override
    public PaymentProvider provider() {
        return PaymentProvider.PAG_SEGURO;
    }


    @Override
    public ProviderCheckoutResponse processPayment(CheckoutCreationRequest checkoutCreationRequest) {
        if (checkoutCreationRequest.paymentMethodType() == PaymentMethodType.CARD) {
            return this.createCheckout(checkoutCreationRequest);

        } else if (checkoutCreationRequest.paymentMethodType() == PaymentMethodType.PIX) {
            throw new UnsupportedPaymentMethodException(PaymentMethodType.PIX.toString());

        } else {
            return null;
        }
    }


    public ProviderCheckoutResponse createCheckout(CheckoutCreationRequest checkoutCreationRequest) {

        Integer unitAmount = checkoutCreationRequest.amount().movePointRight(2).intValueExact();

        PagBankCheckoutRequest request = new PagBankCheckoutRequest(
                checkoutCreationRequest.checkoutId().toString(),
                true,
                List.of(new PagBankCheckoutRequest.Item(
                        checkoutCreationRequest.checkoutId().toString(),
                        checkoutCreationRequest.description(),
                        1,
                        unitAmount)
                ),

                List.of(
                        switch (checkoutCreationRequest.paymentMethodType().toString()) {
                            case "CARD" -> new PagBankCheckoutRequest.PaymentMethod("CREDIT_CARD");
                            case "PIX" -> new PagBankCheckoutRequest.PaymentMethod("PIX");
                            default -> throw new UnsupportedPaymentMethodException(PaymentMethodType.PIX.toString());
                }),

                List.of(new PagBankCheckoutRequest.PaymentMethodConfig(
                        "CREDIT_CARD",
                        List.of(new PagBankCheckoutRequest.ConfigOption("INSTALLMENTS_LIMIT", "1"))
                )),
                List.of(notificationCheckoutUrl),
                List.of(notificationPaymentUrl),
                checkoutCreationRequest.description(),
                redirectUrl,
                returnUrl
        );

        PagBankCheckoutResponse response = restClient.post()
                .uri("/checkouts")
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .body(PagBankCheckoutResponse.class);

        return this.mapperPagBankResponseToProviderResponse(response);
    }


    public ProviderCheckoutResponse mapperPagBankResponseToProviderResponse(PagBankCheckoutResponse pagBankResponse) {

        UUID checkoutId = UUID.fromString(pagBankResponse.referenceId());
        BigDecimal amount = BigDecimal.valueOf(pagBankResponse.items().get(0).unitAmount(), 2);
        String currency = "BRL";

        PaymentMethodType paymentMethodType =
                switch (pagBankResponse.paymentMethods().get(0).type()) {
                    case "CREDIT_CARD" -> PaymentMethodType.CARD;
                    case "PIX" -> PaymentMethodType.PIX;
                    default -> throw new UnsupportedPaymentMethodException(PaymentMethodType.PIX.toString());
        };

        this.provider();
        String providerSessionId = pagBankResponse.id();
        OffsetDateTime offsetCreatedAt = OffsetDateTime.parse(pagBankResponse.createdAt());
        OffsetDateTime offsetExpiresAt = offsetCreatedAt.plusHours(2);
        Instant createdAt = offsetCreatedAt.toInstant();
        Instant expiresAt =  offsetExpiresAt.toInstant();
        String checkoutUrl = pagBankResponse.links()
                .stream()
                .filter(link -> "PAY".equals(link.rel()))
                .findFirst()
                .map(PagBankCheckoutResponse.Link::href)
                .orElseThrow(() -> new InvalidCheckoutResponseException(InvalidCheckoutResponseException.PAY_LINK_NOT_FOUND));


        return new ProviderCheckoutResponse(
                checkoutId,
                amount,
                currency,
                paymentMethodType,
                this.provider(),
                providerSessionId,
                createdAt,
                expiresAt,
                checkoutUrl
        );
    }

    @Override
    public String getCheckoutUrl(String sessionId) {
        PagBankCheckoutResponse response = restClient.get()
                .uri("/checkouts/{checkoutId}", sessionId)
                .retrieve()
                .body(PagBankCheckoutResponse.class);


        if (response.status().equals("INACTIVE")) {
            throw new InvalidSessionStateException(InvalidSessionStateException.SESSION_COMPLETED_OR_EXPIRED);
        }

        return response.links().stream()
                .filter(link -> "PAY".equals(link.rel()))
                .findFirst()
                .map(PagBankCheckoutResponse.Link::href)
                .orElseThrow(() -> new InvalidCheckoutResponseException(InvalidCheckoutResponseException.CHECKOUT_URL_NOT_FOUND));
    }

    @Override
    public void expireSession(String sessionId) {

        PagBankCheckoutResponse response = restClient.post()
                .uri("/checkouts/{checkoutId}/inactivate", sessionId)
                .retrieve()
                .body(PagBankCheckoutResponse.class);
    }
}
