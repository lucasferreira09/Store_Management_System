package com.example.StoreManagement.controller;

import com.example.StoreManagement.model.dtoRequest.CustomerAddressDtoPostRequest;
import com.example.StoreManagement.model.dtoRequest.CustomerAddressPutRequest;
import com.example.StoreManagement.model.dtoResponse.CustomerAddressDetailsResponse;
import com.example.StoreManagement.model.dtoResponse.CustomerAddressResponse;
import com.example.StoreManagement.service.CustomerAddressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController()
@RequestMapping("/customerAddress")
public class CustomerAddressController {

    private final CustomerAddressService customerAddressService;

    @GetMapping
    public ResponseEntity<List<CustomerAddressResponse>> getAll() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.customerAddressService.findAll());
    }

    @GetMapping("/customerId/{id}/addresses")
    public ResponseEntity<List<CustomerAddressDetailsResponse>> getByCustomerId(@PathVariable Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.customerAddressService.findByCustomerId(id));
    }

    @GetMapping("/addressId/{id}/customers")
    public ResponseEntity<List<CustomerAddressDetailsResponse>> getByAddressId(@PathVariable Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.customerAddressService.findByAddressId(id));
    }


    @PostMapping
    public ResponseEntity<CustomerAddressResponse> create(
            @RequestBody @Valid CustomerAddressDtoPostRequest dtoPostRequest
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(this.customerAddressService.create(dtoPostRequest));
    }

    @PutMapping("/customerId/{customerId}/addressId/{addressId}")
    public ResponseEntity<CustomerAddressResponse> update(@PathVariable Long customerId, @PathVariable  Long addressId, CustomerAddressPutRequest dtoPutRequest) {

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.customerAddressService.update(customerId, addressId, dtoPutRequest));
    }

    @DeleteMapping()
    public ResponseEntity<Void> delete(CustomerAddressDtoPostRequest dtoPostRequest) {

        this.customerAddressService.delete(dtoPostRequest);
        return ResponseEntity.noContent().build();
    }
}
