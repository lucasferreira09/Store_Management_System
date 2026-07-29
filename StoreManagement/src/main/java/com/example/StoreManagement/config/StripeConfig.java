package com.example.StoreManagement.config;

import com.stripe.Stripe;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StripeConfig {

    @Value("${stripe.secret-key}")
    private String stripeSecretKey;

    @Value("${stripe.webhook-key}")
    private String webhookKey;

    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeSecretKey;
    }

}
