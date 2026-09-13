package com.example.StoreManagement.controller;

import com.example.StoreManagement.service.StripePaymentGateway;
import com.example.StoreManagement.service.StripeWebhookService;
import com.stripe.exception.StripeException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/webhook")
public class StripeWebhookController {

    private final StripePaymentGateway stripePaymentGateway;
    private final StripeWebhookService stripeWebhookService;

    @PostMapping("/stripe")
    public ResponseEntity<String> handleWebhook(
            @RequestHeader("Stripe-Signature") String sigHeader,
            @RequestBody String payload) throws IllegalAccessException, StripeException {

        String result = this.stripeWebhookService.handleWebhook(sigHeader, payload);
        return ResponseEntity.ok(result);
    }


}
