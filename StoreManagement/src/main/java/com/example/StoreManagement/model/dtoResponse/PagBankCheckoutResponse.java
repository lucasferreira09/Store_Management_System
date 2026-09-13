package com.example.StoreManagement.model.dtoResponse;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record PagBankCheckoutResponse(
        String id,
        String referenceId,
        String createdAt,
        String expirationDate,
        String status,
        List<PaymentMethods> paymentMethods,
        List<Item> items,
        List<Link> links
) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record PaymentMethods(
            String type
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record Item(
            String referenceId,
            String name,
            int quantity,
            long unitAmount
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record Link(
            String rel,
            String href,
            String method
    ) {}


}
