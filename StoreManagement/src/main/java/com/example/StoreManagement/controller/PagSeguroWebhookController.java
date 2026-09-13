package com.example.StoreManagement.controller;

import com.example.StoreManagement.service.PagSeguroWebhookService;
import com.example.StoreManagement.service.PagSeguroWebhookVerifier;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/webhook/pagbank")
public class PagSeguroWebhookController {

    private final PagSeguroWebhookService pagSeguroWebhookService;
    private final PagSeguroWebhookVerifier pagSeguroWebhookVerifier;

    @PostMapping("/checkout")
    public ResponseEntity<Void> handleWebhook(
            @RequestBody String payload,
            @RequestHeader(value = "x-authenticity-token", required = false) String authenticityToken,
            @RequestHeader(value = "x-product-id", required = false) String productId
    ) {
        /*
        _ Authenticity token doesn't work in SandBox mode _
        if(!pagSeguroWebhookVerifier.isValid(payload, authenticityToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }*/

        pagSeguroWebhookService.handleWebhook(payload, productId);
        return ResponseEntity.ok().build();
    }


    @PostMapping("/payment")
    public ResponseEntity<Void> paymentWebhook(
            @RequestBody String payload,
            @RequestHeader(value = "x-authenticity-token", required = false) String authenticityToken,
            @RequestHeader(value = "x-product-id", required = false) String productId
    ) {
        /*
        _ Authenticity token doesn't work in SandBox mode _
        if(!pagSeguroWebhookVerifier.isValid(payload, authenticityToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }*/

        pagSeguroWebhookService.handleWebhook(payload, productId);
        return ResponseEntity.ok().build();
    }
}
