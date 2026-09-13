package com.example.StoreManagement.model.dtoResponse;

import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.util.List;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record PagBankPaymentWebhook(
        String id,
        String referenceId,
        String createdAt,
        Customer customer,
        List<Item> items,
        List<PagBankCharge> charges
) {

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record Customer(
            String name,
            String email,
            String taxId,
            List<Phone> phones
    ) {}

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record Phone(
            String type,
            String country,
            String area,
            String number
    ) {}

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record Item(
            String referenceId,
            String name,
            int quantity,
            long unitAmount
    ) {}

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record PagBankCharge(
            String id,
            String referenceId,
            String status,
            String createdAt,
            String paidAt,
            Amount amount,
            PaymentResponse paymentResponse,
            PaymentMethod paymentMethod
    ) {}

    public record Amount(
            long value,
            String currency,
            Summary summary
    ) {}

    public record Summary(
            long total,
            long paid,
            long refunded,
            long incremented
    ) {}

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record PaymentResponse(
            String code,
            String message,
            String reference,
            RawData rawData
    ) {}

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record RawData(
            String authorizationCode,
            String nsu,
            String reasonCode
    ) {}

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record PaymentMethod(
            String type,
            Integer installments,
            Boolean capture,
            Card card,
            String softDescriptor
    ) {}

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record Card(
            String brand,
            String firstDigits,
            String lastDigits,
            String expMonth,
            String expYear,
            Holder holder,
            Issuer issuer,
            String country,
            String product
    ) {}

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record Holder(
            String name,
            String taxId
    ) {}

    public record Issuer(
            String name,
            String product
    ) {}
}