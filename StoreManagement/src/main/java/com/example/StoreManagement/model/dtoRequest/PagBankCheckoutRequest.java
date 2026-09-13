package com.example.StoreManagement.model.dtoRequest;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.util.List;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record PagBankCheckoutRequest(
        String referenceId,
        boolean customerModifiable,
        List<Item> items,
        List<PaymentMethod> paymentMethods,
        List<PaymentMethodConfig> paymentMethodsConfigs,
        List<String> notificationUrls,
        List<String> paymentNotificationUrls,
        String softDescriptor,
        String redirectUrl,
        String returnUrl
) {
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record Item(
            String referenceId,
            String name,
            int quantity,
            int unitAmount
    ) {}

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record PaymentMethod(
            String type
    ) {}

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record PaymentMethodConfig(
            String type,
            List<ConfigOption> configOptions
    ) {}

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record ConfigOption(
            String option,
            String value
    ) {}
}

