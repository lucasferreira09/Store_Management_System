package com.example.StoreManagement.controller;

import com.example.StoreManagement.model.dtoRequest.CustomerDtoPostRequest;
import com.example.StoreManagement.model.dtoResponse.CustomerDtoResponse;
import com.example.StoreManagement.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public ResponseEntity<List<CustomerDtoResponse>> getAll() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.customerService.findAll());
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<CustomerDtoResponse> getById(@PathVariable Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.customerService.findById(id));
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<List<CustomerDtoResponse>> getByName(@PathVariable String name) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.customerService.findByName(name));
    }

    @GetMapping("/cpf/{cpf}")
    public ResponseEntity<CustomerDtoResponse> getByCpf(@PathVariable String cpf) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.customerService.findByCpf(cpf));
    }

    @PostMapping
    public ResponseEntity<CustomerDtoResponse> create(@RequestBody @Valid CustomerDtoPostRequest customerDtoPostRequest) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(this.customerService.create(customerDtoPostRequest));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerDtoResponse> update(
            @PathVariable Long id, @RequestBody @Valid CustomerDtoPostRequest customerDtoPostRequest
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.customerService.update(id, customerDtoPostRequest));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        this.customerService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
