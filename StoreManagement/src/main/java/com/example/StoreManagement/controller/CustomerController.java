package com.example.StoreManagement.controller;

import com.example.StoreManagement.dtos.dtoRequest.CustomerDtoPutRequest;
import com.example.StoreManagement.utils.PaginationRequest;
import com.example.StoreManagement.utils.PagingResult;
import com.example.StoreManagement.dtos.dtoRequest.CustomerDtoPostRequest;
import com.example.StoreManagement.dtos.dtoResponse.CustomerDtoDetailResponse;
import com.example.StoreManagement.dtos.dtoResponse.CustomerDtoResponse;
import com.example.StoreManagement.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerService customerService;


    @GetMapping
    public ResponseEntity<PagingResult<CustomerDtoResponse>> getAll(
            @RequestParam(defaultValue = "0", required = false) Integer pageNumber,
            @RequestParam(defaultValue = "10", required = false) Integer size,
            @RequestParam(defaultValue = "id", required = false) String sortField,
            @RequestParam(defaultValue = "ASC", required = false) Sort.Direction sortDirection
    ) {
        PaginationRequest request = new PaginationRequest(pageNumber, size, sortField, sortDirection);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.customerService.findAll(request));
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<CustomerDtoResponse> getById(@PathVariable Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.customerService.findById(id));
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<PagingResult<CustomerDtoResponse>> getByName(
            @PathVariable String name,
            @RequestParam(defaultValue = "0", required = false) Integer pageNumber,
            @RequestParam(defaultValue = "10", required = false) Integer size,
            @RequestParam(defaultValue = "id", required = false) String sortField,
            @RequestParam(defaultValue = "ASC", required = false) Sort.Direction sortDirection
    ) {
        PaginationRequest request = new PaginationRequest(pageNumber, size, sortField, sortDirection);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.customerService.findByName(name, request));
    }

    @GetMapping("/cpf/{cpf}")
    public ResponseEntity<CustomerDtoResponse> getByCpf(@PathVariable String cpf) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.customerService.findByCpf(cpf));
    }

    @PostMapping
    public ResponseEntity<CustomerDtoDetailResponse> create(@RequestBody @Valid CustomerDtoPostRequest customerDtoPostRequest) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(this.customerService.create(customerDtoPostRequest));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerDtoDetailResponse> update(
            @PathVariable Long id, @RequestBody @Valid CustomerDtoPutRequest putRequest
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.customerService.update(id, putRequest));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        this.customerService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
