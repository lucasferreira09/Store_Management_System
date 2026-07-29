package com.example.StoreManagement.controller;

import com.example.StoreManagement.service.StripeService;
import com.stripe.exception.StripeException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/webhook")
public class WebhookController {

    private final StripeService stripeService;

    @PostMapping("/stripe")
    public ResponseEntity<String> handleStripeWebhook(@RequestHeader("Stripe-Signature") String sigHeader, @RequestBody String payload) throws IllegalAccessException, StripeException {

        String result = this.stripeService.handleWebhookCheckout(sigHeader, payload);

        return ResponseEntity.ok(result);
    }
}
