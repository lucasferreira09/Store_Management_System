package com.example.StoreManagement.service;

import com.example.StoreManagement.model.entity.enums.PaymentProvider;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class PaymentGatewayFactory {
    private final Map<PaymentProvider, PaymentGateway> gateways;

    public PaymentGatewayFactory(List<PaymentGateway> gateways) {
        this.gateways = gateways.stream().collect(Collectors.toMap(PaymentGateway::provider, Function.identity()));
    }

    public PaymentGateway get(PaymentProvider provider) {
        PaymentGateway gateway = gateways.get(provider);

        if (gateway == null)
            throw new IllegalArgumentException("No gateway found for provider " + provider);

        return gateway;
    }
}
