package com.example.StoreManagement.controller;

import com.example.StoreManagement.service.StripeService;
import com.stripe.exception.StripeException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/stripe")
public class StripeController {

    private final StripeService stripeService;

    @PostMapping("/checkout/sessions/id/{id}/pay")
    public ResponseEntity<String> payCheckoutSession(@PathVariable String id) throws StripeException {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(stripeService.payCheckoutSession(id));
    }

 /*
    @PostMapping("/checkout/sessions/{id}/expire")
    public ResponseEntity<Void> expireCheckoutSession( @PathVariable String id) throws StripeException {
        this.stripeService.expireCheckoutSession(id);

        return ResponseEntity.ok().build();
    }

  */
}
