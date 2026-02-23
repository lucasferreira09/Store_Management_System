package com.example.StoreManagement.controller;

import com.example.StoreManagement.model.dtoRequest.AddressDtoPostRequest;
import com.example.StoreManagement.model.dtoRequest.AddressDtoPutRequest;
import com.example.StoreManagement.model.dtoResponse.AddressDtoResponse;
import com.example.StoreManagement.service.AddressService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/addresses")
public class AddressController {

    private AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @GetMapping
    public ResponseEntity<List<AddressDtoResponse>> getAll() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.addressService.findAll());
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<AddressDtoResponse> getById(@PathVariable Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.addressService.findById(id));
    }

    @PostMapping
    public ResponseEntity<AddressDtoResponse> create(@RequestBody @Valid AddressDtoPostRequest addressDtoPostRequest) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(this.addressService.create(addressDtoPostRequest));
    }

    @PutMapping("/id/{id}")
    public ResponseEntity<AddressDtoResponse> update(@PathVariable Long id, AddressDtoPutRequest dtoPutRequest) {

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.addressService.update(id, dtoPutRequest));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        this.addressService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
