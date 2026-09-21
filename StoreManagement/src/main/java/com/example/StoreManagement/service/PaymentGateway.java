package com.example.StoreManagement.service;

import com.example.StoreManagement.dtos.dtoRequest.CheckoutCreationRequest;
import com.example.StoreManagement.dtos.dtoResponse.ProviderCheckoutResponse;
import com.example.StoreManagement.enums.PaymentProvider;

public interface PaymentGateway {

    PaymentProvider provider();

    ProviderCheckoutResponse processPayment(CheckoutCreationRequest checkoutCreationRequest);

    String getCheckoutUrl(java.lang.String sessionId);

    void expireSession(String sessionId);
}
