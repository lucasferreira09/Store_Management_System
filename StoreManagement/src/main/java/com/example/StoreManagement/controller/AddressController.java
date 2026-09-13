package com.example.StoreManagement.controller;

import com.example.StoreManagement.model.PaginationRequest;
import com.example.StoreManagement.model.PagingResult;
import com.example.StoreManagement.model.dtoRequest.AddressDtoPostRequest;
import com.example.StoreManagement.model.dtoRequest.AddressDtoPutRequest;
import com.example.StoreManagement.model.dtoResponse.AddressDtoResponse;
import com.example.StoreManagement.service.AddressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
@RequestMapping("/addresses")
public class AddressController {

    private final AddressService addressService;

    @GetMapping
    public ResponseEntity<PagingResult<AddressDtoResponse>> getAll(
            @RequestParam(defaultValue = "0", required = false) Integer pageNumber,
            @RequestParam(defaultValue = "10", required = false) Integer size,
            @RequestParam(defaultValue = "id", required = false) String sortField,
            @RequestParam(defaultValue = "ASC", required = false) Sort.Direction direction
            ) {

        PaginationRequest request = new PaginationRequest(pageNumber,size, sortField, direction);
        PagingResult<AddressDtoResponse> orders = this.addressService.findAll(request);
        return ResponseEntity.ok(orders);
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
