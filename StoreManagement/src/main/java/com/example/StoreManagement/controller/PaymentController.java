package com.example.StoreManagement.controller;

import com.example.StoreManagement.model.dtoRequest.PaymentCreationRequest;
import com.example.StoreManagement.model.dtoResponse.PaymentCreationResponse;
import com.example.StoreManagement.model.dtoResponse.PaymentDtoReponse;
import com.example.StoreManagement.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/payment")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/create")
    public ResponseEntity<PaymentCreationResponse> create(@RequestBody @Valid PaymentCreationRequest paymentCreationRequest) {

        return ResponseEntity.status(HttpStatus.CREATED).body(this.paymentService.createPayment(paymentCreationRequest));
    }

    @GetMapping("/checkoutId/{id}")
    public ResponseEntity<PaymentDtoReponse> getByCheckoutId(@PathVariable @Valid UUID checkoutId) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(this.paymentService.findByCheckoutId(checkoutId));
    }

    @PostMapping("/url/checkoutId/{checkoutId}")
    public ResponseEntity<String> getCheckoutUrl(@PathVariable @Valid UUID checkoutId) {

        return ResponseEntity.ok(this.paymentService.getCheckoutUrl(checkoutId));
    }


    @PostMapping("/expire/checkoutId/{checkoutId}")
    public ResponseEntity<Void> expirePayment(@PathVariable @Valid UUID checkoutId, String motive) {

        this.paymentService.expirePayment(checkoutId, motive);
        return ResponseEntity.noContent().build();
    }
}
