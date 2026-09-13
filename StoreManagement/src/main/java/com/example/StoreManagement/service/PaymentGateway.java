package com.example.StoreManagement.service;

import com.example.StoreManagement.model.dtoRequest.CheckoutCreationRequest;
import com.example.StoreManagement.model.dtoResponse.ProviderCheckoutResponse;
import com.example.StoreManagement.model.entity.enums.PaymentProvider;

public interface PaymentGateway {

    PaymentProvider provider();

    ProviderCheckoutResponse processPayment(CheckoutCreationRequest checkoutCreationRequest);

    String getCheckoutUrl(java.lang.String sessionId);

    void expireSession(String sessionId);
}
