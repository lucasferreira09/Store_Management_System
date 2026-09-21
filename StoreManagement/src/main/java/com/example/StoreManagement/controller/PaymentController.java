package com.example.StoreManagement.controller;

import com.example.StoreManagement.utils.PaginationRequest;
import com.example.StoreManagement.utils.PagingResult;
import com.example.StoreManagement.dtos.dtoRequest.PaymentCreationRequest;
import com.example.StoreManagement.dtos.dtoResponse.PaymentCreationResponse;
import com.example.StoreManagement.dtos.dtoResponse.PaymentDtoReponse;
import com.example.StoreManagement.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/payment")
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping()
    public ResponseEntity<PagingResult<PaymentDtoReponse>> getAll(
            @RequestParam(defaultValue = "0", required = false) Integer pageNumber,
            @RequestParam(defaultValue = "10", required = false) Integer size,
            @RequestParam(defaultValue = "id", required = false) String sortField,
            @RequestParam(defaultValue = "ASC", required = false) Sort.Direction direction
    ) {

        PaginationRequest request = new PaginationRequest(pageNumber, size, sortField, direction);
        return ResponseEntity.ok(this.paymentService.findAll(request));
    }

    @GetMapping("/checkoutId/{checkoutId}")
    public ResponseEntity<PaymentDtoReponse> getByCheckoutId(@PathVariable @Valid UUID checkoutId) {
        return ResponseEntity.ok(this.paymentService.findByCheckoutId(checkoutId));
    }

    @PostMapping("/create")
    public ResponseEntity<PaymentCreationResponse> create(@RequestBody @Valid PaymentCreationRequest paymentCreationRequest) {

        return ResponseEntity.status(HttpStatus.CREATED).body(this.paymentService.createPayment(paymentCreationRequest));
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
